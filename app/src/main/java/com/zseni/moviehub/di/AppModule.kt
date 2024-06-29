package com.zseni.moviehub.di

import android.app.Application
import androidx.room.Room
import com.zseni.moviehub.data.local.movie.MovieDatabase
import com.zseni.moviehub.data.local.movie.WatchDatabase
import com.zseni.moviehub.data.remote.MovieApi
import com.zseni.moviehub.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val interceptor:HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    private val client:OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Singleton
    @Provides
    fun providesMoviesApi(): MovieApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
            .create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun providesDatabase(app:Application): MovieDatabase {
        return Room.databaseBuilder(
            app,
            MovieDatabase::class.java,
            "moviedb.db"
        ).build()
    }
    @Provides
    @Singleton
    fun providesWatchDatabase(app:Application): WatchDatabase {
        return Room.databaseBuilder(
            app,
            WatchDatabase::class.java,
            "watchdb.db"
        ).build()
    }

}