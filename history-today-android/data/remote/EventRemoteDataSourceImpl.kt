package com.historytoday.data.remote

import com.historytoday.domain.model.EventCategory
import com.historytoday.domain.model.HistoryEvent
import com.historytoday.domain.model.HistoryPeriod
import com.historytoday.domain.model.RegionType
import java.time.LocalDate

class EventRemoteDataSourceImpl(
    private val dayInHistoryApi: DayInHistoryApiService,
    private val touTiaoApi: TouTiaoApiService
) : EventRemoteDataSource {

    override suspend fun fetchEventsByDate(
        month: Int,
        day: Int,
        category: EventCategory,
        region: RegionType,
        period: HistoryPeriod
    ): List<HistoryEvent> {
        return fetchWithFallback(
            primary = { dayInHistoryApi.getTodayEvents(month, day) },
            fallback = { touTiaoApi.getTodayEvents() },
            date = "$month-$day"
        )
    }

    override suspend fun fetchAllEvents(): List<HistoryEvent> {
        val allEvents = mutableListOf<HistoryEvent>()
        var page = 1
        var hasMore = true
        
        while (hasMore) {
            try {
                val response = dayInHistoryApi.getAllEvents(page, 100)
                if (response.isSuccessful) {
                    val events = response.body()?.events?.map { it.toDomain() } ?: emptyList()
                    if (events.isEmpty()) {
                        hasMore = false
                    } else {
                        allEvents.addAll(events)
                        page++
                    }
                } else {
                    hasMore = false
                }
            } catch (e: Exception) {
                hasMore = false
            }
        }
        
        return allEvents
    }

    override suspend fun fetchEventById(id: String): HistoryEvent? {
        return try {
            val response = dayInHistoryApi.getEventById(id)
            if (response.isSuccessful) {
                response.body()?.toDomain()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun searchEvents(query: String): List<HistoryEvent> {
        return try {
            val response = dayInHistoryApi.searchEvents(query)
            if (response.isSuccessful) {
                response.body()?.events?.map { it.toDomain() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun fetchWithFallback(
        primary: suspend () -> retrofit2.Response<DayInHistoryResponse>,
        fallback: suspend () -> retrofit2.Response<TouTiaoResponse>,
        date: String
    ): List<HistoryEvent> {
        return try {
            val response = primary()
            if (response.isSuccessful && response.body()?.events?.isNotEmpty() == true) {
                response.body()!!.events.map { it.toDomain() }
            } else {
                fallbackToTouTiao(fallback, date)
            }
        } catch (e: Exception) {
            fallbackToTouTiao(fallback, date)
        }
    }

    private suspend fun fallbackToTouTiao(
        fallback: suspend () -> retrofit2.Response<TouTiaoResponse>,
        date: String
    ): List<HistoryEvent> {
        return try {
            val response = fallback()
            if (response.isSuccessful && response.body()?.events?.isNotEmpty() == true) {
                response.body()!!.events.map { it.toDomain(date) }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

fun DayInHistoryItem.toDomain(): HistoryEvent {
    return HistoryEvent(
        id = id,
        title = title,
        date = date,
        year = year,
        category = runCatching { EventCategory.valueOf(category) }.getOrDefault(EventCategory.CULTURE),
        region = runCatching { RegionType.valueOf(region) }.getOrDefault(RegionType.INTERNATIONAL),
        period = runCatching { HistoryPeriod.valueOf(period) }.getOrDefault(HistoryPeriod.ALL),
        description = description,
        shortDesc = shortDesc,
        imageUrl = imageUrl,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis(),
        importance = runCatching { HistoryEvent.ImportanceLevel.fromInt(importance) }
            .getOrDefault(HistoryEvent.ImportanceLevel.C)
    )
}

fun TouTiaoEvent.toDomain(date: String): HistoryEvent {
    val yearInt = runCatching { year.toInt() }.getOrDefault(0)
    val region = determineRegion(title)
    
    return HistoryEvent(
        id = "toutiao_${date}_${title.hashCode()}",
        title = title,
        date = date,
        year = yearInt,
        category = determineCategory(title),
        region = region,
        period = determinePeriod(yearInt, region),
        description = desc,
        shortDesc = if (desc.length > 50) desc.substring(0, 50) + "..." else desc,
        imageUrl = null,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis(),
        importance = estimateImportance(title, desc)
    )
}

private fun determineRegion(title: String): RegionType {
    val chinaKeywords = listOf("中国", "中华", "清朝", "明朝", "唐朝", "宋朝", "元朝", 
                               "民国", "北京", "上海", "香港", "台湾", "澳门")
    return if (chinaKeywords.any { title.contains(it) }) {
        RegionType.DOMESTIC
    } else {
        RegionType.INTERNATIONAL
    }
}

private fun determineCategory(title: String): EventCategory {
    val keywords = mapOf(
        EventCategory.POLITICS to listOf("政治", "战争", "条约", "独立", "建国", "革命", "政变", "改革", "政策"),
        EventCategory.TECH to listOf("科技", "发明", "发现", "航天", "计算机", "互联网", "人工智能"),
        EventCategory.CULTURE to listOf("文化", "艺术", "文学", "绘画", "音乐", "电影", "书籍"),
        EventCategory.SPORTS to listOf("体育", "奥运", "足球", "篮球", "比赛", "冠军"),
        EventCategory.WAR to listOf("战争", "战役", "军事", "抗战", "胜利", "战败"),
        EventCategory.PEOPLE to listOf("出生", "逝世", "诞辰", "去世", "逝世")
    )
    
    return keywords.entries.firstOrNull { (_, kw) -> kw.any { title.contains(it) } }?.key
        ?: EventCategory.CULTURE
}

private fun determinePeriod(year: Int, region: RegionType): HistoryPeriod {
    if (region != RegionType.DOMESTIC) return HistoryPeriod.ALL
    
    return when {
        year == 0 -> HistoryPeriod.ALL
        year < 1840 -> HistoryPeriod.ANCIENT
        year in 1840..1949 -> HistoryPeriod.MODERN
        else -> HistoryPeriod.CONTEMPORARY
    }
}

private fun estimateImportance(title: String, desc: String): HistoryEvent.ImportanceLevel {
    val importantKeywords = listOf("首次", "第一", "著名", "伟大", "重要", "开国", 
                                   "革命", "战争", "条约", "原子弹", "航天", "诺贝尔奖")
    
    val matchCount = importantKeywords.count { title.contains(it) || desc.contains(it) }
    
    return when {
        matchCount >= 2 -> HistoryEvent.ImportanceLevel.A
        matchCount == 1 -> HistoryEvent.ImportanceLevel.B
        else -> HistoryEvent.ImportanceLevel.C
    }
}
