package com.example.racebuddy.ui.v2.event

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.isTraceInProgress
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.racebuddy.R
import com.example.racebuddy.data.database.AthleteInfo
import com.example.racebuddy.data.database.EventInfo
import com.example.racebuddy.data.database.testAthlete
import com.example.racebuddy.data.database.testEvent
import com.example.racebuddy.ui.theme.heights
import com.example.racebuddy.ui.theme.paddings
import com.example.racebuddy.ui.theme.shapes
import com.example.racebuddy.ui.v2.main.countryMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun EventScreen(
    athleteInfo: AthleteInfo,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val imageHeight = screenHeight * 0.30f

        Box(
            modifier = Modifier
        ) {
            // Top image
            Image(
                painter = painterResource(id = R.drawable.default_background), // Replace with your image
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight),
                contentScale = ContentScale.Crop
            )

            EventScreenTopBar(
                isUserLoggedIn = athleteInfo.athleteId != "-1",
                isFavorite = false,
                onBackClick = onBackClick,
                onFavoriteClick = {}
            )

            // Add the rest of your screen content below
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = imageHeight)
            ) {
                // Event details go here
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .align(Alignment.BottomCenter)
                    .background(Color.LightGray)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Buy Ticket", fontWeight = FontWeight.Bold)
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                }
            }
        }


}

@Composable
fun EventScreenTopBar(
    isUserLoggedIn: Boolean,
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Transparent Top App Bar overlay
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(56.dp)
            .background(Color.White.copy(alpha = 0f)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Back Button in White Circle
        Card(
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.size(40.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxSize() // Fill the card
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        }

        if(isUserLoggedIn) {
            // Heart Icon (favorite)
            Card(
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.size(40.dp)
            ) {
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = if(isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                    )
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen2(
    athleteInfo: AthleteInfo,
    eventInfo: EventInfo,
    isFavorite: Boolean,
    onShowMoreTextClick: () -> Unit = {},
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val imageHeight = screenHeight * 0.30f

//    val scope = rememberCoroutineScope()
//    val pagerState = rememberPagerState(pageCount = { FilterEventTabs.entries.size })
    var selectedFilterButton by remember { mutableStateOf("Summary") }

    var showDetails by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
        },
        bottomBar = {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(heights.largeish)
                    .height(heights.largeish)
                    .background(Color.LightGray)
                    .drawBehind {
                        val strokeWidth = 1.dp.toPx()
                        drawLine(
                            color = Color(0xFFDDDDDD),
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = strokeWidth
                        )
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(top = paddings.spacingSmall),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    //Text("385.00 RON", fontWeight = FontWeight.Bold, color = Color.White)
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(2.dp),
                        shape = RoundedCornerShape(shapes.small.topEnd),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier
                            .height(heights.small)
                            .width(heights.extraLarge * 2)
                    ) {
                        Text(
                            text = "Register",
                        )
                    }

                }
            }
        },
        containerColor = Color.White,
        contentColor = Color.Black
    ) { innerPadding ->


            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .padding(top = paddings.spacingMedium)
                    ) {
                        EventScreenTopBar(
                            isUserLoggedIn = athleteInfo.athleteId != "-1",
                            isFavorite = isFavorite,
                            onBackClick = onBackClick,
                            onFavoriteClick = onFavoriteClick,
                            modifier = Modifier.zIndex(1f)
                        )
                        AsyncImage(
                            model = eventInfo.backgroundPictureUrl.takeIf { it.isNotBlank() },
                            contentDescription = "Background Picture",
                            placeholder = painterResource(R.drawable.default_background),
                            error = painterResource(R.drawable.default_background),
                            fallback = painterResource(R.drawable.default_background),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(imageHeight),
                            contentScale = ContentScale.Crop
                        )

//                        Image(
//                            painter = painterResource(id = R.drawable.default_background), // Replace with your image
//                            contentDescription = null,
//
//                        )
                    }
                }



                item {
//                    FilterPager(
//                        eventInfo = eventInfo,
//                        pagerState = pagerState,
//                        scope = scope
//                    )
                    FilterButtons(
                        onFilterButtonClick = {filterString: String ->
                            selectedFilterButton = filterString
                        }
                    )

                    when(selectedFilterButton) {
                        "Summary" -> {
//                            Column(
//                                horizontalAlignment = Alignment.Start,
//                                modifier = Modifier
//                                    .padding(paddings.spacingMedium)
//                            ) {
////                                Text(
////                                    text = eventInfo.title,
////                                    fontSize = 30.sp,
////                                    textAlign = TextAlign.Center,
////                                    overflow = TextOverflow.Ellipsis,
////                                    lineHeight = 35.sp,
////                                    modifier = Modifier
////                                        //.padding(7.dp)
////                                        //.fillMaxWidth()
////                                )
////                                Spacer(
////                                    modifier = Modifier
////                                        .padding(paddings.spacingXSmall)
////                                )
////
////                                Text(
////                                    text = eventInfo.county + " - " + eventInfo.city + ", " + eventInfo.country + " " +countryMap[eventInfo.country],
////                                    style = MaterialTheme.typography.titleMedium,
////                                    letterSpacing = 0.2.sp
////                                )
//                            }




                            EventDetails(
                                eventInfo = eventInfo,
                                modifier = Modifier,
                                onShowMoreTextClick = {
                                    showDetails = true

                                    scope.launch {
                                        sheetState.expand()
                                        sheetState.show()
                                    }.invokeOnCompletion {
                                        showDetails = true
                                    }
                                }
                            )
                        }
                        "Details" -> {
                            DetailsTab(
                                detailsString = eventInfo.details
                            )
                        }
                        "Results" -> {
                            Text("Results")
                        }
                    }
                }




            }

        if (showDetails) {

            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        showDetails = false
                    }
                },
                sheetState = sheetState,
                containerColor = Color.White,
                contentColor = Color.Black,
                dragHandle = {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = paddings.spacingSmall, top = paddings.spacingSmall)
                    ) {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    sheetState.hide()
                                }.invokeOnCompletion {
                                    showDetails = false
                                }
                            }
                        ){
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = ""
                            )
                        }
                    }
                },
                shape = shapes.small,
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                DetailsBottomSheet(
                    details = eventInfo.details
                )
            }
        }
    }
}

