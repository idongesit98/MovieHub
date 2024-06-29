package com.zseni.moviehub.util

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.zseni.moviehub.R
import kotlin.math.ceil
import kotlin.math.floor


@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    starsModifier: Modifier = Modifier,
    rating:Double = 0.0,
    stars:Int = 5,
    starsColour:Color = Color.Yellow
){
    val filledStars =  floor(rating).toInt()
    val unfilledStars = (stars - ceil(rating).toInt())
    val halfStar = !(rating.rem(1).equals(0.0))

    Row(modifier = modifier) {
        repeat(filledStars){
            Icon(
                modifier = starsModifier,
                imageVector = Icons.Rounded.Star,
                contentDescription = stringResource(id = R.string.rating_star),
                tint = starsColour
            )
        }

        if (halfStar){
          Icon(
              modifier = starsModifier,
              painter = painterResource(id = R.drawable.star_half),
              contentDescription = stringResource(id = R.string.rating_star),
              tint = starsColour
          )
        }

        repeat(unfilledStars){
            Icon(
                modifier = starsModifier,
                imageVector =Icons.Rounded.Star ,
                contentDescription = stringResource(id = R.string.rating_star),
                tint = starsColour
            )
        }
    }
}