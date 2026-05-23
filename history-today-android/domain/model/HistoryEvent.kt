package com.historytoday.domain.model

data class HistoryEvent(
    val id: String,
    val title: String,
    val date: String,
    val year: Int,
    val category: EventCategory,
    val region: RegionType,
    val period: HistoryPeriod,
    val importance: ImportanceLevel,
    val description: String,
    val shortDesc: String,
    val imageUrl: String?,
    val createdAt: Long,
    val updatedAt: Long
) {
    enum class ImportanceLevel {
        S, A, B, C, D;
        
        fun getStars(): String = when (this) {
            S -> "⭐⭐⭐⭐⭐"
            A -> "⭐⭐⭐⭐"
            B -> "⭐⭐⭐"
            C -> "⭐⭐"
            D -> "⭐"
        }
        
        companion object {
            fun fromInt(value: Int): ImportanceLevel {
                return when (value) {
                    5 -> S
                    4 -> A
                    3 -> B
                    2 -> C
                    1 -> D
                    else -> C
                }
            }
        }
    }
}