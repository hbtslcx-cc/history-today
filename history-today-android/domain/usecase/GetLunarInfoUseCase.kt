package com.historytoday.domain.usecase

import com.historytoday.domain.model.LunarInfo
import com.historytoday.domain.repository.CalendarRepository
import java.time.LocalDate
import javax.inject.Inject

class GetLunarInfoUseCase @Inject constructor(
    private val repository: CalendarRepository
) {
    suspend operator fun invoke(date: LocalDate): LunarInfo {
        return repository.getLunarInfo(date)
    }
}
