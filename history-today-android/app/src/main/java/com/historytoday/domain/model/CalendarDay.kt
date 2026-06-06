package com.historytoday.domain.model

import java.time.LocalDate

data class CalendarDay(
    val date: LocalDate,
    val lunarInfo: LunarInfo,
    val isToday: Boolean,
    val isSelected: Boolean,
    val eventCount: Int,
    val hasSolarTerm: Boolean,
    val hasFestival: Boolean
)
