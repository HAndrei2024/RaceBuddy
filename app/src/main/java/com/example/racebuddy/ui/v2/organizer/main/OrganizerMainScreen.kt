package com.example.racebuddy.ui.v2.organizer.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.racebuddy.data.database.OrganizerInfo
import com.example.racebuddy.data.database.defaultOrganizer
import com.example.racebuddy.models.Organizer
import com.example.racebuddy.ui.v2.common.MainScreenTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganizerMainScreen(
    organizer: OrganizerInfo,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            MainScreenTopAppBar(
                onSearchIconClick = {},
                onSettingsIconClick = {},
                showSearchIcon = false
            )
        },
        bottomBar = {}
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = modifier.padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = "This is organizer screen!"
                )
            }
        }
    }
}

@Preview
@Composable
fun OrganizerMainScreenPreview() {
    OrganizerMainScreen(
        organizer = defaultOrganizer,
        isRefreshing = false,
        onRefresh = {  },
        modifier = Modifier
    )
}