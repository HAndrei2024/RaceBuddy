package com.example.racebuddy.ui.v2.favorite

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.racebuddy.R
import com.example.racebuddy.data.database.EventInfo
import com.example.racebuddy.ui.theme.paddings
import com.example.racebuddy.ui.v2.common.BottomNavigationBarChat
import com.example.racebuddy.ui.v2.common.EventCardUpdated
import com.example.racebuddy.ui.v2.common.MainScreenTopAppBar
import com.example.racebuddy.ui.v2.main.countryMap
import kotlinx.coroutines.launch

@Composable
fun FavoriteScreen(
    favoriteEvents: List<EventInfo>,
    onFavoriteIconClick: (String, Boolean) -> Unit,
    isUserLoggedIn: Boolean,
    onHomeIconClick: () -> Unit,
    onProfileIconClick: () -> Unit,
    onBottomBarIconClick: (Int) -> Unit,
) {

    Scaffold(
        topBar = {
            MainScreenTopAppBar(
                onSettingsIconClick = {},
                onSearchIconClick = {},
                showSearchIcon = false
            )

        },
        bottomBar = {
            //BottomAppBarUpdated()
            BottomNavigationBarChat(
                selectedItem = 1,
                onItemSelected = { int: Int -> onBottomBarIconClick(int) },
            )
        },
        containerColor = Color.White,
        contentColor = Color.Black
    ) { innerPadding ->
        Text(
            text = "Favorite Events",
            style = MaterialTheme.typography.labelLarge,
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                //.background(Color(0xFFEEEEEE))
                .padding(
                    start = paddings.spacingMedium,
                    //bottom = paddings.spacingSmall
                    //top = paddings.spacingXSmall
                )
                .fillMaxWidth(1f)

//                .drawBehind {
//                    val borderSize = 2.dp.toPx()
//                    drawLine(
//                        color = Color(0xFFEEEEEE),
//                        start = Offset(-paddings.spacingMedium.toPx(), size.height),
//                        end = Offset(size.width, size.height),
//                        strokeWidth = borderSize
//                    )
//                }
            //.offset(y = paddings.spacingXSmall)
        )

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
        ) {

            items(favoriteEvents) { eventInfo ->
                EventCardUpdated(
                    eventInfo = eventInfo,
                    isUserLoggedIn = isUserLoggedIn,
                    countryCodeEmoji = countryMap[eventInfo.city] ?: "",
                    favoriteIcon =  painterResource(R.drawable.baseline_favorite_24),
                    onFavoriteIconClick = {
                        //TODO: Pass the event id, and update the database and local favorite list
                        Log.d("MainScreen UI", "onFavoriteClickFromUi -> ${eventInfo.evenUuid}, false")
                        onFavoriteIconClick(
                            eventInfo.evenUuid,
                            true
                        )
                    },
                    onEventClick = {},
                    modifier = Modifier
                )
            }
        }
    }
}

@Preview
@Composable
fun FavoriteScreenPreview() {

}