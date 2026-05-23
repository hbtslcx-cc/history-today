package com.historytoday.data.remote

import com.historytoday.domain.model.EventCategory
import com.historytoday.domain.model.HistoryEvent
import com.historytoday.domain.model.HistoryPeriod
import com.historytoday.domain.model.RegionType

interface EventRemoteDataSource {
    suspend fun fetchEventsByDate(
        month: Int,
        day: Int,
        category: EventCategory,
        region: RegionType,
        period: HistoryPeriod
    ): List<HistoryEvent>
    
    suspend fun fetchAllEvents(): List<HistoryEvent>
    
    suspend fun fetchEventById(id: String): HistoryEvent?
    
    suspend fun searchEvents(query: String): List<HistoryEvent>
}