@Composable
fun DetailsTab(
    detailsString: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "General info",
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
            modifier = Modifier
                .padding(
                    start = paddings.spacingSmall,
                    top = paddings.spacingSmall
                )
        )
        Card(
            shape = CardDefaults.elevatedShape,
            elevation = CardDefaults.cardElevation(2.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = Color.White, contentColor = Color.Black),
            //border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            modifier = modifier
                .fillMaxWidth()
                .padding(start = paddings.spacingSmall, bottom = paddings.spacingSmall, end = paddings.spacingSmall)
                //.height(120.dp)
        ) {
            Text(
                text = detailsString,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(paddings.spacingSmall)
            )
        }
        Text(
            text = "Categories",
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
            modifier = Modifier
                .padding(
                    start = paddings.spacingSmall,
                    top = paddings.spacingSmall
                )
        )
        Card(
            shape = CardDefaults.elevatedShape,
            elevation = CardDefaults.cardElevation(3.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
            //border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            modifier = modifier
                .fillMaxWidth()
                .padding(start = paddings.spacingSmall, bottom = paddings.spacingSmall, end = paddings.spacingSmall)
            //.height(120.dp)
        ) {
            Text(
                text = detailsString,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(paddings.spacingSmall)
            )
        }
        Text(
            text = "Schedule",
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
            modifier = Modifier
                .padding(
                    start = paddings.spacingSmall,
                    top = paddings.spacingSmall
                )
        )
        Card(
            shape = CardDefaults.elevatedShape,
            elevation = CardDefaults.cardElevation(3.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
            //border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            modifier = modifier
                .fillMaxWidth()
                .padding(start = paddings.spacingSmall, bottom = paddings.spacingSmall, end = paddings.spacingSmall)
            //.height(120.dp)
        ) {
            Text(
                text = detailsString,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(paddings.spacingSmall)
            )
        }
    }
}

