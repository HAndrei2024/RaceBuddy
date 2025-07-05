package com.example.racebuddy.ui.v2.main

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.racebuddy.R
import com.example.racebuddy.data.database.AthleteInfo
import com.example.racebuddy.data.database.EventInfo
import com.example.racebuddy.data.database.cyclingEvents
import com.example.racebuddy.data.database.testAthlete
import com.example.racebuddy.ui.theme.heights
import com.example.racebuddy.ui.theme.paddings
import com.example.racebuddy.ui.theme.shapes
import com.example.racebuddy.ui.v2.common.BottomNavigationBarChat
import com.example.racebuddy.ui.v2.common.EventCardUpdated
import com.example.racebuddy.ui.v2.common.LoadingAnimation
import com.example.racebuddy.ui.v2.common.MainScreenTopAppBar
import com.example.racebuddy.ui.v2.search.SearchEventCard
import com.example.racebuddy.ui.v2.search.SearchScreen
import com.example.racebuddy.ui.v2.search.SearchScreenSheet
import com.example.racebuddy.ui.v2.signup.Country
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.utils.now


val countries = listOf(
    Country("Argentina", "🇦🇷"),
    Country("Australia", "🇦🇺"),
    Country("Austria", "🇦🇹"),
    Country("Belgium", "🇧🇪"),
    Country("Bulgaria", "\uD83C\uDDE7\uD83C\uDDEC"),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    athleteInfo: AthleteInfo,
    events: List<EventInfo>,
    searchEvents: List<EventInfo>,
    selectedFilter: String,
    onFilterButtonClick: (String) -> Unit,
    onFavoriteIconClick: (String, Boolean) -> Unit,
    favoriteEventsId: List<String> = emptyList(),
    searchFilters: List<String> = listOf("All", "Past", "Upcoming"),
    onSearchIconClick: () -> Unit,
    onProfilePicClick: () -> Unit,
    onSettingsIconClick: () -> Unit,
    onEventClick: (eventInfo: EventInfo) -> Unit,
    onFavoriteIconBottomBarClick: () -> Unit,
    onProfileIconBottomBarClick: () -> Unit,
    onBottomBarIconClicked: (Int) -> Unit,
    onRefresh: () -> Unit,
    isRefreshing: Boolean,
    isUserLoggedIn: Boolean,
    modifier: Modifier = Modifier
) {
    var showSearch by remember { mutableStateOf(false) }
    //var selectedFilter by remember { mutableStateOf("All") }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()

    val animatedBlur by animateDpAsState(targetValue = if(isRefreshing) 1.dp else 0.dp)

    Scaffold(
        topBar = {
//            TopBarWithCategoryAndSearchChat(
//                categories = listOf("XC", "Downhill", "Road", "All"),
//                selectedCategory = "All",
//                onCategorySelected = {},
//                onSearchQueryChanged = {}
//            )
            MainScreenTopAppBar(
                onSettingsIconClick = onSettingsIconClick,
                onSearchIconClick = {
                    onSearchIconClick()
                    showSearch = true

                    scope.launch {
                        sheetState.expand()
                        sheetState.show()
                    }.invokeOnCompletion {
                        showSearch = true
                    }
                }
            )
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
                onItemSelected = { int: Int -> onBottomBarIconClicked(int) }
            )
        },
        containerColor = Color.White,
        contentColor = Color.Black
    ) { innerPadding ->

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                onRefresh()
            },
