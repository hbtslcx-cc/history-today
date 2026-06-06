package com.historytoday.data.repository

import com.historytoday.data.local.EventDao
import com.historytoday.domain.model.CalendarDay
import com.historytoday.domain.model.LunarInfo
import com.historytoday.domain.repository.CalendarRepository
import com.historytoday.lunar.LunarCalendar
import java.time.LocalDate
import java.time.YearMonth

class CalendarRepositoryImpl(
    private val eventDao: EventDao
) : CalendarRepository {

    override suspend fun getCalendarMonth(year: Int, month: Int): List<CalendarDay> {
        val today = LocalDate.now()
        val yearMonth = YearMonth.of(year, month)
        val firstDay = yearMonth.atDay(1)
        val lastDay = yearMonth.atEndOfMonth()
        val startDay = firstDay.minusDays(firstDay.dayOfWeek.value.toLong() - 1)

        val days = mutableListOf<CalendarDay>()
        var currentDay = startDay

        while (currentDay <= lastDay.plusDays(6 - lastDay.dayOfWeek.value.toLong())) {
            val lunarInfo = LunarCalendar.solarToLunar(currentDay)
            val dateStr = formatDate(currentDay)
            val eventCount = eventDao.getEventCountByDate(dateStr)

            days.add(
                CalendarDay(
                    date = currentDay,
                    lunarInfo = lunarInfo,
                    isToday = currentDay == today,
                    isSelected = false,
                    eventCount = eventCount,
                    hasSolarTerm = lunarInfo.solarTerm != null,
                    hasFestival = lunarInfo.festival != null
                )
            )
            currentDay = currentDay.plusDays(1)
        }

        return days
    }

    override suspend fun getLunarInfo(date: LocalDate): LunarInfo {
        return LunarCalendar.solarToLunar(date)
    }

    override suspend fun getEventCountByDate(date: String): Int {
        return eventDao.getEventCountByDate(date)
    }

    private fun formatDate(date: LocalDate): String {
        return String.format("%02d-%02d", date.monthValue, date.dayOfMonth)
    }
}
