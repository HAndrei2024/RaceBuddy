package com.example.racebuddy.ui.v2.organizer.event

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.racebuddy.R
import com.example.racebuddy.data.database.EventInfo
import com.example.racebuddy.data.database.OrganizerInfo
import com.example.racebuddy.data.database.ResultAthleteInfo
import com.example.racebuddy.ui.theme.heights
import com.example.racebuddy.ui.theme.paddings
import com.example.racebuddy.ui.theme.shapes
import com.example.racebuddy.ui.v2.event.DragHandleWithIconOnRight
import com.example.racebuddy.ui.v2.event.EventDetails
import com.example.racebuddy.ui.v2.event.EventScreenBottomBar
import com.example.racebuddy.ui.v2.event.EventScreenTopBar
import com.example.racebuddy.ui.v2.event.FilterButtons
import com.example.racebuddy.ui.v2.event.FilterResultsButtons
import com.example.racebuddy.ui.v2.event.ParticipantRow
import com.example.racebuddy.ui.v2.event.ParticipantsHeader
import com.example.racebuddy.ui.v2.event.ParticipantsScreen
import com.example.racebuddy.ui.v2.event.ResultRow
import com.example.racebuddy.ui.v2.event.ResultsBottomSheet
import com.example.racebuddy.ui.v2.event.ResultsByGender
import com.example.racebuddy.ui.v2.main.countryMap
import com.example.racebuddy.ui.v2.organizer.common.OrganizerBottomAppBar
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.utils.now

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganizerEventScreen(
    eventInfo: EventInfo,
    organizerInfo: OrganizerInfo,
    categories: List<String>,
    resultAthleteInfoList: List<ResultAthleteInfo>,
    onConfirmClick: (athleteUuid: String, value: Boolean) -> Unit,
    onBackClick: () -> Unit,
    onFilterButtonClick: (String) -> Unit,
    onUpdateResultsClick: () -> Unit,
    isInFuture: Boolean
){
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val backgroundImageHeight = screenHeight * 0.30f

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
            if(selectedFilterButton == "Results") {
                EventScreenBottomBar(
                    isAthleteRegistered = false,
                    buttonText = "Update Results",
                    onClick = onUpdateResultsClick
                )
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
                BackgroundImageWithIntegratedTopBar(
                    pictureUrl = eventInfo.backgroundPictureUrl.takeIf { it.isNotBlank() },
                    imageHeight = backgroundImageHeight,
                    onBackClick = onBackClick
                )
            }

            item {
                FilterButtons(
                    items = if(eventInfo.startDate > LocalDate.now()) listOf("Summary", "Participants", "Predictions") else listOf("Summary", "Results"),
                    onFilterButtonClick = { filterString: String ->
                        selectedFilterButton = filterString
                    }
                )
            }

            item {
                when(selectedFilterButton) {
                    "Summary" -> {
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

                    "Participants" -> {
                        //Add possibility to confirm and estimation
                        FilterResultsButtons(
                            items = listOf("General") + categories,
                            onFilterButtonClick = onFilterButtonClick
                        )


                        OrganizerParticipantsScreen(
                            participants = resultAthleteInfoList,
                            onConfirmClick = onConfirmClick
                        )
                    }

                    "Results" -> {
                        if(resultAthleteInfoList.isEmpty()) {

                        }
                        else {
                            ResultsBlock(
                                resultAthleteInfoList = resultAthleteInfoList,
                                categories = categories,
                                onFilterResultsButtonClick = onFilterButtonClick
                            )
                        }
                    }

                    "Predictions" -> {
                        
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsBlock(
    resultAthleteInfoList: List<ResultAthleteInfo>,
    categories: List<String>,
    onFilterResultsButtonClick: (String) -> Unit,
) {
    var genderResultSheet by remember { mutableStateOf("Male") }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()
    var showResults by remember { mutableStateOf(false) }



    FilterResultsButtons(
        items = listOf("General") + categories,
        onFilterButtonClick = onFilterResultsButtonClick
    )

    ResultsByGender(
        gender = "Women",
        resultAthleteInfoList = resultAthleteInfoList.filter { it -> it.gender == "Female" }.sortedBy { it.rank },
        onClick = {
            genderResultSheet = "Female"

            scope.launch {
                sheetState.expand()
                sheetState.show()
            }.invokeOnCompletion {
                showResults = true
            }
        }
    )

//                            Spacer(modifier = Modifier
//                                .padding(paddings.spacingSmall))

    ResultsByGender(
        gender = "Men",
        resultAthleteInfoList = resultAthleteInfoList.filter { it -> it.gender == "Male" }.sortedBy { it.rank },
        onClick = {
            // Update the list of what results should be displayed
            // The screen composable should take this lists as parameters
            // switch on showResults
            genderResultSheet = "Male"

            scope.launch {
                sheetState.expand()
                sheetState.show()
            }.invokeOnCompletion {
                showResults = true
            }
        }
    )

    if(showResults) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    showResults = false
                }
            },
            sheetState = sheetState,
            containerColor = Color.White,
            contentColor = Color.Black,
            dragHandle = {
                DragHandleWithIconOnRight(
                    iconImageVector = Icons.Filled.Close,
                    onIconClick = {
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            showResults = false
                        }
                    }
                )
            },
            shape = shapes.small,
            modifier = Modifier
                .fillMaxWidth()

        ) {
            ResultsBottomSheet(
                resultAthleteInfoList = resultAthleteInfoList.filter { it.gender == genderResultSheet }
            )
        }
    }

}

