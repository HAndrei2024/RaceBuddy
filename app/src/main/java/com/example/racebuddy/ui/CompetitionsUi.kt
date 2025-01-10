package com.example.racebuddy.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MainPage() {
    Scaffold(
        topBar = { AppTopBar() },
        bottomBar = { AppBottomBar() },
        modifier = Modifier.fillMaxSize()
    ) {
        innerPadding -> EventList(Modifier.padding(innerPadding))
    }
}

@Composable
fun AppTopBar() {
    Column() {
        CountriesDropDownList()
        CategoryDropDownList()
        SeasonDropDownList()
    }
}

@Composable
fun CountriesDropDownList() {

}

@Composable
fun CategoryDropDownList() {

}

@Composable
fun SeasonDropDownList() {

}

@Composable
fun AppBottomBar() {

}

@Composable
fun EventList(modifier: Modifier = Modifier) {

}

@Composable
@Preview
fun previewMainPage() {
    MainPage()
}
