package com.example.racebuddy.ui.v2.organizer.profile


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.racebuddy.R
import com.example.racebuddy.data.database.AthleteInfo
import com.example.racebuddy.data.database.OrganizerInfo
import com.example.racebuddy.data.database.defaultOrganizer
import com.example.racebuddy.ui.theme.StravaOrange
import com.example.racebuddy.ui.theme.paddings
import com.example.racebuddy.ui.theme.shapes
import com.example.racebuddy.ui.v2.common.BottomNavigationBarChat
import com.example.racebuddy.ui.v2.common.LoadingAnimation
import com.example.racebuddy.ui.v2.common.MainScreenTopAppBar
import com.example.racebuddy.ui.v2.event.DetailRow
import com.example.racebuddy.ui.v2.main.countries
import com.example.racebuddy.ui.v2.main.countryMap
import com.example.racebuddy.ui.v2.profile.AthleteDetailsColumn
import com.example.racebuddy.ui.v2.profile.DetailRow
import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.utils.now


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganizerProfileScreen(
    organizer: OrganizerInfo,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onBottomBarIconClick: () -> Unit,
    onSettingsIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedBlur by animateDpAsState(targetValue = if(isRefreshing) 1.dp else 0.dp)

    Scaffold(
        topBar = {
            MainScreenTopAppBar(
                onSearchIconClick = {},
                onSettingsIconClick = onSettingsIconClick,
                showSearchIcon = false
            )
        },
        bottomBar = {
            BottomNavigationBarChat(
                selectedItem = 1,
                onItemSelected = { onBottomBarIconClick() },
                isFavoritesVisible = false
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
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isRefreshing) {
                LoadingAnimation(
                    modifier = Modifier
                        .zIndex(2f)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .blur(
                        radius = animatedBlur,
                        edgeTreatment = BlurredEdgeTreatment.Unbounded
                    )
                    //.zIndex(1f)
                    .fillMaxSize()
            ) {
//                UserProfileCardNoElevation(
//                    profileImage = painterResource(R.drawable.default_profile),
//                    name = organizer.name,
//                    country = organizer.country + countryMap[organizer.country],
//                    adminFirstName = organizer.administratorFirstName,
//                    adminLastName = organizer.administratorLastName
//                )
                OrganizerDetails(
                    organizerInfo = organizer,
                    onLogoutButtonClick = {},
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun UserProfileCard(
    profileImage: Painter,
    name: String,
    country: String,
    adminFirstName: String,
    adminLastName: String
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        //elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Profile Image
            Image(
                painter = profileImage,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Info Rows
            InfoRow(label = "Name", value = name)
            InfoRow(label = "Country", value = country)
            InfoRow(label = "Administrator First Name", value = adminFirstName)
            InfoRow(label = "Administrator Last Name", value = adminLastName)
        }
    }
}

@Composable
fun UserProfileCardNoElevation(
    profileImage: Painter,
    name: String,
    country: String,
    adminFirstName: String,
    adminLastName: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White, contentColor = Color.Black),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // No elevation
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ElevatedImage(profileImage)

            ElevatedInfoRow(label = "Name", value = name)
            ElevatedInfoRow(label = "Country", value = country)
            ElevatedInfoRow(label = "Administrator First Name", value = adminFirstName)
            ElevatedInfoRow(label = "Administrator Last Name", value = adminLastName)
        }
    }
}

@Composable
fun ElevatedImage(profileImage: Painter) {
    Surface(
        tonalElevation = 4.dp,
        shape = CircleShape,
        shadowElevation = 6.dp
    ) {
        Image(
            painter = profileImage,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun ElevatedInfoRow(label: String, value: String) {
    Surface(
        tonalElevation = 2.dp,
        shadowElevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        contentColor = Color.Black,
        border = BorderStroke((0.2).dp, MaterialTheme.colorScheme.primary),
        modifier = Modifier.fillMaxWidth()
        ) {
        Row(
            modifier = Modifier
                .padding(paddings.spacingMedium),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$label: ",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}



@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .padding(paddings.spacingMedium)
            .fillMaxWidth()
            .background(Color.White),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}



@Composable
fun OrganizerDetails(
    organizerInfo: OrganizerInfo,
    onLogoutButtonClick: () -> Unit,
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

//        Row(
//            modifier = modifier
//                .fillMaxWidth()
//                .height(IntrinsicSize.Min)
//        ) {

//            Column(
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
                AsyncImage(
                    model = "",
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

                OrganizerDetailsColumn(
                    organizerInfo = organizerInfo,
                    areDetailsFilled = organizerInfo.name != "-",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(2.dp),
                    shape = RoundedCornerShape(shapes.small.topEnd),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    onClick = onLogoutButtonClick,
                    modifier = Modifier
                        .scale(0.8f)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "Logout",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                        //.padding(paddings.spacingXSmall)
                    )
                //}
                //}


        }
    }
}

@Composable
fun OrganizerDetailsColumn(
    organizerInfo: OrganizerInfo,
    areDetailsFilled: Boolean,
    modifier: Modifier = Modifier
) {
//    val today = LocalDate.now()
//    var age = today.year - organizerInfo.birthdate.year
//    if (
//        today.monthNumber < athleteInfo.birthdate.monthNumber ||
//        (today.monthNumber == athleteInfo.birthdate.monthNumber && today.dayOfMonth < athleteInfo.birthdate.dayOfMonth)
//    ) {
//        age -= 1
//    }

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
            field = "Name",
            value = if(!areDetailsFilled) "-" else "${organizerInfo.name}",
            maxLines = 2,
            modifier = Modifier
        )

        DetailRow(
            field = "Administrator",
            value = if(!areDetailsFilled) "-" else "${organizerInfo.administratorFirstName} ${organizerInfo.administratorLastName}",
            modifier = Modifier
        )

        DetailRow(
            field = "Country",
            value = if(!areDetailsFilled) "-" else "${organizerInfo.country} ${countryMap.get(organizerInfo.country)}",
            maxLines = 2,
            modifier = Modifier
        )

    }
}



@Preview
@Composable
fun OgranizerProfileScreenPreview() {
    OrganizerProfileScreen(
        organizer = defaultOrganizer,
        isRefreshing = false,
        onRefresh = {},
        onBottomBarIconClick = {},
        onSettingsIconClick = {},
        modifier = Modifier
    )
}

