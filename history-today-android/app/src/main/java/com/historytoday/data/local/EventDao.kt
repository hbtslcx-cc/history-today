package com.historytoday.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface EventDao {

    @Query("SELECT * FROM events WHERE date = :date ORDER BY year DESC")
    suspend fun getEventsByDate(date: String): List<EventEntity>

    @Query("SELECT * FROM events WHERE date = :date AND category = :category ORDER BY year DESC")
    suspend fun getEventsByDateAndCategory(date: String, category: String): List<EventEntity>

    @Query("SELECT * FROM events WHERE date = :date AND region = :region ORDER BY year DESC")
    suspend fun getEventsByDateAndRegion(date: String, region: String): List<EventEntity>

    @Query("SELECT * FROM events WHERE date = :date AND region = :region AND period = :period ORDER BY year DESC")
    suspend fun getEventsByDateRegionAndPeriod(date: String, region: String, period: String): List<EventEntity>

    @Query("SELECT * FROM events WHERE date = :date AND region = :region AND category = :category ORDER BY year DESC")
    suspend fun getEventsByDateRegionAndCategory(date: String, region: String, category: String): List<EventEntity>

    @Query("SELECT * FROM events WHERE date = :date AND region = :region AND period = :period AND category = :category ORDER BY year DESC")
    suspend fun getEventsByDateRegionPeriodAndCategory(
        date: String,
        region: String,
        period: String,
        category: String
    ): List<EventEntity>

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEventById(id: String): EventEntity?

    @Query("SELECT * FROM events WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY year DESC")
    suspend fun searchEvents(query: String): List<EventEntity>

    @Query("SELECT COUNT(*) FROM events WHERE date = :date")
    suspend fun getEventCountByDate(date: String): Int

    @Insert
    suspend fun insertAll(events: List<EventEntity>)

    @Query("DELETE FROM events")
    suspend fun clearAll()

    @Transaction
    suspend fun replaceAll(events: List<EventEntity>) {
        clearAll()
        insertAll(events)
    }

    @Query("SELECT COUNT(*) FROM events")
    suspend fun getCount(): Int
}
