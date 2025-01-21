package com.example.racebuddy.ui.event

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.racebuddy.R
import com.example.racebuddy.data.database.Event
import com.example.racebuddy.ui.login.UserButton
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun EventScreen(
    eventScreenViewModel: EventScreenViewModel,
    event: Event,
    onFavoriteIconClick: () -> Unit,
    onNavigateBackIconClick: () -> Unit,
    modifier: Modifier
) {
    val athleteId = eventScreenViewModel.athleteId.collectAsState().value
    val isFavoriteIconEnabledBoolean = athleteId > 0
    eventScreenViewModel.checkFavoriteEvent(athleteId, event.id)
    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        item {
            val isFavorite = eventScreenViewModel.isFavorite.collectAsState().value
            IconsRow(
                onFavoriteIconClick = {
                    eventScreenViewModel.onFavoriteIconClick(
                        athleteId = athleteId,
                        eventId = event.id,
                        newFavoriteValue = !isFavorite
                        )
                    onFavoriteIconClick()
                                      },
                isFavoriteIconEnabled = isFavoriteIconEnabledBoolean,
                isFavorite = isFavorite,
                onNavigateBackIconClick = onNavigateBackIconClick,
                modifier = Modifier
            )
        }

        item {
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier.fillMaxWidth()
            ) {
                BackgroundImageCard(modifier = Modifier.fillMaxWidth())
                RegisterButton(
                    text = "Register",
                    onClick = {

                    },
                    modifier = Modifier.offset(y = 4.dp)
                )
            }
        }

        item {
            EventDetails(
                event = event,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            //val events = eventScreenViewModel.getAllEventResults(event.id).collectAsState().value
            val uiState by eventScreenViewModel.uiState.collectAsState()
            val events = uiState.eventResults
            HorizontalPagerTabRowSample(
                description = event.description,
                eventResults = events
            )
        }
    }
}

@Composable
fun EventDetails(
    event: Event,
    modifier: Modifier
) {
    Log.d("RecompositionDetails", "HorizontalPagerTabRowSample recomposed!")
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            text = event.title,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 35.sp,
            modifier = Modifier
                .padding(7.dp)
        )
        EventDetailsRow(
            detailsIcon = R.drawable.outline_calendar_today_40,
            bigText = event.startDateString + "-" + event.endDateString,
            smallText = "Day x to Day y"
        )
        EventDetailsRow(
            detailsIcon = R.drawable.outline_location_on_40,
            bigText = event.county + " " + event.country, //TODO implement event details
            smallText = event.city + " " + event.locationName
        )
        EventDetailsRow(
            detailsIcon = R.drawable.outline_person_40,
            bigText = event.organizerName,
            smallText = "Organizer"
        )
    }
}

@Composable
fun EventDetailsRow(
    detailsIcon: Int,
    bigText: String,
    smallText: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Image(
            painter = painterResource(detailsIcon),
            contentDescription = "",
            modifier = Modifier
                .weight(1.5f)
        )
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = modifier
                .padding(5.dp)
                .weight(3f)
        ) {
            Text(
                text = bigText,
                fontSize = 20.sp
            )
            Text(
                text = smallText,
                fontSize = 15.sp
            )
        }
    }
}


@Composable
fun IconsRow(
    navigateBackIcon: Int = R.drawable.baseline_arrow_back_ios_24,
    onNavigateBackIconClick: () -> Unit,
    onFavoriteIconClick: () -> Unit,
    favoriteIcon: Int = R.drawable.baseline_favorite_24,
    isFavoriteIconEnabled: Boolean,
    isFavorite: Boolean,
    unfavoriteIcon: Int = R.drawable.baseline_favorite_border_24,
    modifier: Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = 25.dp)
    ) {
        IconButton(
            onClick = onNavigateBackIconClick
        ) {
            Image(
                painter = painterResource(navigateBackIcon),
                contentDescription = ""
            )
        }
        IconButton(
            onClick = onFavoriteIconClick,
            enabled = isFavoriteIconEnabled,
            modifier = Modifier.alpha(if (isFavoriteIconEnabled) 1f else 0f)
        ) {
            val painter = if(isFavorite) favoriteIcon else unfavoriteIcon
            Image(
                painter = painterResource(painter),
                contentDescription = ""
            )
        }
    }
}

@Composable
fun RegisterButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier
) {
   Button(
       onClick = onClick,
       modifier = modifier
   ) {
       Text(
           text = text
       )
   }
}

@Composable
fun BackgroundImageCard(
    image: Int = R.drawable.default_profile,
    modifier: Modifier
) {
    Card(
        modifier = modifier
            .padding(20.dp)
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = "",
            modifier = Modifier
        )
//        Image(
//            painter = painterResource(id = image),
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                // Workaround to enable alpha compositing
//                .graphicsLayer { alpha = 0.99f }
//                .drawWithContent {
//                    drawContent()  // Draw the image first
//
//                    // Create a gradient that fades from opaque to fully transparent
//                    val colors = listOf(
//                        Color.Black,        // End with the solid color (black in this case)
//                        Color.Transparent, // Start with transparency
//                    )
//
//                    drawRect(
//                        brush = Brush.verticalGradient(
//                            colors = colors,
//                            startY = size.height * 0.1f,  // Adjust this to control where transparency starts
//                            endY = size.height              // The bottom should be fully transparent
//                        ),
//                        blendMode = BlendMode.DstIn  // Apply transparency to the bottom of the image
//                    )
//                }
//        )

    }
}

@Preview
@Composable
fun EventDetailRowPreview() {
    EventDetailsRow(
        detailsIcon = R.drawable.baseline_calendar_today_24,
        bigText = "Date and other",
        smallText = "Location and other"
    )
}

//@Preview
//@Composable
//fun EventScreenPreview() {
//    EventScreen(
//        modifier = Modifier,
//        onNavigateBackIconClick = {},
//        onFavoriteIconClick = {}
//    )
//}