@Composable
fun FilterButtons(
    items: List<String> = listOf("Summary", "Track", "Results"),
    onFilterButtonClick: (String) -> Unit
) {
    var selectedItem by remember { mutableStateOf<String?>("Summary") }


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
                        selectedItem = item
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FilterPager(
    eventInfo: EventInfo,
    pagerState: PagerState,
    scope: CoroutineScope
) {

    val selectedTabIndex = remember { derivedStateOf { pagerState.currentPage } }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex.value,
            containerColor = Color.White,
            contentColor = Color.Black,
            modifier = Modifier.fillMaxWidth()
        ) {
            FilterEventTabs.entries.forEachIndexed { index, currentTab ->
                Tab(
                    selected = selectedTabIndex.value == index,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = Color.Gray,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(currentTab.ordinal)
                        }
                    },
                    text = { Text(text = currentTab.text) },

                )
            }
        }


//
//        when(selectedTabIndex.value) {
//            0 -> {
//                EventDetails(
//                    eventInfo = eventInfo,
//                    modifier = Modifier,
//                )
//                EventDetails(
//                    eventInfo = eventInfo,
//                    modifier = Modifier,
//                )
//                EventDetails(
//                    eventInfo = eventInfo,
//                    modifier = Modifier,
//                )
//            }
//            1 -> {
//                Text(
//                    text = eventInfo.details
//                )
//            }
//            2 -> {
//                Text("Results!")
//            }
//        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
               Text(
                   text = "yoooo"
               )
            }
        }
    }
}

enum class FilterEventTabs(
    val text: String
) {
    Summary("Summary"),
    Details("Details"),
    Results("Results");
}

@Composable
fun EventDetails(
    eventInfo: EventInfo,
    onShowMoreTextClick: () -> Unit,
    modifier: Modifier
) {
    Log.d("RecompositionDetails", "HorizontalPagerTabRowSample recomposed!")
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(paddings.spacingMedium)
    ) {

        Text(
            text = "Title",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                //.background(Color(0xFFEEEEEE))
                .padding(
                    start = paddings.spacingSmall,
                    //top = paddings.spacingSmall
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
            .offset(y = -paddings.spacingXSmall)
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = paddings.spacingSmall, end = paddings.spacingSmall)
                .fillMaxWidth()
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
        ) {

            Text(
                text = eventInfo.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.2.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = paddings.spacingXSmall)
            )
        }

        Spacer(
            modifier = Modifier
                .padding(paddings.spacingSmall)
        )

        Text(
            text = "Summary",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                //.background(Color(0xFFEEEEEE))
                .padding(
                    start = paddings.spacingSmall,
                    //top = paddings.spacingSmall
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
                //.offset(y = paddings.spacingXSmall)
        )

        Spacer(
            modifier = Modifier
                .padding(paddings.spacingXSmall)
        )

//            EventDetailsRowUpdated(
//                detailsIcon = R.drawable.outline_calendar_today_40,
//                bigText = "${eventInfo.startDate.dayOfMonth} - ${eventInfo.endDate.dayOfMonth} ${
//                    eventInfo.startDate.month.name.lowercase()
//                        .replaceFirstChar { it.uppercase() }
//                } ${eventInfo.startDate.year}",
//                showSmallText = false,
//                smallText = "Day x to Day y"
//            )
//            EventDetailsRowUpdated(
//                detailsIcon = R.drawable.outline_location_on_40,
//                iconEmoji = "📍",
//                bigText = eventInfo.county + ", " + eventInfo.city,
//                smallText = eventInfo.country + " " + countryMap[eventInfo.country]
//            )
       // First2DetailsRow(eventInfo)

        First2DetailsColumn(eventInfo)

        EventDetailsRowUpdated(
            detailsIcon = R.drawable.outline_person_40,
            isIcon = true,
            bigText = "Organizer Name",
            showSmallText = false,
            smallText = "Organizer",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier
                .padding(top = paddings.spacingSmall)
        )

        Text(
            text = "Details",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                //.background(Color(0xFFEEEEEE))
                .padding(
                    start = paddings.spacingSmall,
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
            .offset(y = paddings.spacingXSmall)
        )

        Text(
            text = eventInfo.details,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.2.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(paddings.spacingSmall)
                .align(Alignment.Start)
        )

        AnnotatedShowMoreText(
            onTextClick = {
                onShowMoreTextClick()
            },
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = paddings.spacingSmall)
        )
    }
}

