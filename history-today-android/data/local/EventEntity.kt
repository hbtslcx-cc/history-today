package com.historytoday.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey val id: String,
    val title: String,
    val date: String,
    val year: Int,
    val category: String,
    val region: String,
    val period: String,
    val description: String,
    val shortDesc: String,
    val imageUrl: String?,
    val createdAt: Long,
    val updatedAt: Long
)
