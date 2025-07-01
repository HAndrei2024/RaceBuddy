package com.example.racebuddy.ui.v2.profile

import android.content.Context
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.racebuddy.R
import com.example.racebuddy.data.database.AthleteInfo
import com.example.racebuddy.data.database.EventInfo
import com.example.racebuddy.data.database.EventResultProfileInfo
import com.example.racebuddy.data.database.ResultAthleteInfo
import com.example.racebuddy.data.database.ResultInfo
import com.example.racebuddy.data.database.testAthlete
import com.example.racebuddy.data.database.testEvent
import com.example.racebuddy.data.database.testResult
import com.example.racebuddy.data.database.testResultAthleteInfo
import com.example.racebuddy.ui.theme.LightGray
import com.example.racebuddy.ui.theme.StravaOrange
import com.example.racebuddy.ui.theme.heights
import com.example.racebuddy.ui.theme.paddings
import com.example.racebuddy.ui.theme.shapes
import com.example.racebuddy.ui.theme.sizes
import com.example.racebuddy.ui.v2.common.BottomNavigationBarChat
import com.example.racebuddy.ui.v2.common.EventCardUpdated
import com.example.racebuddy.ui.v2.common.LoadingAnimation
import com.example.racebuddy.ui.v2.common.MainScreenTopAppBar
import com.example.racebuddy.ui.v2.event.DetailRow
import com.example.racebuddy.ui.v2.event.formatMillisToTimeString
import com.example.racebuddy.ui.v2.main.countryMap
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.utils.amPmHourToHour24
import network.chaintech.kmp_date_time_picker.utils.now



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    athleteInfo: AthleteInfo,
    isUserLoggedIn: Boolean,
    results: List<EventResultProfileInfo>,
    selectedFilterButton: String,
    selectedFilterResultButton: String,
    registeredEvents: List<EventInfo>,
    onEventClick: (eventInfo: EventInfo) -> Unit,
    onFilterButtonClick: (filter: String) -> Unit,
    onFilterResultsButtonClick: (filter: String) -> Unit,
    onStravaButtonClick: () -> Unit,
    yearsOfResults: List<String>,
    eventCategories: List<String>,
    onLogoutButtonClick: () -> Unit,
    onLoginButtonClick: () -> Unit,
    onBottomBarIconClick: (Int) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onSettingsIconClick: () -> Unit,
) {
    //var selectedFilterButton by remember { mutableStateOf("Results") }

    //var isRefreshing by remember { mutableStateOf(false) }


    yearsOfResults.filter { yearStr ->
        val yearInt = yearStr.toIntOrNull()
        yearInt != null && yearInt <= LocalDate.now().year
    }

    Scaffold(
        topBar = {
            MainScreenTopAppBar(
                onSettingsIconClick = onSettingsIconClick,
                onSearchIconClick = {},
                showSearchIcon = false
            )

        },
        bottomBar = {
            //BottomAppBarUpdated()
            BottomNavigationBarChat(
                selectedItem = 2,
                onItemSelected = { int: Int -> onBottomBarIconClick(int) },
            )
        },
        containerColor = Color.White,
        contentColor = Color.Black
    ) { innerPadding ->

        val animatedBlur by animateDpAsState(targetValue = if(isRefreshing) 1.dp else 0.dp)


        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                onRefresh()
            },
//            contentAlignment = Alignment.Center,
//            modifier = Modifier
//                .fillMaxSize(),

        ) {
            if(isRefreshing) {
               LoadingAnimation()
            }

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
                    .blur(
                        radius = animatedBlur,
                        edgeTreatment = BlurredEdgeTreatment.Unbounded
                    )
            ) {
                if (isUserLoggedIn) {
                    item {
                        AthleteDetails(
                            athleteInfo = athleteInfo,
                            onStravaButtonClick = onStravaButtonClick,
                            modifier = Modifier
                        )
                    }

                    item {
                        FilterButtons(
                            selectedItem = selectedFilterButton,
                            onFilterButtonClick = onFilterButtonClick
                        )
                    }

                    item {
                        if (selectedFilterButton == "Results")
                            FilterResultsButtons(
                                selectedItem = selectedFilterResultButton,
                                items = listOf("All") + eventCategories,
                                onFilterButtonClick = onFilterResultsButtonClick
                            )
                    }

                    item {
                        when (selectedFilterButton) {
                            "Results" -> {

                                if (yearsOfResults.isEmpty()) {
                                    Column(
                                        modifier = Modifier
                                            .padding(paddings.spacingSmall)
                                            .shadow(
                                                3.dp,
                                                shapes.small
                                            ) // Shadow with rounded corners
                                            .background(
                                                Color.White,
                                                shapes.small
                                            ) // Background is required//
                                    ) {
                                        Text(
                                            text = "Year",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.Gray,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                //.background(Color(0xFFEEEEEE))
                                                .padding(
                                                    paddings.spacingSmall
                                                    //start = paddings.spacingMedium,
                                                    //bottom = paddings.spacingSmall
                                                    //top = paddings.spacingXSmall
                                                )
                                                .fillMaxWidth(1f)

                                                .drawBehind {
                                                    val borderSize = 2.dp.toPx()
                                                    drawLine(
                                                        color = Color(0xFFEEEEEE),
                                                        start = Offset(0f, size.height),
                                                        end = Offset(size.width, size.height),
                                                        strokeWidth = borderSize
                                                    )
                                                }
                                        )

                                        Column(
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(75.dp)
                                        ) {
                                            Text(
                                                text = "You have no results...",
                                                maxLines = 2,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                                yearsOfResults.sortedDescending().forEach { year ->
                                    Column(
                                        modifier = Modifier
                                            .padding(paddings.spacingSmall)
                                            .shadow(
                                                3.dp,
                                                shapes.small
                                            ) // Shadow with rounded corners
                                            .background(
                                                Color.White,
                                                shapes.small
                                            ) // Background is required//
                                    ) {
                                        Text(
                                            text = year,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.Gray,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                //.background(Color(0xFFEEEEEE))
                                                .padding(
                                                    paddings.spacingSmall
                                                    //start = paddings.spacingMedium,
                                                    //bottom = paddings.spacingSmall
                                                    //top = paddings.spacingXSmall
                                                )
                                                .fillMaxWidth(1f)

                                                .drawBehind {
                                                    val borderSize = 2.dp.toPx()
                                                    drawLine(
                                                        color = Color(0xFFEEEEEE),
                                                        start = Offset(0f, size.height),
                                                        end = Offset(size.width, size.height),
                                                        strokeWidth = borderSize
                                                    )
                                                }
                                            //.offset(y = paddings.spacingXSmall)
                                        )
                                        val filteredResults =
                                            results.filter { it.startDate.year == year.toInt() }

                                        if (filteredResults.isNotEmpty()) {
                                            filteredResults.forEach { result ->
                                                EventResultCard(
                                                    year = result.startDate.year,
                                                    eventTitle = result.title,
                                                    eventBackgroundImageUrl = result.backgroundPictureUrl,
                                                    rank = result.rank,
                                                    time = result.time,
                                                    onEventClick = {
                                                    },
                                                    resultCategory = result.resultCategory,
                                                    eventCategory = result.eventCategory
                                                )

                                            }
                                        } else {
                                            Column(
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(75.dp)
                                            ) {
                                                Text(
                                                    text = "You have no results for the selected year...",
                                                    maxLines = 2,
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            "Registered" -> {


                                RegisteredTab(
                                    registeredEvents = registeredEvents,
                                    onEventClick = onEventClick,
                                    modifier = Modifier
                                )

                            }
                        }

                    }

//                    item {
//                        LogoutButton(
//                            onLogoutButtonClick
//                        )
//                    }


                } else {
                    item {
                        NoUserLoggedInScreen(
                            onLoginButtonClick = onLoginButtonClick
                        )
                    }
                }
            }


        }
    }
}

@Composable
fun NoUserLoggedInScreen(
    onLoginButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        
        Image(
            painter = painterResource(R.drawable.default_profile),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(paddings.spacingSmall)
                .size(80.dp) // Adjust size as needed
                .clip(shapes.extraSmall)
        )
        
        Text(
            text = "Please log in first!"
        )

        LoginButton(
            onLoginButtonClick
        )
    }
}

@Composable
fun RegisteredTab(
    registeredEvents: List<EventInfo>,
    onEventClick: (eventInfo: EventInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
    ) {
        registeredEvents.forEach { event ->
            EventCardUpdated(
                eventInfo = event,
                isUserLoggedIn = false,
                countryCodeEmoji = countryMap.get(event.country) ?: "",
                onFavoriteIconClick = {},
                onEventClick = {
                    onEventClick(event)
                },
                modifier = Modifier
            )
        }
    }
}

@Composable
fun AthleteDetails(
    athleteInfo: AthleteInfo,
    onStravaButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(paddings.spacingSmall)
//            .shadow(3.dp, shapes.small) // Shadow with rounded corners
//            .background(Color.White, shapes.small)
    ) {
//        Text(
//            text = "Details",
//            style = MaterialTheme.typography.bodyMedium,
//            color = Color.Gray,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier
//                //.background(Color(0xFFEEEEEE))
//                .padding(
//                    paddings.spacingSmall
//                    //start = paddings.spacingMedium,
//                    //bottom = paddings.spacingSmall
//                    //top = paddings.spacingXSmall
//                )
//                .fillMaxWidth(1f)
//
//                .drawBehind {
//                    val borderSize = 2.dp.toPx()
//                    drawLine(
//                        color = Color(0xFFEEEEEE),                        start = Offset(0f, size.height),
//                        end = Offset(size.width, size.height),
//                        strokeWidth = borderSize
//                    )
//                }
//            //.offset(y = paddings.spacingXSmall)
//        )
//
//

        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = athleteInfo.profilePictureUrl,
                    contentDescription = "",
                    placeholder = painterResource(R.drawable.default_profile),
                    error = painterResource(R.drawable.ic_launcher_foreground),
                    fallback = painterResource(R.drawable.default_profile),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(paddings.spacingSmall)
                        .size(125.dp) // Adjust size as needed
                        .clip(shapes.medium)
                    // .border(0.5.dp, MaterialTheme.colorScheme.primary, shapes.small)
                    //.weight(0.5f)
                )

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = StravaOrange,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(2.dp),
                    shape = RoundedCornerShape(shapes.small.topEnd),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    onClick = onStravaButtonClick,
                    modifier = Modifier
                        .scale(0.8f)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "Connect with Strava",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            //.padding(paddings.spacingXSmall)
                    )
                }
            }

            AthleteDetailsColumn(
                athleteInfo = athleteInfo,
                areDetailsFilled = athleteInfo.gender != "-",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
fun AthleteDetailsColumn(
    athleteInfo: AthleteInfo,
    areDetailsFilled: Boolean,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    var age = today.year - athleteInfo.birthdate.year
    if (
        today.monthNumber < athleteInfo.birthdate.monthNumber ||
        (today.monthNumber == athleteInfo.birthdate.monthNumber && today.dayOfMonth < athleteInfo.birthdate.dayOfMonth)
    ) {
        age -= 1
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = paddings.spacingSmall, end = paddings.spacingSmall, bottom = paddings.spacingSmall)
//            .shadow(3.dp, shapes.small) // Shadow with rounded corners
//            .background(Color.White, shapes.small)
    ) {

        DetailRow(
            field = "Full Name",
            value = if(!areDetailsFilled) "-" else "${athleteInfo.firstName} ${athleteInfo.lastName}",
            maxLines = 2,
            modifier = Modifier
        )

        DetailRow(
            field = "Gender",
            value = if(!areDetailsFilled) "-" else "${athleteInfo.gender}",
            modifier = Modifier
        )

        DetailRow(
            field = "Age",
            value = if(!areDetailsFilled) "-" else "$age",
            modifier = Modifier
        )

        DetailRow(
            field = "Country",
            value = if(!areDetailsFilled) "-" else "${athleteInfo.country} ${countryMap.get(athleteInfo.country)}",
            maxLines = 2,
            modifier = Modifier
        )

    }
}



@Composable
fun DetailRow(
    field: String,
    value: String,
    maxLines: Int = 1,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(paddings.spacingSmall)
    ) {
        Text(
            text = "$field:",
            style = MaterialTheme.typography.titleSmall,
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            maxLines = maxLines,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            maxLines = maxLines,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Composable
fun ResultsPerYearCard(
    year: Int,
    results: List<ResultInfo>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(paddings.spacingSmall)
            .shadow(3.dp, shapes.small) // Shadow with rounded corners
            .background(Color.White, shapes.small) // Background is required//
            .clickable {
            }
        // .drawBehind {
//                val strokeWidth = 1.dp.toPx()
//                drawLine(
//                    color = Color(0xFFDDDDDD),
//                    start = Offset(0f, 0f),
//                    end = Offset(size.width, 0f),
//                    strokeWidth = strokeWidth
//                )
//            }
    ) {
        Text(
            text = year.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                //.background(Color(0xFFEEEEEE))
                .padding(
                    paddings.spacingSmall
                    //start = paddings.spacingMedium,
                    //bottom = paddings.spacingSmall
                    //top = paddings.spacingXSmall
                )
                .fillMaxWidth(1f)

                .drawBehind {
                    val borderSize = 2.dp.toPx()
                    drawLine(
                        color = Color(0xFFEEEEEE), start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = borderSize
                    )
                }
        )


        if (results.isNotEmpty()) {

        }
        else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp)
            ) {
                Text(
                    text = "There are no results...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun EventResultCard(
    year: Int,
    eventTitle: String,
    eventBackgroundImageUrl: String,
    rank: Int,
    time: Long,
    resultCategory: String,
    eventCategory: String,
    onEventClick: (eventInfo: EventInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = CardDefaults.elevatedShape,
//        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
//        //border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        modifier = modifier
            .fillMaxWidth()
//            .clickable {
//                onEventClick
//            }
            .padding(paddings.spacingSmall)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = eventBackgroundImageUrl,
                    contentDescription = "",
                    placeholder = painterResource(R.drawable.default_background),
                    error = painterResource(R.drawable.default_background),
                    fallback = painterResource(R.drawable.default_background),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(paddings.spacingSmall)
                        .height(heights.extraLarge)
                        .width(sizes.large) // Adjust size as needed
                        .clip(shapes.small)
                        .weight(2f)
                )

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(3f)
                        .height(heights.extraLarge)
                ) {

                    Text(
                        text = eventTitle,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                        modifier = Modifier
                            .weight(3f)
                            //.shadow(1.dp, shapes.small) // Shadow with rounded corners
                            .padding(start = paddings.spacingSmall, end = paddings.spacingSmall)
                            .background(Color.White, shapes.small)
                    )

                    Text(
                        text = eventCategory,
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50)) // Fully rounded corners = oval effect
                            .background(Color.LightGray)
                            .padding(horizontal = 12.dp, vertical = 4.dp)// Custom background color
//                        .padding(
//                            top = paddings.spacingXSmall,
//                            bottom = paddings.spacingMedium
//                        )
                    )
                }
            }

            ResultHeader()
            ResultRow(
                rank = rank,
                time = time,
                category = resultCategory,
                modifier = Modifier
                    .padding(bottom = paddings.spacingSmall),
            )
        }
    }
}

