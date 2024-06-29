package com.zseni.moviehub.presentation.screens.movieDetails

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.zseni.moviehub.R
import com.zseni.moviehub.domain.model.Cast
import com.zseni.moviehub.domain.model.Movie
import com.zseni.moviehub.presentation.screens.searchScreen.components.genresProvider
import com.zseni.moviehub.presentation.viewModels.DetailsViewModel
import com.zseni.moviehub.ui.theme.abeezeeFont
import com.zseni.moviehub.ui.theme.akayaFont
import com.zseni.moviehub.ui.theme.backgroundColor
import com.zseni.moviehub.util.Constants
import com.zseni.moviehub.util.MovieType
import com.zseni.moviehub.util.RatingBar
import com.zseni.moviehub.util.getAverageColour
import java.util.Locale

@Composable
fun DetailScreen(
    selectedMovieType: MovieType,
    currentMovie: Movie
) {
    val detailsViewModel = hiltViewModel<DetailsViewModel>()
    val detailsState = detailsViewModel.detailsState.collectAsState().value
    val movieCast = detailsViewModel.movieCast.value
    val backDropImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(Constants.IMAGE_BASE_URL + detailsState.movie?.backdrop_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    val posterImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(Constants.IMAGE_BASE_URL + detailsState.movie?.poster_path)
            .size(Size.ORIGINAL)
            .build()
    ).state
    
    val movieType:MovieType = remember {
        selectedMovieType
    }
    val movie by remember {
        mutableStateOf(currentMovie)
    }
    
    LaunchedEffect(key1 = movie ) {
        detailsViewModel.getMovieCast(movieId = movie.id, movieType = movieType)
        Log.d("Detail Screen", "Cast List: ${detailsViewModel.getMovieCast(movieId = movie.id, movieType = movieType)}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        if (backDropImageState is AsyncImagePainter.State.Error) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(70.dp),
                    imageVector = Icons.Rounded.ImageNotSupported,
                    contentDescription = detailsState.movie?.title
                )
            }
        }
        if (backDropImageState is AsyncImagePainter.State.Success) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                painter = backDropImageState.painter,
                contentDescription = detailsState.movie?.title,
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(240.dp)
            ) {
                if (posterImageState is AsyncImagePainter.State.Error) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(70.dp),
                            imageVector = Icons.Rounded.ImageNotSupported,
                            contentDescription = detailsState.movie?.title
                        )
                    }
                }

                if (posterImageState is AsyncImagePainter.State.Success) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        painter = posterImageState.painter,
                        contentDescription = detailsState.movie?.title,
                        contentScale = ContentScale.Crop
                    )
                }
            }

            detailsState.movie?.let { movie ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = movie.name.ifEmpty { movie.title },
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = akayaFont
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        RatingBar(
                            starsModifier = Modifier.size(18.dp),
                            rating = movie.vote_average / 2
                        )
                        Text(
                            modifier = Modifier.padding(start = 4.dp),
                            text = movie.vote_average.toString().take(3),
                            color = Color.LightGray,
                            fontSize = 14.sp,
                            maxLines = 1,
                            fontFamily = akayaFont
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = stringResource(R.string.language) + ": " + movie.original_language.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        },
                        fontFamily = akayaFont
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = stringResource(R.string.release_date) + ":  " + movie.release_date,
                        fontFamily = akayaFont
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    val genres = genresProvider(
                        genre_ids = movie.genre_ids
                    )
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = genres,
                        fontFamily = akayaFont
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clip(shape = RoundedCornerShape(size = 5.dp))
                            .background(
                                if (movie.adult) Color.Red else Color.Blue.copy(alpha = 0.8f)
                            )
                            .padding(2.dp),
                        text = when(movie.adult){
                            true -> "18+"
                            false -> "PG"
                        },
                        fontFamily = akayaFont
                    )
                }
            }
        }
        LazyRow(
            horizontalArrangement = Arrangement.Center
        ) {
            movieCast.forEach { cast ->
                item { CastMembers(cast = cast) }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(R.string.overview),
            fontSize = 19.sp,
            fontFamily = abeezeeFont,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        detailsState.movie?.let {
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = it.overview,
                fontSize = 25.sp,
                fontFamily = akayaFont,
                textAlign = TextAlign.Left,
            )
        }
    }

}

@Composable
fun CastMembers(
    cast:Cast,
) {
    val imageUrl =
        "${Constants.POSTER_IMAGE_BASE_URL}${cast.profilePath}"
    val imagePainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .size(Size.ORIGINAL)
            .build()
    )

    val imageState = imagePainter.state
    val defaultDominantColour = MaterialTheme.colorScheme.primaryContainer
    var dominantColour by remember{
        mutableStateOf(defaultDominantColour)
    }
    Column(
        modifier = Modifier
            .padding(end = 8.dp, top = 2.dp, bottom = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier
            .size(70.dp)
        ){
            if (imageState is AsyncImagePainter.State.Success){
                val imageBitmap = imageState.result.drawable.toBitmap()
                dominantColour = getAverageColour(imageBitmap.asImageBitmap())
                Image(
                    bitmap = imageBitmap.asImageBitmap() ,
                    contentDescription = trimTitle(cast.name),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.background)
                        .border(3.dp, backgroundColor, CircleShape)
                )
            }

            if (imageState is AsyncImagePainter.State.Error){
                dominantColour = MaterialTheme.colorScheme.primary
                Icon(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.background)
                        .padding(8.dp)
                        .alpha(0.8f),
                    imageVector = Icons.Rounded.ImageNotSupported,
                    contentDescription = cast.name,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Text(
            text = cast.name,
            color = Color.Green,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = abeezeeFont,
            textAlign = TextAlign.Center
        )
    }
}

private fun trimTitle(text: String) = if (text.length <= 26) text else {
    val textWithEllipsis = text.removeRange(startIndex = 26, endIndex = text.length)
    "$textWithEllipsis..."
}
