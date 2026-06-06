package com.historytoday.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DayInHistoryApiService {
    @GET("api/today")
    suspend fun getTodayEvents(
        @Query("month") month: Int,
        @Query("day") day: Int,
        @Query("type") type: String = "json"
    ): Response<DayInHistoryResponse>
    
    @GET("api/events")
    suspend fun getAllEvents(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 100
    ): Response<DayInHistoryResponse>
    
    @GET("api/search")
    suspend fun searchEvents(
        @Query("q") query: String
    ): Response<DayInHistoryResponse>
    
    @GET("api/event")
    suspend fun getEventById(
        @Query("id") id: String
    ): Response<DayInHistoryItem>
}

interface TouTiaoApiService {
    @GET("api/today")
    suspend fun getTodayEvents(): Response<TouTiaoResponse>
}

data class DayInHistoryResponse(
    val code: Int,
    val message: String,
    val total: Int,
    val events: List<DayInHistoryItem>
)

data class DayInHistoryItem(
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

data class TouTiaoResponse(
    val code: Int,
    val date: String,
    val events: List<TouTiaoEvent>
)

data class TouTiaoEvent(
    val title: String,
    val year: String,
    val desc: String,
    val link: String
)