@Composable
fun ResultRow(
    rank: Int,
    time: Long,
    category: String, 
    emojiString: String = "",
    elevation: Dp = 3.dp,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(paddings.spacingSmall)

    ) {


            Text(
                text = "${rank}.",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
            )

            Text(
                text = "${formatMillisToTimeString(time)}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
            )


        Text(
            text = category,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
        )

    }
}

@Composable
fun ResultHeader(
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium, //titleSmall,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = paddings.spacingMedium, bottom = paddings.spacingMedium, start = paddings.spacingSmall, end = paddings.spacingSmall)
            .drawBehind {
                val borderSize = 2.dp.toPx()
                drawLine(
                    color = Color(0xFFEEEEEE),                        start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = borderSize
                )
            }
    ) {
        Text(
            text = "Rank",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
        )

        Text(
            text = "Time",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
        )


        Text(
            text = "Category",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
        )

    }
}


@Composable
fun LogoutButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick
    ) {
        Text(
            text = "Logout"
        )
    }
}

@Composable
fun FilterResultsButtons(
    selectedItem: String,
    items: List<String>,
    onFilterButtonClick: (String) -> Unit
) {
    //var selectedItem by remember { mutableStateOf<String?>(defaultSelectedItem) }


    Column(
        modifier = Modifier
            .padding(paddings.spacingSmall)
            .drawBehind {
                val borderSize = 1.dp.toPx()
                drawLine(
                    color = Color(0xFFEEEEEE),
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = borderSize
                )
            }
    ) {
//        Text(
//            text = "Category",
//            style = MaterialTheme.typography.labelLarge,
//            color = Color.Black,
//            fontWeight = FontWeight.Normal,
//            modifier = Modifier
//                //.background(Color(0xFFEEEEEE))
//                .padding(
//                    start = paddings.spacingMedium,
//                    bottom = paddings.spacingXSmall/2
//                    //top = paddings.spacingXSmall
//                )
//                .fillMaxWidth()
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
        //)

        LazyRow(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = paddings.spacingSmall),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            //contentPadding = PaddingValues(horizontal = paddings.spacingMedium)
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
fun FilterButtons(
    selectedItem: String,
    items: List<String> = listOf("Results", "Registered"),
    onFilterButtonClick: (String) -> Unit
) {
    //var selectedItem by remember { mutableStateOf<String?>("Results") }


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
            horizontalArrangement = Arrangement.SpaceEvenly,
            contentPadding = PaddingValues(horizontal = paddings.spacingMedium),
            modifier = Modifier
                .drawBehind {
                    val strokeWidth = 1.dp.toPx()
                    drawLine(
                        color = Color(0xFFDDDDDD),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = strokeWidth
                    )
                }
                .drawBehind {
                    val strokeWidth = 1.dp.toPx()
                    val y =
                        size.height - strokeWidth / 2  // Adjust for stroke centering
                    drawLine(
                        color = Color(0xFFDDDDDD),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = strokeWidth
                    )
                }
                .fillMaxWidth()
                .padding(top = paddings.spacingSmall, bottom = paddings.spacingSmall),
        ) {
            items(items) { item ->
                val isSelected = item == selectedItem
                Button(
                    onClick = {
                        // TODO: Implement actual action here
                        onFilterButtonClick(item)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
                    ),
                    //elevation = ButtonDefaults.buttonElevation(2.dp),
                    shape = RoundedCornerShape(shapes.small.topEnd),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    modifier = Modifier
                        .height(heights.small)
                        .padding(end = paddings.spacingSmall)
                        .drawBehind {
                            if(isSelected) {
                                val strokeWidth = 2.dp.toPx()
                                val y =
                                    size.height - strokeWidth / 2  // Adjust for stroke centering
                                drawLine(
                                    color = Color(0xFF4169E1),
                                    start = Offset(0f, y),
                                    end = Offset(size.width, y),
                                    strokeWidth = strokeWidth
                                )
                            }
                        }
                ) {
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if(isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                        fontWeight = if(isSelected) FontWeight.Normal else FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun LoginButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick
    ) {
        Text(
            text = "Login"
        )
    }
}

@Preview
@Composable
fun AthleteDetailsPreview() {
    AthleteDetails(
        athleteInfo = testAthlete,
        onStravaButtonClick = {},
        modifier = Modifier
    )
}

@Preview
@Composable
fun EventResultCardPreview() {
    EventResultCard(
        year = 2024,
        eventTitle = testEvent.title,
        eventBackgroundImageUrl = "",
        modifier = Modifier,
        rank = 1,
        time = 8300,
        resultCategory = "Junior",
        onEventClick = {},
        eventCategory = "XC"
    )
}

@Preview
@Composable
fun ResultRowPreview() {
    ResultRow(
        rank = 1,
        time = 8500,
        category = "Junior",
    ) 
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        athleteInfo = testAthlete,
        onBottomBarIconClick = {},
        isUserLoggedIn = true,
        onLogoutButtonClick = {},
        onLoginButtonClick = {},
        yearsOfResults = listOf("2025", "2024", "2023"),
        eventCategories = listOf("Road", "XC", "Enduro", "Downhill"),
        results = emptyList(),
        registeredEvents = emptyList(),
        onEventClick = {},
        onFilterResultsButtonClick = {},
        selectedFilterButton = "Results",
        selectedFilterResultButton = "All",
        onFilterButtonClick = {},
        onStravaButtonClick = {},
        onRefresh = {},
        isRefreshing = false,
        onSettingsIconClick = {}
    )
}