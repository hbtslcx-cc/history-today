package com.historytoday.di

import android.content.Context
import androidx.room.Room
import com.historytoday.data.local.AppDatabase
import com.historytoday.data.local.EventDao
import com.historytoday.data.repository.CalendarRepositoryImpl
import com.historytoday.data.repository.EventRepositoryImpl
import com.historytoday.data.remote.DayInHistoryApiService
import com.historytoday.data.remote.EventRemoteDataSource
import com.historytoday.data.remote.EventRemoteDataSourceImpl
import com.historytoday.data.remote.TouTiaoApiService
import com.historytoday.domain.repository.CalendarRepository
import com.historytoday.domain.repository.EventRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DayInHistoryRetrofit

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TouTiaoRetrofit

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val DAY_IN_HISTORY_BASE_URL = "https://dayinhistory.dev/"
    private const val TOUTIAO_BASE_URL = "https://tmini.net/"
    private const val DATABASE_NAME = "history_today.db"

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    @Singleton
    fun provideEventDao(database: AppDatabase): EventDao {
        return database.eventDao()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @DayInHistoryRetrofit
    fun provideDayInHistoryRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(DAY_IN_HISTORY_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @TouTiaoRetrofit
    fun provideTouTiaoRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(TOUTIAO_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideDayInHistoryApiService(@DayInHistoryRetrofit retrofit: Retrofit): DayInHistoryApiService {
        return retrofit.create(DayInHistoryApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTouTiaoApiService(@TouTiaoRetrofit retrofit: Retrofit): TouTiaoApiService {
        return retrofit.create(TouTiaoApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        dayInHistoryApi: DayInHistoryApiService,
        touTiaoApi: TouTiaoApiService
    ): EventRemoteDataSource {
        return EventRemoteDataSourceImpl(dayInHistoryApi, touTiaoApi)
    }

    @Provides
    @Singleton
    fun provideCalendarRepository(
        eventDao: EventDao
    ): CalendarRepository {
        return CalendarRepositoryImpl(eventDao)
    }

    @Provides
    @Singleton
    fun provideEventRepository(
        eventDao: EventDao,
        remoteDataSource: EventRemoteDataSource
    ): EventRepository {
        return EventRepositoryImpl(eventDao, remoteDataSource)
    }
}

val MIGRATION_1_2 = object : androidx.room.migration.Migration(1, 2) {
    override fun migrate(database: androidx.sqlite.db.SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE events ADD COLUMN importance TEXT DEFAULT 'C'")
    }
}