@Composable
fun First2DetailsColumn(
    eventInfo: EventInfo
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()

    ) {
        EventDetailsRowUpdated(
            detailsIcon = R.drawable.outline_calendar_today_40,
            bigText = "${eventInfo.startDate.dayOfMonth} - ${eventInfo.endDate.dayOfMonth} ${
                eventInfo.startDate.month.name.lowercase()
                    .replaceFirstChar { it.uppercase() }} ${eventInfo.startDate.year} ",
            showSmallText = false,
            smallText =" ",
            modifier = Modifier
                //.weight(1f)
        )

//        Spacer(
//            modifier = Modifier
//                .padding(paddings.spacingXSmall)
//        )

        EventDetailsRowUpdated(
            detailsIcon = R.drawable.outline_location_on_40,
            iconEmoji = "📍",
            bigText = eventInfo.county + ", " + eventInfo.city,
            smallText = eventInfo.country + " " + countryMap[eventInfo.country],
            showSmallText = true,
            modifier = Modifier
                //.weight(1f)
        )

//        EventDetailsRowUpdated(
//            detailsIcon = R.drawable.outline_person_40,
//            bigText = "Organizer Name",
//            showSmallText = false,
//            smallText = "Organizer"
//        )
    }
}


@Composable
fun First2DetailsRow(
    eventInfo: EventInfo
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()

    ) {
        EventDetailsRowUpdated(
            detailsIcon = R.drawable.outline_calendar_today_40,
            bigText = "${eventInfo.startDate.dayOfMonth} - ${eventInfo.endDate.dayOfMonth} ${
                eventInfo.startDate.month.name.lowercase()
                    .replaceFirstChar { it.uppercase() }} ",
            showSmallText = true,
            smallText =" ${eventInfo.startDate.year}",
            modifier = Modifier
                .weight(1f)
        )

        Spacer(
            modifier = Modifier
                .padding(paddings.spacingXSmall)
        )

        EventDetailsRowUpdated(
            detailsIcon = R.drawable.outline_location_on_40,
            iconEmoji = "📍",
            bigText = eventInfo.county + ", " + eventInfo.city,
            smallText = eventInfo.country + " " + countryMap[eventInfo.country],
            modifier = Modifier
                .weight(1f)
        )

//        EventDetailsRowUpdated(
//            detailsIcon = R.drawable.outline_person_40,
//            bigText = "Organizer Name",
//            showSmallText = false,
//            smallText = "Organizer"
//        )
    }
}

@Composable
fun AnnotatedShowMoreText(
    onTextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val annotatedLinkString: AnnotatedString = buildAnnotatedString {
        val str = "... Show more >"
        val startIndex = str.indexOf("Show")
        val endIndex = str.indexOf("more") + "more".length
        append(str)

        addStyle(
            style = SpanStyle(
                color = Color.Black,
                fontStyle = MaterialTheme.typography.bodyLarge.fontStyle, //gabaritoMediumBoldTextStyle
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.2.sp
            ), start = 0, end = str.length
        )

        addStyle(
            style = SpanStyle(
                color = Color.Black,
                fontStyle = MaterialTheme.typography.bodyLarge.fontStyle, //gabaritoMediumBoldTextStyle
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.2.sp,
                textDecoration = TextDecoration.Underline
            ), start = startIndex, end = endIndex
        )
        addStyle(
            style = SpanStyle(
                color = Color.Black,
                fontStyle = MaterialTheme.typography.bodyLarge.fontStyle, //gabaritoMediumBoldTextStyle
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.2.sp,
            ), start = str.indexOf(">"), end = str.length
        )

        addStringAnnotation(
            tag = "Show more",
            annotation = "Show more",
            start = startIndex,
            end = endIndex)
    }

    ClickableText(
        text = annotatedLinkString,
        onClick = {
            annotatedLinkString
                .getStringAnnotations("Show more", it, it)
                .firstOrNull()?.let { stringAnnotation ->
                    //Function call or just function reference? -> onSignUpClick
                    onTextClick()
                }
        },
        modifier = modifier
            .fillMaxWidth()
    )
}

