package com.historytoday.domain.repository

import com.historytoday.domain.model.CalendarDay
import com.historytoday.domain.model.LunarInfo
import java.time.LocalDate

interface CalendarRepository {
    suspend fun getCalendarMonth(year: Int, month: Int): List<CalendarDay>
    suspend fun getLunarInfo(date: LocalDate): LunarInfo
    suspend fun getEventCountByDate(date: String): Int
}
