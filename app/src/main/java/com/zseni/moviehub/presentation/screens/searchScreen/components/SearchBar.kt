package com.zseni.moviehub.presentation.screens.searchScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.zseni.moviehub.R

@Composable
fun SearchScreenBar(
    toolbarOffsetHeightPx:Int,
    searchState: SearchState,
    onSearch:(String) -> Unit
){
    Box(modifier = Modifier
        .background(Color.Transparent)
        .padding(horizontal = 16.dp, vertical = 12.dp)
        .height(74.dp)
        .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx) }
    ) {
        SearchBar(
            leadingIcon = {
                Icon(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    imageVector = Icons.TwoTone.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground)
            },
            placeholderText = stringResource(id = R.string.search_icon),
            searchState =searchState,
        ){
            onSearch(it)
        }
    }

}
@Composable
fun SearchBar(
    modifier:Modifier = Modifier,
    leadingIcon:(@Composable () -> Unit) = {},
    placeholderText:String,
    searchState: SearchState,
    onSearch:(String) -> Unit
){
    var text by rememberSaveable {
        mutableStateOf(searchState.searchQuery) }

    val focusRequester = remember{FocusRequester()}
    LaunchedEffect(focusRequester){
        focusRequester.requestFocus()
    }

    val customTextSelectionColors = TextSelectionColors(
        handleColor = MaterialTheme.colorScheme.primary,
        backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
    )
    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        BasicTextField(
            modifier = modifier
                .clip(RoundedCornerShape(percent = 50))
                .background(
                    MaterialTheme.colorScheme.secondaryContainer,
                    MaterialTheme.shapes.small
                )
                .height(60.dp)
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            singleLine = true,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                //fontFamily = adaminaFont,
                fontSize = 16.sp
            ),
            decorationBox = { innerTextField ->
                Row(modifier,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    leadingIcon()
                    Box(Modifier.weight(1f)){
                        if (text.isEmpty()) Text(
                            text = placeholderText,
                            style = LocalTextStyle.current.copy(
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                                //fontFamily = adaminaFont,
                                fontSize = 16.sp
                            )
                        )
                        innerTextField()
                    }
                    if (text.isNotEmpty()){
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .size(28.dp)
                                .clickable {
                                    text = ""
                                    onSearch("")
                                }
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun NonSearchScreenBar(
    toolbarOffsetHeightPx: Int,
    navController: NavController
){
    Box(modifier = Modifier
        .wrapContentSize()
        .background(Color.Transparent)
        .padding(horizontal = 16.dp, vertical = 12.dp)
        .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx) }
    ){
        NonFocusedSearchBar(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 8.dp),
            placeholderText = stringResource(id = R.string.placeholder),
            navController = navController
            )
    }
}


@Composable
fun NonFocusedSearchBar(
    modifier: Modifier  = Modifier,
    placeholderText: String,
    navController:NavController
){
    Row(modifier = Modifier
        .clip(RoundedCornerShape(percent = 50))
        .background(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.shapes.small
        )
        .height(60.dp)
        .fillMaxWidth(),
        //.clickable { navController.navigate(Screen.SearchScreen.route) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .padding(start = 16.dp)
                .size(20.dp)
                .alpha(0.3f),
            imageVector = Icons.Rounded.Search,
            contentDescription = stringResource(id = R.string.search_icon),
            tint = MaterialTheme.colorScheme.onBackground
        )
        Text(
            modifier = Modifier
                .padding(start = 8.dp),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
            //fontFamily = adaminaFont,
            fontSize = 16.sp,
            text = placeholderText)

    }

}

@Preview
@Composable
fun PreviewFocusedSearch(){
    SearchScreenBar(searchState = SearchState(), onSearch = {}, toolbarOffsetHeightPx = 0)
}
@Preview
@Composable
fun PreviewSearchBar(){
  SearchBar(
        leadingIcon = { /*TODO*/ },
        placeholderText = "",
        searchState = SearchState(),
        onSearch = {}
    )
}

@Preview
@Composable
fun PreviewNonSearchScreenBar(){
    val navController = rememberNavController()
    NonSearchScreenBar(
        toolbarOffsetHeightPx = 0,
        navController = navController
    )
}

@Preview
@Composable
fun PreviewNonFocusSearch(){
    val navController = rememberNavController()
    NonFocusedSearchBar(
        placeholderText = "",
        navController = navController
    )
}