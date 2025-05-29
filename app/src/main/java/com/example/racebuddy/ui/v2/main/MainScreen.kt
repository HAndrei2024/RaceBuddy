package com.example.racebuddy.ui.v2.main

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.racebuddy.R
import com.example.racebuddy.data.database.AthleteInfo
import com.example.racebuddy.data.database.EventInfo
import com.example.racebuddy.data.database.cyclingEvents
import com.example.racebuddy.data.database.testAthlete
import com.example.racebuddy.ui.theme.paddings
import com.example.racebuddy.ui.theme.shapes
import com.example.racebuddy.ui.v2.common.BottomNavigationBarChat
import com.example.racebuddy.ui.v2.common.EventCardUpdated
import com.example.racebuddy.ui.v2.common.MainScreenTopAppBar
import com.example.racebuddy.ui.v2.signup.Country

val countries = listOf(
    Country("Argentina", "🇦🇷"),
    Country("Australia", "🇦🇺"),
    Country("Austria", "🇦🇹"),
    Country("Belgium", "🇧🇪"),
    Country("Brazil", "🇧🇷"),
    Country("Canada", "🇨🇦"),
    Country("China", "🇨🇳"),
    Country("Czech Republic", "🇨🇿"),
    Country("Denmark", "🇩🇰"),
    Country("Egypt", "🇪🇬"),
    Country("Finland", "🇫🇮"),
    Country("France", "🇫🇷"),
    Country("Germany", "🇩🇪"),
    Country("Greece", "🇬🇷"),
    Country("India", "🇮🇳"),
    Country("Ireland", "🇮🇪"),
    Country("Israel", "🇮🇱"),
    Country("Italy", "🇮🇹"),
    Country("Japan", "🇯🇵"),
    Country("Mexico", "🇲🇽"),
    Country("Netherlands", "🇳🇱"),
    Country("New Zealand", "🇳🇿"),
    Country("Nigeria", "🇳🇬"),
    Country("Norway", "🇳🇴"),
    Country("Poland", "🇵🇱"),
    Country("Portugal", "🇵🇹"),
    Country("Romania", "🇷🇴"),
    Country("Russia", "🇷🇺"),
    Country("Saudi Arabia", "🇸🇦"),
    Country("South Africa", "🇿🇦"),
    Country("South Korea", "🇰🇷"),
    Country("Spain", "🇪🇸"),
    Country("Sweden", "🇸🇪"),
    Country("Switzerland", "🇨🇭"),
    Country("Turkey", "🇹🇷"),
    Country("UK", "🇬🇧"),
    Country("Ukraine", "🇺🇦"),
    Country("USA", "🇺🇸")
)

val countryMap = countries.associate { it.name to it.flag }

@Composable
fun MainScreen(
    athleteInfo: AthleteInfo,
    events: List<EventInfo>,
    onFilterButtonClick: (String) -> Unit,
    onFavoriteIconClick: (String, Boolean) -> Unit,
    favoriteEventsId: List<String> = emptyList(),
    modifier: Modifier = Modifier
) {

    Scaffold(
        topBar = {
//            TopBarWithCategoryAndSearchChat(
//                categories = listOf("XC", "Downhill", "Road", "All"),
//                selectedCategory = "All",
//                onCategorySelected = {},
//                onSearchQueryChanged = {}
//            )
            MainScreenTopAppBar()
//            Row(
//                horizontalArrangement = Arrangement.SpaceBetween,
//                modifier = Modifier
//                    .padding(start = paddings.spacingMedium, top = paddings.spacingSmall, end = paddings.spacingSmall, bottom = paddings.spacingSmall)
//                    .fillMaxWidth()
//            ) {
//                Text(
//                    text = countryMap["Romania"] ?: ""
//                )
//                Text(
//                    text = "Some text",
//                    style = MaterialTheme.typography.labelMedium
//                )
//            }
        },
        bottomBar = {
            //BottomAppBarUpdated()
            BottomNavigationBarChat(
                selectedItem = 0,
                onItemSelected = {}
            )
        },
        containerColor = Color.White,
        contentColor = Color.Black
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            HelloText(
                athleteFirstName = athleteInfo.firstName ?: "",
                imageUrl = athleteInfo.profilePictureUrl ?: "",
            )

            Spacer(
                modifier = Modifier
                    .padding(paddings.spacingSmall)
            )


            FilterButtons(
                onFilterButtonClick = onFilterButtonClick
            )
            Spacer(
                modifier = Modifier
                    .padding(paddings.spacingSmall)
            )

            Events(
                events = events,
                favoriteEventsId = favoriteEventsId,
                onFavoriteIconClick = onFavoriteIconClick
            )
        }
    }

}

