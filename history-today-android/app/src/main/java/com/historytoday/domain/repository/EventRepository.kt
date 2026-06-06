package com.historytoday.domain.repository

import com.historytoday.domain.model.EventCategory
import com.historytoday.domain.model.HistoryEvent
import com.historytoday.domain.model.HistoryPeriod
import com.historytoday.domain.model.RegionType

interface EventRepository {
    suspend fun getEventsByDate(
        date: String,
        category: EventCategory,
        region: RegionType,
        period: HistoryPeriod
    ): List<HistoryEvent>
    suspend fun getEventById(id: String): HistoryEvent?
    suspend fun searchEvents(query: String): List<HistoryEvent>
}
