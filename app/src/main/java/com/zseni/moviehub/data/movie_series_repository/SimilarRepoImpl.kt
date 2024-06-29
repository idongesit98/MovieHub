package com.zseni.moviehub.data.movie_series_repository

import com.zseni.moviehub.data.local.movie.MovieDatabase
import com.zseni.moviehub.data.mapper.toMovie
import com.zseni.moviehub.data.mapper.toMovieEntity
import com.zseni.moviehub.data.remote.MovieApi
import com.zseni.moviehub.domain.model.Movie
import com.zseni.moviehub.domain.repository.SimilarRepos
import com.zseni.moviehub.util.MovieType
import com.zseni.moviehub.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SimilarRepoImpl @Inject constructor(
    private val apiService: MovieApi,
    private val movieDatabase: MovieDatabase
): SimilarRepos {
    override suspend fun getSimilar(
        forceFetchFromRemote: Boolean,
        category: String,
        isRefresh: Boolean,
        page: Int,
        movieType: MovieType
    ): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))

            val localMovieList = movieDatabase.movieDao.getMoviesByCategory(category)

            val loadLocalMovie = localMovieList.isNotEmpty() && !forceFetchFromRemote && !isRefresh

            if (loadLocalMovie) {
                emit(Resource.Success(
                    data = localMovieList.map { movieEntity ->
                        movieEntity.toMovie(
                            category = category
                        )
                    }
                ))
                emit(Resource.Loading(false))
                return@flow
            }

            if (isRefresh) {
                movieDatabase.movieDao.deleteMovies(category)
            }

            val movieListFromRemote = try {
                //bring an if else statement here if movietype is movie else tvseries //movietvlist
                if (movieType == MovieType.Movie)
                    apiService.getSimilarMovies(category, page)
                else apiService.getSimilarTvShows(category, page)
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
                    movieDto.toMovieEntity(
                        category = category
                    )
                }
            }
            movieDatabase.movieDao.upsertMovieList(movieEntities)

            emit(Resource.Success(
                movieEntities.map {
                    it.toMovie(
                        category = category
                    )
                }
            ))
            emit(Resource.Loading(false))
        }
    }

}