package com.zseni.moviehub.data.movie_series_repository

import android.util.Log
import com.zseni.moviehub.data.local.movie.MovieDatabase
import com.zseni.moviehub.data.mapper.toMediaEntity
import com.zseni.moviehub.data.mapper.toMovie
import com.zseni.moviehub.data.mapper.toMovieEntity
import com.zseni.moviehub.data.remote.MovieApi
import com.zseni.moviehub.domain.model.Movie
import com.zseni.moviehub.domain.repository.MovieRepository
import com.zseni.moviehub.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class TrendingRepoImpl @Inject constructor(
    private val apiService: MovieApi,
    private val movieDatabase: MovieDatabase
): MovieRepository {
    override suspend fun getTrending(
        forceFetchFromRemote: Boolean,
        time: String,
        isRefresh: Boolean,
        page: Int,
    ): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))

            val localMovieList = movieDatabase.movieDao.getMoviesByCategory("trending")

            val loadLocalMovie = localMovieList.isNotEmpty() && !forceFetchFromRemote

            if (loadLocalMovie) {
                emit(Resource.Success(
                    data = localMovieList.map { movieEntity ->
                        movieEntity.toMovie(
                            category = "trending"
                        )
                    }
                ))
                emit(Resource.Loading(false))
                return@flow
            }

            if (isRefresh) {
                movieDatabase.movieDao.deleteMovies(time)
            }

            val movieListFromRemote = try {
                //bring an if else statement here if movietype is movie else tvseries //movietvlist
                apiService.getTrendingMovies(time, page)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error leading movies"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error leading movies"))
                return@flow
            }
            val movieEntities = movieListFromRemote.results.let {
                it.map { movieDto ->
                    movieDto.toMovieEntity(category = "trending")
                }
            }
            movieDatabase.movieDao.upsertMovieList(movieEntities)
            emit(Resource.Success(
                movieEntities.map { it.toMovie(category = "trending") }
            ))
            emit(Resource.Loading(false))
            Log.d("TrendingRepoImpl", "Fetched ${movieEntities.size} movies from remote")
//            movieListFromRemote.let { movieList ->
//                val movie = movieList.map {
//                    it.toMovie(
//                        category = "day"
//                    )
//                }
//
//                val movieEntities = movieList.map {
//                    it.toMovieEntity(
//                        category = "day"
//                    )
//                }
//
//
//            }
        }
    }

    override suspend fun getTrendingSeries(
        forceFetchFromRemote: Boolean,
        time: String,
        isRefresh: Boolean,
        page: Int,
    ): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))

            val localMovieList = movieDatabase.movieDao.getTvSeriesByCategory("trending")

            val loadLocalMovie = localMovieList.isNotEmpty() && !forceFetchFromRemote

            if (loadLocalMovie) {
                emit(Resource.Success(
                    data = localMovieList.map { movieEntity ->
                        movieEntity.toMovie(
                            category = "trending"
                        )
                    }
                ))
                emit(Resource.Loading(false))
                return@flow
            }

            if (isRefresh) {
                movieDatabase.movieDao.deleteMovies(time)
            }

            val movieListFromRemote = try {
                apiService.getTrendingTvSeries(time, page).results
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error leading movies"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error leading movies"))
                return@flow
            }
            movieListFromRemote.let { movieList ->
                val movie = movieList.map {
                    it.toMovie(
                        category = "day"
                    )
                }

                val movieEntities = movieList.map {
                    it.toMovieEntity(
                        category = "day"
                    )
                }
                Log.d("TrendingRepoImpl", "Fetched ${movieEntities.size} movies from remote")
                movieDatabase.movieDao.upsertMovieList(movieEntities)

                emit(Resource.Success(data = movie))
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getMovie(
        id: Int): Flow<Resource<Movie>> {
        return flow{
            emit(Resource.Loading(true))

            val movieEntity = movieDatabase.movieDao.getMovieById(id)
            if (movieEntity != null){
                emit(
                    Resource.Success(movieEntity.toMovie(
                        category = movieEntity.category
                    ))
                )
                emit(Resource.Loading(false))
                return@flow
            }
            emit(Resource.Error("Error no such movie"))
            emit(Resource.Loading(false))

        }
    }

    override suspend fun getTv(
        id: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(true))
            val tvEntity = movieDatabase.movieDao.getTvById(id)

            if (tvEntity != null){
                emit(Resource.Success(
                    tvEntity.toMovie(
                        tvEntity.category
                    )))
                emit(Resource.Loading(false))
                return@flow
            }
            emit(Resource.Error("No error"))
            emit(Resource.Loading(false))

        }

    }

    override suspend fun insertMovie(movie: Movie) {
        movieDatabase.movieDao.insertMovie(
            movieItem = movie.toMediaEntity()
        )
    }

    override suspend fun updateMovie(movie: Movie) {
        movieDatabase.movieDao.upsertMovieList(
            movieList = listOf(movie.toMediaEntity())
        )
    }

}