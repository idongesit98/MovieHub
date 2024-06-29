package com.zseni.moviehub.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material.icons.twotone.ArrowCircleDown
import androidx.compose.material.icons.twotone.ArrowCircleUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.zseni.moviehub.domain.model.Movie
import com.zseni.moviehub.util.Constants
import com.zseni.moviehub.util.RatingBar
import com.zseni.moviehub.R
import com.zseni.moviehub.presentation.screens.Screen
import com.zseni.moviehub.presentation.screens.searchScreen.components.genresProvider
import com.zseni.moviehub.ui.theme.akayaFont
import com.zseni.moviehub.util.ReverseLottie
import com.zseni.moviehub.util.getAverageColour


@Composable
fun MovieItem(
    movieList:List<Movie>,
    clickable:()-> Unit,
    navController: NavController
) {
    //Log.d("MovieItem", "Number of movies: ${movieList.size}")
    LazyRow {
        items(movieList.size){index ->
            val paddingEnd =
            if (index == movieList.size -1) 16.dp else 0.dp
            ScrollableMovieItem(
                movie = movieList[index],
                modifier = Modifier
                    .height(200.dp)
                    .width(150.dp)
                    .padding(start = 16.dp, end = paddingEnd),
                navController = navController
            )
        }
    }
}


@Composable
fun ScrollableMovieItem(
    movie: Movie,
    modifier: Modifier,
    navController: NavController
) {

    val imageUrl =
        "${Constants.IMAGE_BASE_URL}${movie.backdrop_path}"
    //Log.d("ImageURL", "Loading image from URL: $imageUrl")
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
    Box(
        modifier = Modifier
            .width(180.dp)
            .wrapContentHeight()
            .padding(bottom = 16.dp, start = 8.dp, end = 8.dp)
    ){
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.secondaryContainer,
                            dominantColour
                        )
                    )
                )
                .clickable {
                    navController.navigate(
                        Screen.Details.route + "/${movie.id}"
                    )
                },
            horizontalAlignment = Alignment.Start

        ) {
            Box(modifier = Modifier
                .height(200.dp)
                .width(200.dp)
                .padding(6.dp)
            ){
                if (imageState is AsyncImagePainter.State.Success){
                    val imageBitmap = imageState.result.drawable.toBitmap()
                    dominantColour = getAverageColour(imageBitmap.asImageBitmap())
                    Image(
                        bitmap = imageBitmap.asImageBitmap() ,
                        contentDescription = movie.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.background)
                    )
                }
                if (imageState is AsyncImagePainter.State.Loading){
                   ReverseLottie(lottieFile = R.raw.loading)
                }
                if (imageState is AsyncImagePainter.State.Error){
                    dominantColour = MaterialTheme.colorScheme.primary
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.background)
                            .padding(32.dp)
                            .alpha(0.8f),
                        imageVector = Icons.Rounded.ImageNotSupported,
                        contentDescription = movie.title,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            var badgeCount by remember { mutableIntStateOf(0) }
            Text(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                text = trimTitle(movie.title.ifEmpty { movie.name }),
               fontFamily = akayaFont,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Start,
                maxLines = 1,
                color = Color.White,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { textLayoutResult ->
                    if (textLayoutResult.hasVisualOverflow) {
                        val lineEndIndex = textLayoutResult.getLineEnd(
                            lineIndex = 0,
                            visibleEnd = true
                        )
                        badgeCount = movie.title.ifEmpty { movie.name }.take(lineEndIndex)
                            .count { it == '.' }
                    }
                }
            )
            val genres = genresProvider(
                genre_ids = movie.genre_ids
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 12.dp),
                text = genres,
               fontFamily = akayaFont,
                fontSize = 14.sp,
                maxLines = 1,
                color = Color.LightGray,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = {textLayoutResult ->
                    if (textLayoutResult.hasVisualOverflow){
                        val lineEndIndex = textLayoutResult.getLineEnd(
                            lineIndex = 0,
                            visibleEnd = true
                        )
                        badgeCount = genres.substring(lineEndIndex)
                            .count { it=='.' }
                    }
                }
            )
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, start = 11.dp, end = 16.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically
                ) {
                    RatingBar(
                        starsModifier = Modifier
                            .size(18.dp),
                        rating = movie.vote_average/2
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        text = movie.vote_average.toString().take(3),
                        fontSize = 14.sp,
                        maxLines = 1,
                        color = Color.LightGray)

                }
            }

        }
    }
}

private fun trimTitle(text: String) = if (text.length <= 26) text else {
    val textWithEllipsis = text.removeRange(startIndex = 26, endIndex = text.length)
    "$textWithEllipsis..."
}

@Composable
fun ShowAboutCategory(
    name:String,description:String
){
    var showAboutCategory by remember {
        mutableStateOf(false)
    }
    Row(verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            fontSize = 24.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(
                    start = 4.dp, top = 14.dp,
                    end = 8.dp, bottom = 4.dp
                )
        )
        IconButton(
            onClick = {showAboutCategory = showAboutCategory.not() }
        ) {
            Icon(
                imageVector = if(showAboutCategory) Icons.TwoTone.ArrowCircleUp else Icons.TwoTone.ArrowCircleDown, 
                contentDescription = "Info icon",
                tint = Color.LightGray
            )
        }
    }
    AnimatedVisibility(visible = showAboutCategory) {
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .border(
                    width = 1.dp, color = Color.Green,
                    shape = RoundedCornerShape(5.dp)
                ),
            contentAlignment = Alignment.Center,
            content = {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = description,
                        modifier = Modifier.padding(vertical = 5.dp),
                        color = Color.Black
                    )
                }
            }
        )
        
    }
}
