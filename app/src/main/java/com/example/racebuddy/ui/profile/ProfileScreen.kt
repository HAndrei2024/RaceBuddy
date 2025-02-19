package com.example.racebuddy.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Button
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.racebuddy.BuildConfig
import com.example.racebuddy.R
import com.example.racebuddy.data.database.Athlete
import com.example.racebuddy.data.database.Result
import com.example.racebuddy.ui.common.BottomAppBar
import com.example.racebuddy.ui.common.LoginTopAppBar
import com.example.racebuddy.ui.main.MainScreenViewModel

@Composable
fun ProfileScreen(
    profileScreenViewModel: ProfileScreenViewModel,
    athlete: Athlete,
    athleteResults: List<Result>,
    onHomeClick: () -> Unit = { },
    onFavoriteClick: () -> Unit = { },
    onProfileClick: () -> Unit = { },
    isHomeSelected: Boolean = false,
    isFavoriteSelected: Boolean = false,
    isProfileSelected: Boolean = true,
    onLoginButtonClick: () -> Unit,
    onLogoutButtonClick: () -> Unit,
    onStravaButtonClick: () -> Unit
) {
    Scaffold(
        topBar = { LoginTopAppBar("RaceBuddy") },
        bottomBar = { BottomAppBar(
            onHomeClick = onHomeClick,
            onFavoriteClick = onFavoriteClick,
            onProfileClick = onProfileClick,
            isHomeSelected = isHomeSelected,
            isFavoriteSelected = isFavoriteSelected,
            isProfileSelected = isProfileSelected,
        ) }
    ) { innerPadding ->
        if(athlete.id > 0) {
            val activity = LocalContext.current as Activity
            val intent = activity.intent
            val responseCode = intent.data?.getQueryParameter("code").toString()
            profileScreenViewModel.updateResponseCode(responseCode)

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .background(color = Color.Blue)
                ) {

                }
                ImageAndNameRow(
                    name = athlete.name,
                    surname = athlete.surname,
                    profilePictureUrl = athlete.profilePictureUrl,
                    modifier = Modifier
                        .padding(10.dp)
                )
                DetailsCard(
                    modifier = Modifier
                        .padding(5.dp)
                )
                ResultsCard(
                    modifier = Modifier
                        .padding(5.dp),
                    results = athleteResults
                )
                ProfileButton(
                    onClick = onLogoutButtonClick
                )
                ProfileButton(
                    buttonText = "Get Profile from Strava",
                    onClick = { onStravaButtonClick() }
                )
            }
        }
        else {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text("There is no user logged in!")
                Button(
                    onClick = onLoginButtonClick
                ) {
                    Text("Log In")
                }
            }
        }
    }

}

@Composable
fun ImageAndNameRow(
    name: String,
    surname: String,
    profilePictureUrl: String,
    modifier: Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Card(
            elevation = CardDefaults.cardElevation(5.dp),
            modifier = Modifier
                .weight(2f)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(profilePictureUrl)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.error_image_generic),
                placeholder = painterResource(R.drawable.default_profile),
                contentDescription = "Profile picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .heightIn(min = 150.dp, max = 150.dp)
            )
        }
        Card(
            elevation = CardDefaults.cardElevation(5.dp),
            modifier = Modifier
                .weight(3f)
                .padding(horizontal = 10.dp)
                .heightIn(min = 150.dp, max = 150.dp)
        ) {
            Column() {
                Text(
                    text = "$name $surname",
                    modifier = Modifier
                        .padding(10.dp)
                    //.weight(3f)
                )
                Text(
                    text = "Age: ",
                    modifier = Modifier
                        .padding(10.dp)
                    //.weight(3f)
                )
                Text(
                    text = "Nationality: ",
                    modifier = Modifier
                        .padding(10.dp)
                    //.weight(3f)
                )
            }
        }
    }
}

@Composable
fun DetailsCard(
    modifier: Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(
                text = "Team",
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f)
            )
            Text(
                text = "Sponsors",
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f)
            )
        }
    }
}

@Composable
fun ResultsCard(
    modifier: Modifier,
    results: List<Result> = emptyList()
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Column(

            ) {
                Card() {
                    Text(
                        text = "Results",
                        modifier = Modifier
                        //.fillMaxWidth()
                    )

                }
                TableHeader()
            }
        }
        LazyColumn {
            items(results) { result ->
                if(result.time != "-")
                ResultCard(
                    result = result,
                    modifier = Modifier
                        .padding(10.dp)
                )
            }
        }
    }
}

@Composable
fun TableHeader(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Athlete Id"
            )
            Text(
                text = "Event Id"
            )
            Text(
                text = "Time"
            )
        }
    }
}

@Composable
fun ResultCard(
    result: Result,
    modifier: Modifier
) {
    Card(
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "${result.eventId}"
            )
            Text(
                text = "${result.athleteId}"
            )
            Text(
                text = result.time
            )
        }
    }
}

@Composable
fun ProfileButton(
    buttonText: String = "Log Out",
    onClick: () -> Unit
) {
    Button(
        onClick = onClick
    ) {
        Text(
            text = buttonText
        )
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        profileScreenViewModel = viewModel(
            factory = ProfileScreenViewModel.factory
        ),
        athlete = Athlete(0, "test", "test", "test", "test"),
        athleteResults = emptyList(),
        onLoginButtonClick = {},
        onLogoutButtonClick = {},
        onStravaButtonClick = {}
    )
}