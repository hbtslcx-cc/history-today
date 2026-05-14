package com.historytoday.domain.model

data class HistoryEvent(
    val id: String,
    val title: String,
    val date: String,
    val year: Int,
    val category: EventCategory,
    val region: RegionType,
    val period: HistoryPeriod,
    val description: String,
    val shortDesc: String,
    val imageUrl: String?,
    val createdAt: Long,
    val updatedAt: Long
)
