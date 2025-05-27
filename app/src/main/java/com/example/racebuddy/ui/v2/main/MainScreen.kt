package com.example.racebuddy.ui.v2.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.racebuddy.R
import com.example.racebuddy.data.database.EventInfo
import com.example.racebuddy.data.database.cyclingEvents
import com.example.racebuddy.ui.theme.paddings
import com.example.racebuddy.ui.theme.shapes
import com.example.racebuddy.ui.v2.common.BottomAppBarUpdated
import com.example.racebuddy.ui.v2.common.EventCardUpdated
import com.example.racebuddy.ui.v2.common.MainScreenTopAppBar
import com.example.racebuddy.ui.v2.common.TopBarWithCategoryAndSearchChat
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
    athleteId: String,
    events: List<EventInfo>,
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
        },
        bottomBar = {
            BottomAppBarUpdated()
        },
        containerColor = Color.White,
        contentColor = Color.Black
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
        ) {
            HelloText(
                athleteFirstName = "Name",
                imageUrl = "",
            )

            Spacer(
                modifier = Modifier
                    .padding(paddings.spacingSmall)
            )
            FilterButtons()
            Spacer(
                modifier = Modifier
                    .padding(paddings.spacingSmall)
            )

            Events(
                events = events
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
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
          .fillMaxWidth()
            .padding(paddings.spacingMedium)
    ) {
        Column() {
            Text(
                text = "Hello, $athleteFirstName!",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(bottom = paddings.spacingXSmall)
            )

            Text(
                text = "Let's explore events nearby...",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        }

        Image(
            painter = painterResource(R.drawable.default_profile),
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(52.dp) // Adjust size as needed
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

    }
}

@Composable
fun FilterButtons(
    items: List<String> = listOf("Road", "XC", "XCO", "Enduro", "Downhill")
) {
    var selectedItem by remember { mutableStateOf<String?>(null) }


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

        LazyColumn {
            items(events) { eventInfo ->
                EventCardUpdated(
                    eventInfo = eventInfo,
                    isUserLoggedIn = true,
                    countryCodeEmoji = countryMap[eventInfo.city] ?: "",
                    onFavoriteIconClick = {},
                    onEventClick = {},
                    modifier = Modifier
                )
            }
        }
    }
}


@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(
        athleteId = "",
        events = cyclingEvents
    )
}