@Composable
fun HelloText(
    athleteFirstName: String,
    imageUrl: String,
    modifier: Modifier = Modifier
) {
//    Spacer(
//        modifier = Modifier
//            .padding(paddings.spacingSmall)
//    )

    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        shape = RoundedCornerShape(0.dp)
        ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .padding(paddings.spacingMedium)
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        start = paddings.spacingXSmall,
                        top = paddings.spacingXSmall,
                        bottom = paddings.spacingXSmall
                    )
            ) {
                Text(
                    text = "Hello, $athleteFirstName!",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black,
                    modifier = Modifier
                )

                Text(
                    text = "Let's explore events nearby...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }

            AsyncImage(
                model = imageUrl,
                contentDescription = "Profile Picture",
                placeholder = painterResource(R.drawable.default_profile),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(52.dp) // Adjust size as needed
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )
//        Image(
//            painter = painterResource(R.drawable.default_profile),
//            contentDescription = "Profile Image",
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .size(52.dp) // Adjust size as needed
//                .clip(CircleShape)
//                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
//        )

        }
    }
}

@Composable
fun FilterButtons(
    items: List<String> = listOf("All", "Road", "XC", "XCO", "Enduro", "Downhill"),
    onFilterButtonClick: (String) -> Unit
) {
    var selectedItem by remember { mutableStateOf<String?>("All") }


    Column() {
        Text(
            text = "Category",
            style = MaterialTheme.typography.labelLarge,
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(
                    start = paddings.spacingMedium,
                    bottom = paddings.spacingXSmall/2
                    //top = paddings.spacingXSmall
                )
                //.offset(y = -paddings.spacingXSmall)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = paddings.spacingMedium)
        ) {
            items(items) { item ->
                val isSelected = item == selectedItem
                Button(
                    onClick = {
                        selectedItem = item
                        // TODO: Implement actual action here
                        onFilterButtonClick(item)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.White,
                        contentColor = if (isSelected) Color.White else Color.Gray
                    ),
                    elevation = ButtonDefaults.buttonElevation(2.dp),
                    shape = RoundedCornerShape(shapes.small.topEnd),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(text = item)
                }
            }
        }
    }
}

@Composable
fun Events(
    events: List<EventInfo>,
    favoriteEventsId: List<String>,
    onFavoriteIconClick: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column() {
        Text(
            text = "Events",
            style = MaterialTheme.typography.labelLarge,
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(
                    start = paddings.spacingMedium,
                    //top = paddings.spacingXSmall
                )
            //.offset(y = paddings.spacingXSmall)
        )

        events.forEach { eventInfo ->
            EventCardUpdated(
                    eventInfo = eventInfo,
                    isUserLoggedIn = true,
                    countryCodeEmoji = countryMap[eventInfo.city] ?: "",
                    favoriteIcon = if(favoriteEventsId.contains(eventInfo.evenUuid)) painterResource(R.drawable.baseline_favorite_24) else painterResource(R.drawable.baseline_favorite_border_24),
                    onFavoriteIconClick = {
                        //TODO: Pass the event id, and update the database and local favorite list
                        Log.d("MainScreen UI", "onFavoriteClickFromUi -> ${eventInfo.evenUuid}, ${favoriteEventsId.contains(eventInfo.evenUuid)}")
                        onFavoriteIconClick(
                            eventInfo.evenUuid,
                            favoriteEventsId.contains(eventInfo.evenUuid)
                        )
                    },
                    onEventClick = {},
                    modifier = Modifier
                )
        }

//        LazyColumn {
//            items(events) { eventInfo ->
//                EventCardUpdated(
//                    eventInfo = eventInfo,
//                    isUserLoggedIn = true,
//                    countryCodeEmoji = countryMap[eventInfo.city] ?: "",
//                    onFavoriteIconClick = {},
//                    onEventClick = {},
//                    modifier = Modifier
//                )
//            }
//        }
    }
}


@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(
        athleteInfo = testAthlete,
        events = cyclingEvents,
        onFavoriteIconClick = {
            string: String, boolean: Boolean ->
        },
        onFilterButtonClick = {}
    )
}