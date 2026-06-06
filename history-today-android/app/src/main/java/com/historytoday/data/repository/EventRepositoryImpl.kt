package com.historytoday.data.repository

import com.historytoday.data.local.EventDao
import com.historytoday.data.mapper.toDomain
import com.historytoday.data.remote.EventRemoteDataSource
import com.historytoday.domain.model.EventCategory
import com.historytoday.domain.model.HistoryEvent
import com.historytoday.domain.model.HistoryPeriod
import com.historytoday.domain.model.RegionType
import com.historytoday.domain.repository.EventRepository

class EventRepositoryImpl(
    private val eventDao: EventDao,
    private val remoteDataSource: EventRemoteDataSource
) : EventRepository {

    override suspend fun getEventsByDate(
        date: String,
        category: EventCategory,
        region: RegionType,
        period: HistoryPeriod
    ): List<HistoryEvent> {
        val localEvents = fetchFromLocal(date, category, region, period)
        
        if (localEvents.isNotEmpty()) {
            return localEvents
        }
        
        val month = date.split("-")[0].toInt()
        val day = date.split("-")[1].toInt()
        val remoteEvents = remoteDataSource.fetchEventsByDate(month, day, category, region, period)
        
        if (remoteEvents.isNotEmpty()) {
            eventDao.insertAll(remoteEvents.map { it.toEntity() })
            return remoteEvents
        }
        
        return emptyList()
    }

    override suspend fun getEventById(id: String): HistoryEvent? {
        return eventDao.getEventById(id)?.toDomain()
            ?: remoteDataSource.fetchEventById(id)?.also {
                eventDao.insertAll(listOf(it.toEntity()))
            }
    }

    override suspend fun searchEvents(query: String): List<HistoryEvent> {
        val localEvents = eventDao.searchEvents(query).map { it.toDomain() }
        
        if (localEvents.isNotEmpty()) {
            return localEvents
        }
        
        val remoteEvents = remoteDataSource.searchEvents(query)
        
        if (remoteEvents.isNotEmpty()) {
            eventDao.insertAll(remoteEvents.map { it.toEntity() })
        }
        
        return remoteEvents
    }

    suspend fun syncRemoteData(): Int {
        val remoteEvents = remoteDataSource.fetchAllEvents()
        if (remoteEvents.isEmpty()) return 0
        
        val existingIds = eventDao.getEventsByDate("01-01").map { it.id }.toSet()
        val newEvents = remoteEvents.filter { !existingIds.contains(it.id) }
        
        if (newEvents.isNotEmpty()) {
            eventDao.insertAll(newEvents.map { it.toEntity() })
        }
        
        return newEvents.size
    }

    private suspend fun fetchFromLocal(
        date: String,
        category: EventCategory,
        region: RegionType,
        period: HistoryPeriod
    ): List<HistoryEvent> {
        return when {
            region != RegionType.ALL && period != HistoryPeriod.ALL && category != EventCategory.ALL -> {
                eventDao.getEventsByDateRegionPeriodAndCategory(date, region.name, period.name, category.name)
                    .map { it.toDomain() }
            }
            region != RegionType.ALL && period != HistoryPeriod.ALL -> {
                eventDao.getEventsByDateRegionAndPeriod(date, region.name, period.name)
                    .map { it.toDomain() }
            }
            region != RegionType.ALL && category != EventCategory.ALL -> {
                eventDao.getEventsByDateRegionAndCategory(date, region.name, category.name)
                    .map { it.toDomain() }
            }
            region != RegionType.ALL -> {
                eventDao.getEventsByDateAndRegion(date, region.name).map { it.toDomain() }
            }
            category != EventCategory.ALL -> {
                eventDao.getEventsByDateAndCategory(date, category.name).map { it.toDomain() }
            }
            else -> {
                eventDao.getEventsByDate(date).map { it.toDomain() }
            }
        }
    }
}

private fun HistoryEvent.toEntity(): com.historytoday.data.local.EventEntity {
    return com.historytoday.data.local.EventEntity(
        id = id,
        title = title,
        date = date,
        year = year,
        category = category.name,
        region = region.name,
        period = period.name,
        importance = importance.name,
        description = description,
        shortDesc = shortDesc,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
