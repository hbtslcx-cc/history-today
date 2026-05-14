package com.historytoday.domain.usecase

import com.historytoday.domain.model.HistoryEvent
import com.historytoday.domain.repository.EventRepository
import javax.inject.Inject

class GetEventByIdUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(id: String): HistoryEvent? {
        return repository.getEventById(id)
    }
}
