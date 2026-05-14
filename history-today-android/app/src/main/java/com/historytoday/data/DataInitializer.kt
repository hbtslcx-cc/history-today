package com.historytoday.data

import android.content.Context
import com.historytoday.data.local.EventDao
import com.historytoday.data.local.EventEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class DataInitializer(
    private val context: Context,
    private val eventDao: EventDao
) {

    suspend fun initialize() {
        if (eventDao.getCount() > 0) return

        val json = context.assets.open("events.json").bufferedReader().use { it.readText() }
        val eventData = Json.decodeFromString<EventData>(json)
        
        val entities = eventData.events.map { event ->
            EventEntity(
                id = event.id,
                title = event.title,
                date = event.date,
                year = event.year,
                category = event.category,
                region = event.region,
                period = event.period,
                importance = when (event.importance) {
                    5 -> "S"
                    4 -> "A"
                    3 -> "B"
                    2 -> "C"
                    else -> "D"
                },
                description = event.description,
                shortDesc = event.shortDesc,
                imageUrl = event.imageUrl,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        }
        
        eventDao.insertAll(entities)
    }
}

@Serializable
data class EventData(
    val version: String,
    val totalEvents: Int,
    val events: List<EventItem>
)

@Serializable
data class EventItem(
    val id: String,
    val title: String,
    val date: String,
    val year: Int,
    val category: String,
    val region: String,
    val period: String,
    val shortDesc: String,
    val description: String,
    val imageUrl: String?,
    val importance: Int
)
