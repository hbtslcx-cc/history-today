package com.historytoday.domain.usecase

import com.historytoday.domain.model.EventCategory
import com.historytoday.domain.model.HistoryEvent
import com.historytoday.domain.model.HistoryPeriod
import com.historytoday.domain.model.RegionType
import com.historytoday.domain.repository.EventRepository
import javax.inject.Inject

class GetEventsByDateUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(
        date: String,
        category: EventCategory,
        region: RegionType,
        period: HistoryPeriod
    ): List<HistoryEvent> {
        return repository.getEventsByDate(date, category, region, period)
    }
}
