package com.historytoday.domain.usecase

import com.historytoday.domain.model.CalendarDay
import com.historytoday.domain.repository.CalendarRepository
import javax.inject.Inject

class GetCalendarMonthUseCase @Inject constructor(
    private val repository: CalendarRepository
) {
    suspend operator fun invoke(year: Int, month: Int): List<CalendarDay> {
        return repository.getCalendarMonth(year, month)
    }
}