@Composable
fun BackgroundImageWithIntegratedTopBar(
    pictureUrl: String?,
    imageHeight: Dp,
    onBackClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(top = paddings.spacingMedium)
    ) {
        EventScreenTopBar(
            isUserLoggedIn = false,
            isFavorite = false,
            onBackClick = onBackClick,
            onFavoriteClick = { },
            modifier = Modifier.zIndex(1f)
        )
        AsyncImage(
            model = pictureUrl,
            contentDescription = "Background Picture",
            placeholder = painterResource(R.drawable.default_background),
            error = painterResource(R.drawable.default_background),
            fallback = painterResource(R.drawable.default_background),
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun OrganizerParticipantsScreen(
    participants: List<ResultAthleteInfo>,
    onConfirmClick: (athleteUuid: String, value: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(paddings.spacingSmall)
            .shadow(3.dp, shapes.small) // Shadow with rounded corners
            .background(Color.White, shapes.small) // Background is required//
    ) {

        if(participants.isNotEmpty()) {
            val categories = participants.map { it.category }.distinct()

            categories.forEach { category ->
                RegsitrationsByCategory(
                    category = category,
                    resultAthleteInfoList = participants.filter { it.category == category },
                    onCardClick = {},
                    onConfirmClick = onConfirmClick,
                    modifier = Modifier
                )
            }
        }
        else {
            Text(
                text = "There are no athletes registered yet!",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddings.spacingMedium)
            )
        }
    }
}

@Composable
fun OrganizerParticipantRow(
    athleteUuid: String,
    fullName: String,
    profilePicUrl: String,
    country: String,
    confirmed: Boolean,
    onConfirmClick: (athleteUuid: String, value: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    // Profile image, Full name, status

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    top = paddings.spacingMedium,
                    bottom = paddings.spacingMedium,
                    start = paddings.spacingSmall,
                    end = paddings.spacingSmall
                )
        ) {

            Row(
                horizontalArrangement = Arrangement.spacedBy(paddings.spacingSmall),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(2f)
            ) {
                AsyncImage(
                    model = profilePicUrl,
                    contentDescription = "",
                    placeholder = painterResource(R.drawable.default_profile),
                    error = painterResource(R.drawable.ic_launcher_foreground),
                    fallback = painterResource(R.drawable.default_profile),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(end = paddings.spacingSmall)
                        .size(35.dp) // Adjust size as needed
                        .clip(CircleShape)
                        .border(0.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                )

                Text(
                    text = fullName,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )

            }

            Text(
                text = countryMap.get(country) ?: "",
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
            )

            ConfirmationButton(
                confirmed = confirmed,
                onConfirm = onConfirmClick,
                athleteUuid = athleteUuid,
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}

@Composable
fun ConfirmationButton(
    confirmed: Boolean,
    athleteUuid: String,
    onConfirm: (athleteUuid: String, value: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {
                if (!confirmed) onConfirm(athleteUuid, true)
                Log.d("OrganizerEvent UI", "Confirmation Button pressed!")
        }, // prevent double confirmation
        modifier = modifier
            .defaultMinSize(minWidth = 1.dp) // Optional: makes the button less wide
            .padding(horizontal = 4.dp)
    ) {
        Text(
            text = if (confirmed) "✅ Confirmed" else "Confirm ⏳",
            maxLines = 1
        )
    }
}


@Composable
fun RegsitrationsByCategory(
    category: String,
    resultAthleteInfoList: List<ResultAthleteInfo>,
    onCardClick: (gender: String) -> Unit,
    onConfirmClick: (athleteUuid: String, value: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(paddings.spacingSmall)
            .shadow(3.dp, shapes.small) // Shadow with rounded corners
            .background(Color.White, shapes.small) // Background is required//
            .clickable {
                onCardClick(category)
            }
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(
                    paddings.spacingSmall

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

        if(resultAthleteInfoList.isNotEmpty()) {
            resultAthleteInfoList.forEachIndexed { index, resultAthleteInfo ->
                OrganizerParticipantRow(
                    athleteUuid = resultAthleteInfo.athleteUuid,
                    fullName = "${resultAthleteInfo.firstName} ${resultAthleteInfo.lastName}",
                    profilePicUrl = resultAthleteInfo.profilePictureUrl ?: "",
                    country = resultAthleteInfo.country,
                    confirmed = resultAthleteInfo.confirmed,
                    onConfirmClick = onConfirmClick,
                    modifier = Modifier
                )
            }
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
                    text = "There are no athletes registered...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}


