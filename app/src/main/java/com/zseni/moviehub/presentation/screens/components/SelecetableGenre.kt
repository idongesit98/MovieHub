package com.zseni.moviehub.presentation.screens.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
@Composable
fun SelectableGenre(
    modifier: Modifier = Modifier,
    genre:String,
    selected:Boolean,
    onClick:() -> Unit
) {
    val animateSelectable by animateColorAsState(
        targetValue = if(selected) Color(0xFFA0A1C2) else Color.LightGray,
        animationSpec = tween(
            durationMillis = if(selected) 100 else 50,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ),
        label = "Animating a button"
    )

    Box(
        modifier
            .padding(end = 4.dp)
            .clip(CircleShape)
            .background(
                color = Color.Gray
            )
            .height(32.dp)
            .widthIn(min = 80.dp)
            .padding(horizontal = 8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick.invoke()
            }
    ){
        Text(
            //check if one of the align can be removed
            text = genre,
            modifier = Modifier.align(Alignment.Center),
            fontWeight = if(selected) FontWeight.Normal else FontWeight.Light,
            textAlign = TextAlign.Center,
            color = if (selected) Color(0XFF180E36) else Color.White.copy(alpha = 0.80f)
        )
    }
}


@Preview
@Composable
private fun PreviewSelectable() {
    SelectableGenre(
        genre = "Genre",
        selected = false
    ){}
}