//            contentAlignment = Alignment.Center,
//            modifier = Modifier
//                .fillMaxSize(),

        ) {
            if (isRefreshing) {
                LoadingAnimation(
                    modifier = Modifier
                        .zIndex(2f)
                )
            }
            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .blur(
                        radius = animatedBlur,
                        edgeTreatment = BlurredEdgeTreatment.Unbounded
                    )
                    .zIndex(1f)
            ) {
                HelloText(
                    athleteFirstName = athleteInfo.firstName ?: "",
                    imageUrl = athleteInfo.profilePictureUrl ?: "",
                    onProfilePicClick = onProfilePicClick
                )

                Spacer(
                    modifier = Modifier
                        .padding(paddings.spacingSmall)
                )


                FilterButtons(
                    selectedItem = selectedFilter,
                    onFilterButtonClick = onFilterButtonClick
                )
                Spacer(
                    modifier = Modifier
                        .padding(top = paddings.spacingSmall)
                )

                Events(
                    events = events,
                    favoriteEventsId = favoriteEventsId,
                    onFavoriteIconClick = onFavoriteIconClick,
                    isUserLoggedIn = isUserLoggedIn,
                    onEventClick = onEventClick
                )
            }
        }

        if (showSearch) {
            var searchQuery by remember { mutableStateOf("") }
            var searchFilteredEvents by remember { mutableStateOf(searchEvents) }
            var filter by remember { mutableStateOf("All") }


            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        showSearch = false
                    }
                },
                sheetState = sheetState,
                containerColor = Color.White,
                contentColor = Color.Black,
                dragHandle = {},
                shape = shapes.small,
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Column(
                    //verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxHeight(0.95f)
                    //.height((LocalConfiguration.current.screenHeightDp.dp) * 0.95f) // 95% height
                ) {

                    SearchTextField(
                        searchQuery = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            when (filter) {
                                "All" -> {
                                    searchFilteredEvents = searchEvents.filter { eventInfo ->
                                        eventInfo.title.contains(
                                            searchQuery,
                                            ignoreCase = true
                                        ) ||
                                                eventInfo.category.contains(
                                                    searchQuery,
                                                    ignoreCase = true
                                                )
                                    }.sortedBy { event -> event.startDate }
                                }

                                "Past" -> {
                                    searchFilteredEvents = searchEvents.filter { eventInfo ->
                                        (eventInfo.title.contains(
                                            searchQuery,
                                            ignoreCase = true
                                        )
                                                || eventInfo.category.contains(
                                            searchQuery,
                                            ignoreCase = true
                                        )) &&
                                                eventInfo.startDate <= LocalDate.now()

                                    }.sortedByDescending { event -> event.startDate }
                                }

                                "Upcoming" -> {
                                    searchFilteredEvents = searchEvents.filter { eventInfo ->
                                        (eventInfo.title.contains(
                                            searchQuery,
                                            ignoreCase = true
                                        )
                                                || eventInfo.category.contains(
                                            searchQuery,
                                            ignoreCase = true
                                        )) &&
                                                eventInfo.startDate > LocalDate.now()
                                    }.sortedBy { event -> event.startDate }
                                }

                                else -> {

                                }
                            }
                        },
                        onCloseClick = {
                            scope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                showSearch = false
                            }
                        }
                    )

                    SearchFilterButtons(
                        items = searchFilters,
                        onFilterButtonClick = { item: String ->
                            filter = item
                            when (item) {
                                "All" -> {
                                    searchFilteredEvents = searchEvents.filter { eventInfo ->
                                        eventInfo.title.contains(
                                            searchQuery,
                                            ignoreCase = true
                                        ) ||
                                                eventInfo.category.contains(
                                                    searchQuery,
                                                    ignoreCase = true
                                                )
                                    }
                                }

                                "Past" -> {
                                    searchFilteredEvents = searchEvents.filter { eventInfo ->
                                        (eventInfo.title.contains(
                                            searchQuery,
                                            ignoreCase = true
                                        )
                                                || eventInfo.category.contains(
                                            searchQuery,
                                            ignoreCase = true
                                        )) &&
                                                eventInfo.startDate <= LocalDate.now()
                                    }
                                }

                                "Upcoming" -> {
                                    searchFilteredEvents = searchEvents.filter { eventInfo ->
                                        (eventInfo.title.contains(
                                            searchQuery,
                                            ignoreCase = true
                                        )
                                                || eventInfo.category.contains(
                                            searchQuery,
                                            ignoreCase = true
                                        )) &&
                                                eventInfo.startDate > LocalDate.now()
                                    }
                                }

                                else -> {

                                }
                            }

                        }
                    )

                    //DelimitatorText()

                    SearchEventList(
                        events = searchFilteredEvents,
                        isUserLoggedIn = isUserLoggedIn,
                        favoriteEventsId = favoriteEventsId,
                        onFavoriteIconClick = onFavoriteIconClick,
                        onEventClick = onEventClick
                    )
                }

            }
        }
    }

}

