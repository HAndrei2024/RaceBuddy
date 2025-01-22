package com.example.racebuddy.ui.profile

import android.widget.Button
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.racebuddy.R
import com.example.racebuddy.data.database.Athlete
import com.example.racebuddy.data.database.Result
import com.example.racebuddy.ui.common.BottomAppBar
import com.example.racebuddy.ui.common.LoginTopAppBar

@Composable
fun ProfileScreen(
    athlete: Athlete,
    athleteResults: List<Result>,
    onHomeClick: () -> Unit = { },
    onFavoriteClick: () -> Unit = { },
    onProfileClick: () -> Unit = { },
    isHomeSelected: Boolean = false,
    isFavoriteSelected: Boolean = false,
    isProfileSelected: Boolean = true,
    onLoginButtonClick: () -> Unit,
    onLogoutButtonClick: () -> Unit
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
                    //.weight(0.3f)
                ) {

                }
                ImageAndNameRow(
                    name = athlete.name,
                    surname = athlete.surname,
                    modifier = Modifier
                        .padding(10.dp)
                    //.weight(2f)
                )
                DetailsCard(
                    modifier = Modifier
                        .padding(5.dp)
                    //.weight(15f)
                )
                ResultsCard(
                    modifier = Modifier
                        .padding(5.dp),
                    results = athleteResults
                )
                LogoutButton(
                    onLogoutClick = onLogoutButtonClick
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
    modifier: Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .weight(2f)
        ) {
            Image(
                painter = painterResource(R.drawable.default_profile),
                contentDescription = "Profile Picture"
            )
        }
        Text(
            text = "$name $surname",
            modifier = Modifier
                .padding(10.dp)
                .weight(3f)
        )
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
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "Age",
                    modifier = Modifier
                        .padding(10.dp)
                )
                Text(
                    text = "Nationality",
                    modifier = Modifier
                        .padding(10.dp)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "Team",
                    modifier = Modifier
                        .padding(10.dp)
                )
                Text(
                    text = "Sponsors",
                    modifier = Modifier
                        .padding(10.dp)
                )
            }
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
fun LogoutButton(
    buttonText: String = "Log Out",
    onLogoutClick: () -> Unit
) {
    Button(
        onClick = onLogoutClick
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
        athlete = Athlete(0, "test", "test", "test", "test"),
        athleteResults = emptyList(),
        onLoginButtonClick = {},
        onLogoutButtonClick = {}
    )
}