package com.zseni.moviehub.data.movie_series_repository

import com.zseni.moviehub.data.remote.CastDto
import com.zseni.moviehub.data.remote.GenreListDto
import com.zseni.moviehub.data.remote.MovieApi
import com.zseni.moviehub.domain.repository.GenreRepository
import com.zseni.moviehub.util.MovieType
import com.zseni.moviehub.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreRepoImpl @Inject constructor(
    private val apiService: MovieApi
): GenreRepository {
    override suspend fun getGenre(movieType: MovieType): Resource<GenreListDto> {
        val response = try{
            if (movieType == MovieType.Movie)
                apiService.getMovieGenres() else apiService.getTvShowGenres()
        }catch (e:Exception){
            return Resource.Error("Couldn't load genre data")
        }
        return Resource.Success(response)
    }

    override suspend fun getCast(movieType: MovieType, movieId: Int): Resource<CastDto> {
        val response = try{
            if (movieType == MovieType.Movie)
            apiService.getMovieCast(movieId) else
                apiService.getTvShowCast(movieId)
        }catch (e:Exception){
            return Resource.Error("Error when loading movie cast")
        }
        return Resource.Success(response)
    }
}



/**
private val genreDao = genreDb.genreDao
override suspend fun getGenre(
    forceFetchFromRemote: Boolean,
    category:String,
    movieType: MovieType
): Flow<Resource<List<Genre>>> {
    return flow {
        emit(Resource.Loading(true))
        val genreEntity = genreDao.getGenresList(category)
        if (genreEntity.isNotEmpty() && !forceFetchFromRemote){
            emit(Resource.Success(
                genreEntity.map { genreEntity ->
                    Genre(
                        id = genreEntity.id,
                        name = genreEntity.name,
                    )
                }
            ))
            emit(Resource.Loading(false))
            return@flow
        }
        val remoteGenreList = try {
            if (movieType == MovieType.Movie)
                apiService.getGenres(category).genres else apiService.getTvShowGenres().genres
        }catch (e:IOException){
            e.printStackTrace()
            emit(Resource.Error("Couldn't load genre data"))
            emit(Resource.Loading(false))
            return@flow
        }catch (e:HttpException){
            e.printStackTrace()
            emit(Resource.Error(""))
            emit(Resource.Loading(false))
            return@flow
        }
        remoteGenreList.let {
            genreDao.insertGenres(remoteGenreList.map { remoteGenre->
                GenreEntity(
                    id = remoteGenre.id,
                    name = remoteGenre.name,
                    category = category
                )
            })
            emit(Resource.Success(remoteGenreList))
            emit(Resource.Loading(false))

        }

    }
}**/