@Composable
fun SearchEventList(
    events: List<EventInfo>,
    onEventClick: (eventInfo: EventInfo) -> Unit,
    isUserLoggedIn: Boolean,
    favoriteEventsId: List<String>,
    onFavoriteIconClick: (String, Boolean) -> Unit
) {
    LazyColumn {
        items(events) { eventInfo ->
            EventCardUpdated(
                eventInfo = eventInfo,
                isUserLoggedIn = isUserLoggedIn,
                countryCodeEmoji = countryMap[eventInfo.city] ?: "",
                favoriteIcon = if(favoriteEventsId.contains(eventInfo.eventUuid)) painterResource(R.drawable.baseline_favorite_24) else painterResource(R.drawable.baseline_favorite_border_24),
                onFavoriteIconClick = {
                    //TODO: Pass the event id, and update the database and local favorite list
                    Log.d("MainScreen UI", "onFavoriteClickFromUi -> ${eventInfo.eventUuid}, ${favoriteEventsId.contains(eventInfo.eventUuid)}")
                    onFavoriteIconClick(
                        eventInfo.eventUuid,
                        favoriteEventsId.contains(eventInfo.eventUuid)
                    )
                },
                onEventClick = {
                    onEventClick(eventInfo)
                },
                modifier = Modifier
            )
        }
    }
}

@Composable
fun DelimitatorText() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFEEEEEE))
            .height(10.dp)
    ) {

    }
}

@Composable
fun SearchTextField(
    searchQuery: String,
    onValueChange: (String) -> Unit,
    onCloseClick: () -> Unit
) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color(0xFFEEEEEE))
        ) {

            TextField(
                value = searchQuery,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        text = "Search...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        modifier = Modifier
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .padding(paddings.spacingSmall)
                    .scale(scaleY = 0.9F, scaleX = 0.9F)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = shapes.large
                    )
                    .weight(7f)
                //.height(80.dp)
                ,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = ""
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White, //Color(0xFFF5F5F5),
                    unfocusedContainerColor = Color.White, //Color(0xFFF5F5F5),
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White
                ),
                shape = shapes.large,

                )

            IconButton(
                onClick = onCloseClick,
                modifier = Modifier
                    .weight(1f)
                .padding(end = paddings.spacingSmall)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = ""
                )
            }
        }
}

