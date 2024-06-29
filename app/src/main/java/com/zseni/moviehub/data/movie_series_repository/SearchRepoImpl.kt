package com.zseni.filmapp.data.repository

import com.zseni.moviehub.data.local.movie.MovieDatabase
import com.zseni.moviehub.data.mapper.toMovie
import com.zseni.moviehub.data.mapper.toMovieEntity
import com.zseni.moviehub.data.remote.MovieApi
import com.zseni.moviehub.domain.model.Movie
import com.zseni.moviehub.domain.repository.SearchRepository
import com.zseni.moviehub.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class SearchRepoImpl @Inject constructor(
    private val apiService: MovieApi,
    private val movieDatabase: MovieDatabase
): SearchRepository {
    override suspend fun getSearchList(
        forceFetchedFromRemote: Boolean,
        query: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))

            val localSearch = movieDatabase.movieDao.searchMovies(category = "")
            val loadLocalSearch = localSearch.isNotEmpty() && !forceFetchedFromRemote
            if (loadLocalSearch){
                emit(Resource.Success(
                    data = localSearch.map { movieEntity ->
                        movieEntity.toMovie(
                            category = ""
                        )
                    }
                ))
                emit(Resource.Loading(false))
                return@flow
            }
            val remoteSearchList = try {
                apiService.searchMovies(query, page)
            }catch (e:IOException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                return@flow
            }catch (e:HttpException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                return@flow
            }catch (e:Exception){
                e.printStackTrace()
                emit(Resource.Error(message = "Couldn't load search results"))
                return@flow
            }

            val searchEntities = remoteSearchList.results.let {
                it.map { movieDto ->
                    movieDto.toMovieEntity(
                        category = "",

                    )
                }
            }
            movieDatabase.movieDao.upsertMovieList(searchEntities)
            emit(Resource.Success(
                searchEntities.map { it.toMovie(
                    category = ""
                ) }
            ))
            emit(Resource.Loading(false))
        }
    }
}