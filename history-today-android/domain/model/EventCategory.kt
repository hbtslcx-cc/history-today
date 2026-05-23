package com.historytoday.domain.model

import androidx.compose.ui.graphics.Color

enum class EventCategory {
    ALL, POLITICS, TECH, CULTURE, SPORTS, WAR, PEOPLE;

    fun getDisplayName(): String = when (this) {
        ALL -> "全部"
        POLITICS -> "政治"
        TECH -> "科技"
        CULTURE -> "文化"
        SPORTS -> "体育"
        WAR -> "战争"
        PEOPLE -> "人物"
    }

    fun getColor(): Color = when (this) {
        ALL -> Color.Gray
        POLITICS -> Color(0xFF1e3a5f)
        TECH -> Color(0xFF3498db)
        CULTURE -> Color(0xFF9b59b6)
        SPORTS -> Color(0xFFe67e22)
        WAR -> Color(0xFFc0392b)
        PEOPLE -> Color(0xFF27ae60)
    }
}

enum class RegionType {
    ALL, DOMESTIC, INTERNATIONAL;

    fun getDisplayName(): String = when (this) {
        ALL -> "全部"
        DOMESTIC -> "国内"
        INTERNATIONAL -> "国外"
    }
}

enum class HistoryPeriod {
    ALL, ANCIENT, MODERN, CONTEMPORARY;

    fun getDisplayName(): String = when (this) {
        ALL -> "全部"
        ANCIENT -> "古代"
        MODERN -> "近代"
        CONTEMPORARY -> "现代"
    }

    fun getYearRange(): Pair<Int, Int>? = when (this) {
        ANCIENT -> Pair(Int.MIN_VALUE, 1839)
        MODERN -> Pair(1840, 1948)
        CONTEMPORARY -> Pair(1949, Int.MAX_VALUE)
        ALL -> null
    }
}