@Composable
fun SearchFilterButtons(
    items: List<String> = listOf("All", "Past", "Upcoming"),
    onFilterButtonClick: (String) -> Unit
) {
    var selectedItem by remember { mutableStateOf<String?>("All") }


    Column(
        modifier = Modifier
            //.background(Color(0xFFEEEEEE))
    ) {
//        Text(
//            text = "Period",
//            style = MaterialTheme.typography.labelLarge,
//            color = Color.Black,
//            fontWeight = FontWeight.Normal,
//            modifier = Modifier
//                .padding(
//                    start = paddings.spacingMedium,
//                    bottom = paddings.spacingXSmall/2
//                    //top = paddings.spacingXSmall
//                )
//            //.offset(y = -paddings.spacingXSmall)
//        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = paddings.spacingMedium),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddings.spacingSmall, bottom = paddings.spacingSmall),
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
                        contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.primary
                    ),
                    elevation = ButtonDefaults.buttonElevation(2.dp),
                    shape = RoundedCornerShape(shapes.small.topEnd),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    modifier = Modifier
                        .height(heights.small)
                ) {
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun HelloText(
    athleteFirstName: String,
    imageUrl: String,
    personalizedText: String = "Let's explore events nearby...",
    onProfilePicClick: () -> Unit,
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
                    text = personalizedText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }

            AsyncImage(
                model = imageUrl.takeIf { it.isNotBlank() },
                contentDescription = "Profile Picture",
                placeholder = painterResource(R.drawable.default_profile),
                error = painterResource(R.drawable.ic_launcher_foreground),
                fallback = painterResource(R.drawable.default_profile),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(52.dp) // Adjust size as needed
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .clickable {
                        onProfilePicClick()
                    }
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
    selectedItem: String = "All",
    onFilterButtonClick: (String) -> Unit
) {
    //var selectedItem by remember { mutableStateOf<String?>(selectedItem) }


    Column(
        modifier = Modifier
//            .drawBehind {
//                val borderSize = 2.dp.toPx()
//                drawLine(
//                    color = Color(0xFFEEEEEE),
//                    start = Offset(0f, size.height),
//                    end = Offset(size.width, size.height),
//                    strokeWidth = borderSize
//                )
//            }
    ) {
        Text(
            text = "Category",
            style = MaterialTheme.typography.labelLarge,
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                //.background(Color(0xFFEEEEEE))
                .padding(
                    start = paddings.spacingMedium,
                    bottom = paddings.spacingXSmall/2
                    //top = paddings.spacingXSmall
                )
                .fillMaxWidth()
//                .drawBehind {
//                    val borderSize = 2.dp.toPx()
//                    drawLine(
//                        color = Color(0xFFEEEEEE),
//                        start = Offset(-paddings.spacingMedium.toPx(), size.height),
//                        end = Offset(size.width, size.height),
//                        strokeWidth = borderSize
//                    )
//                }
                //.offset(y = -paddings.spacingXSmall)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = paddings.spacingSmall),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = paddings.spacingMedium)
        ) {
            items(items) { item ->
                val isSelected = item == selectedItem
                Button(
                    onClick = {
                        //selectedItem = item
                        // TODO: Implement actual action here
                        onFilterButtonClick(item)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.White,
                        contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.primary
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
    onEventClick: (eventInfo: EventInfo) -> Unit,
    isUserLoggedIn: Boolean,
    modifier: Modifier = Modifier
) {
    Column() {
            Text(
                text = "Events",
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

        events.forEach { eventInfo ->
            EventCardUpdated(
                    eventInfo = eventInfo,
                    isUserLoggedIn = isUserLoggedIn,
                    countryCodeEmoji = countryMap[eventInfo.city] ?: "",
                    favoriteIcon = if(favoriteEventsId.contains(eventInfo.eventUuid)) painterResource(R.drawable.baseline_favorite_24) else painterResource(R.drawable.baseline_favorite_border_24),
                    onFavoriteIconClick = {
                        //TODO: Pass the event id, and update the database and local favorite list
                        Log.d("MainScreen UI", "onFavoriteClickFromUi -> ${eventInfo.eventUuid}, ${favoriteEventsId.contains(eventInfo.eventUuid)}")
                        onFavoriteIconClick(
                            eventInfo.eventUuid,
                            favoriteEventsId.contains(eventInfo.eventUuid)
                        )
                    },
                    onEventClick = {
                        onEventClick(eventInfo)
                    },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenBottomSheet(

) {
    ModalBottomSheet(
        onDismissRequest = {}
    ) {

    }
}


@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(
        athleteInfo = testAthlete,
        events = cyclingEvents,
        onFavoriteIconClick = { string: String, boolean: Boolean ->
        },
        onFilterButtonClick = {},
        favoriteEventsId = emptyList(),
        onSearchIconClick = {},
        searchEvents = emptyList(),
        onProfileIconBottomBarClick = {},
        onFavoriteIconBottomBarClick = {},
        onBottomBarIconClicked = {},
        isUserLoggedIn = true,
        onEventClick = {},
        selectedFilter = "All",
        onProfilePicClick = {},
        onSettingsIconClick = {},
        onRefresh = {},
        isRefreshing = false
    )
}