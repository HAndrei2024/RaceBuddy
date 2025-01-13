package com.example.racebuddy.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.racebuddy.R
import com.example.racebuddy.ui.main.MainScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginTopAppBar(
    title: String = "RaceBuddy"
) {
    CenterAlignedTopAppBar(
        title = { Text(title) }
    )
}

@Composable
fun MainScreenTopAppBar(
    mainScreenViewModel: MainScreenViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by mainScreenViewModel.uiState.collectAsState()
    TextField(
        value = uiState.searchString,
        onValueChange = { mainScreenViewModel.onSearchValueChange(it) },
        placeholder = { Text("Search...") },
        singleLine = true,
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
        trailingIcon = {
            if (uiState.searchString.isNotEmpty()) {
                IconButton(onClick = { mainScreenViewModel.onSearchValueChange("") }) {
                    Icon(Icons.Filled.Clear, contentDescription = "Clear Text")
                }
            }
        },
        modifier = modifier
            .fillMaxWidth()
    )
}


@Composable
fun BottomAppBar(
    onHomeClick: () -> Unit = { },
    onFavoriteClick: () -> Unit = { },
    onProfileClick: () -> Unit = { }
) {
    BottomAppBar {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            BottomBarIcon(
                isSelected = true,
                selectedImage = R.drawable.baseline_home_24,
                unselectedImage = R.drawable.outline_home_24,
                onClick = onHomeClick
            )
            BottomBarIcon(
                isSelected = false,
                selectedImage = R.drawable.baseline_favorite_24,
                unselectedImage = R.drawable.baseline_favorite_border_24,
                onClick = onFavoriteClick
            )
            BottomBarIcon(
                isSelected = false,
                selectedImage = R.drawable.baseline_person_24,
                unselectedImage = R.drawable.outline_person_24,
                onClick = onProfileClick
            )
        }
    }
}

@Composable
fun BottomBarIcon(
    isSelected: Boolean,
    selectedImage: Int,
    unselectedImage: Int,
    contentDescription: String = "",
    onClick: () -> Unit = { }
) {
    val iconPainter = if(isSelected) {
        painterResource(selectedImage)
    }
    else {
        painterResource(unselectedImage)
    }

    IconButton(
        onClick = onClick
    ) {
        Icon(
            painter = iconPainter,
            contentDescription = contentDescription
        )
    }
}

@Preview
@Composable
fun BottomBarPreview() {
    BottomAppBar()
}