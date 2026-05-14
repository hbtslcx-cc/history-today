package com.historytoday.data.repository

import com.historytoday.data.local.EventDao
import com.historytoday.data.mapper.toDomain
import com.historytoday.domain.model.EventCategory
import com.historytoday.domain.model.HistoryEvent
import com.historytoday.domain.model.HistoryPeriod
import com.historytoday.domain.model.RegionType
import com.historytoday.domain.repository.EventRepository

class EventRepositoryImpl(
    private val eventDao: EventDao
) : EventRepository {

    override suspend fun getEventsByDate(
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

    override suspend fun getEventById(id: String): HistoryEvent? {
        return eventDao.getEventById(id)?.toDomain()
    }

    override suspend fun searchEvents(query: String): List<HistoryEvent> {
        return eventDao.searchEvents(query).map { it.toDomain() }
    }
}
