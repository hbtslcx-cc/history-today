package com.historytoday.domain.model

data class LunarInfo(
    val lunarDate: String,
    val lunarYear: String,
    val zodiac: String,
    val ganZhi: String,
    val solarTerm: String?,
    val festival: String?,
    val yi: List<String>,
    val ji: List<String>
)