@Composable
fun DetailsBottomSheet(
    details: String
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.95f)
            .padding(paddings.spacingMedium)
    ) {
        item {
            Text(
                text = "Event details \uD83D\uDCCB",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.2.sp,
                modifier = Modifier
                    .padding(start = paddings.spacingSmall, bottom = paddings.spacingSmall)
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
            )

            Text(
                text = details,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.24.sp,
                modifier = Modifier
                    .padding(paddings.spacingSmall)
            )
        }
    }
}

@Composable
fun EventDetailsRowUpdated(
    detailsIcon: Int,
    isIcon: Boolean = false,
    iconEmoji: String =  "📅",
    bigText: String,
    smallText: String,
    showSmallText: Boolean = true,
    modifier: Modifier = Modifier
) {
    Card(
        shape = CardDefaults.elevatedShape,
        elevation = CardDefaults.cardElevation(1.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White, contentColor = Color.Black),
        modifier = modifier
            .padding(bottom = paddings.spacingSmall)
            //.border(0.5.dp, MaterialTheme.colorScheme.primary, shapes.small)
        ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            if(isIcon) {
                AsyncImage(
                    model = "",
                    contentDescription = "Profile Picture",
                    placeholder = painterResource(R.drawable.default_profile),
                    error = painterResource(R.drawable.ic_launcher_foreground),
                    fallback = painterResource(R.drawable.default_profile),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(start = paddings.spacingMedium)
                        .size(40.dp) // Adjust size as needed
                        .clip(CircleShape)
                        .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                )
            }
            else {
                Text(
                    text = iconEmoji,
                    fontSize = 25.sp,
                    modifier = Modifier
                        .padding(start = paddings.spacingMedium)
                        .weight(0.2f)// Use sp for text size
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(start = paddings.spacingSmall)
                    .weight(3f)
            ) {
                Text(
                    text = bigText,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Normal,
                    //fontSize = 20.sp,
                    modifier = Modifier
                        .padding(bottom = paddings.spacingXSmall)
                        .offset(if(isIcon) -paddings.spacingMedium else 0.dp)
                )
                if (showSmallText) {
                    Text(
                        text = smallText,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}


@Composable
fun EventScreenCollapsing(
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val imageHeight = screenHeight * 0.30f
    val maxOffsetPx = with(LocalDensity.current) { imageHeight.toPx() }
    val scrollState = rememberLazyListState()

    // Get scroll offset for fade effect
    val scrollOffset = minOf(
        0f,
        maxOffsetPx
    )
    val progress = (scrollOffset / maxOffsetPx).coerceIn(0f, 1f)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddings.spacingMedium)
    ) {
        // Main scrollable content
        LazyColumn(state = scrollState) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.default_background),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight)
                        .graphicsLayer { alpha = 1f - progress }, // fade out
                    contentScale = ContentScale.Crop
                )
            }

            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Event Title", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("This is a detailed description of the event...")
                    Spacer(modifier = Modifier.height(1000.dp)) // Fake content
                }
            }
        }

        // Top Bar (fades in as image scrolls away)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    Color.White.copy(alpha = progress),
                )
                .zIndex(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Card(
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.size(40.dp)
                ) {
                    IconButton(onClick = onBackClick, modifier = Modifier.fillMaxSize()) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }

                Card(
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.size(40.dp)
                ) {
                    IconButton(onClick = onFavoriteClick, modifier = Modifier.fillMaxSize()) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite", tint = Color.Red)
                    }
                }
            }
        }

        // Bottom bar pinned
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.BottomCenter)
                .background(Color.LightGray)
                .zIndex(1f),
            contentAlignment = Alignment.Center
        ) {
            Text("Buy Ticket", fontWeight = FontWeight.Bold)
        }
    }
}



@Preview
@Composable
fun EventScreenPreview() {
    EventScreen2(
        athleteInfo = testAthlete.copy(athleteId = "not 1"),
        eventInfo = testEvent,
        onBackClick = {},
        onFavoriteClick = {},
        onShowMoreTextClick = {},
        isFavorite = true
    )
}
