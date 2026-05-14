package com.historytoday.di

import android.content.Context
import com.historytoday.data.remote.DayInHistoryApiService
import com.historytoday.data.remote.EventRemoteDataSource
import com.historytoday.data.remote.EventRemoteDataSourceImpl
import com.historytoday.data.remote.TouTiaoApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val DAY_IN_HISTORY_BASE_URL = "https://dayinhistory.dev/"
    private const val TOUTIAO_BASE_URL = "https://tmini.net/"

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
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
}

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DayInHistoryRetrofit

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TouTiaoRetrofit
