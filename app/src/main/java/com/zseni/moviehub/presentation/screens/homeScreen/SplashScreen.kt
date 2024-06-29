package com.zseni.moviehub.presentation.screens.homeScreen

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.zseni.moviehub.R
import com.zseni.moviehub.presentation.screens.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(1000),RepeatMode.Reverse),
        label = "scale of text"
    )
    val animatedColor by infiniteTransition.animateColor(
        initialValue = Color(0xFFEFB8C8),
        targetValue = Color(0xFF7D5260),
        animationSpec = infiniteRepeatable(tween(1000),RepeatMode.Restart),
        label = "colour animation"
    )
    LaunchedEffect(key1 = Unit) {
        delay(4000L)
        navController.navigate(Screen.Home.route)
    }
   Box(
       contentAlignment = Alignment.Center,
       content = {
           val styledText = buildAnnotatedString {
               withStyle(
                   style = SpanStyle(
                       fontSize = 80.sp,
                       brush = Brush.horizontalGradient(
                           listOf(
                               Color.Green,
                               Color.Red, Color.Magenta, Color.Gray,
                               Color.Green
                           )
                       ),
                       fontFamily = FontFamily.Cursive,
                       fontWeight = FontWeight.Bold
                   )
               ) {
                   append(stringResource(R.string.app_name))
               }
           }
           BasicText(
               modifier = Modifier
                   .graphicsLayer {
                       scaleX = scale
                       scaleY = scale
                       transformOrigin = TransformOrigin.Center
                   }
                   .align(Alignment.Center),
               color = {animatedColor},
               text = styledText,
               style = LocalTextStyle.current.copy(textMotion = TextMotion.Animated)
           )
       })
}

@Preview(backgroundColor = 0xFFEFB8C8)
@Composable
private fun PreviewSplash() {
    val navController = rememberNavController()
    SplashScreen(navController = navController)
    
}