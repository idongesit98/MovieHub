package com.zseni.moviehub.di

import com.zseni.moviehub.data.movie_series_repository.TrendingRepoImpl
import com.zseni.filmapp.data.repository.SearchRepoImpl
import com.zseni.moviehub.data.local.movie.WatchDatabase
import com.zseni.moviehub.data.movie_series_repository.GenreRepoImpl
import com.zseni.moviehub.data.movie_series_repository.NowPlayingRepoImpl
import com.zseni.moviehub.data.movie_series_repository.PopularRepoImpl
import com.zseni.moviehub.data.movie_series_repository.RecommendedRepoImpl
import com.zseni.moviehub.data.movie_series_repository.SimilarRepoImpl
import com.zseni.moviehub.data.movie_series_repository.TopRatedRepoImpl
import com.zseni.moviehub.data.movie_series_repository.UpcomingRepoImpl
import com.zseni.moviehub.data.movie_series_repository.WatchMovieRepo
import com.zseni.moviehub.domain.repository.GenreRepository
import com.zseni.moviehub.domain.repository.MovieRepository
import com.zseni.moviehub.domain.repository.NowPlayingRepo
import com.zseni.moviehub.domain.repository.PopularRepository
import com.zseni.moviehub.domain.repository.RecomRepository
import com.zseni.moviehub.domain.repository.SearchRepository
import com.zseni.moviehub.domain.repository.SimilarRepos
import com.zseni.moviehub.domain.repository.TopRatedRepo
import com.zseni.moviehub.domain.repository.UpcomingRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
//
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindTrendingMoviesRepository(
        trendingRepoImpl: TrendingRepoImpl
    ): MovieRepository

    @Binds
    @Singleton
    abstract fun bindPopularMoviesRepository(
        popularRepoImpl: PopularRepoImpl
    ): PopularRepository

    @Binds
    @Singleton
    abstract fun bindRecommendedMoviesRepository(
        recommendedRepoImpl: RecommendedRepoImpl
    ): RecomRepository

    @Binds
    @Singleton
    abstract fun bindSimilarMoviesRepository(
        similarRepoImpl: SimilarRepoImpl
    ): SimilarRepos

    @Binds
    @Singleton
    abstract fun bindTopRatedMoviesRepository(
        topRatedRepoImpl: TopRatedRepoImpl
    ): TopRatedRepo

    @Binds
    @Singleton
    abstract fun bindUpcomingRepository(
        upcomingRepoImpl: UpcomingRepoImpl
    ): UpcomingRepository

    @Binds
    @Singleton
    abstract fun bindNowPlayingRepository(
        nowPlayingRepoImpl: NowPlayingRepoImpl
    ): NowPlayingRepo

    companion object {
        @Singleton
        @Provides
        fun providesWatchMovieRepository(watchListDatabase: WatchDatabase) =
            WatchMovieRepo(database = watchListDatabase)
    }

    @Binds
    @Singleton
    abstract fun bindGenreRepository(
        genreRepoImpl: GenreRepoImpl
    ): GenreRepository

    @Binds
    @Singleton
    abstract fun bindSearchMovieRepository(
        searchRepoImpl: SearchRepoImpl
    ): SearchRepository
}