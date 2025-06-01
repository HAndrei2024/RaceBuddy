package com.example.racebuddy.ui.v2.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.racebuddy.data.database.AthleteInfo
import com.example.racebuddy.data.database.testAthlete
import com.example.racebuddy.ui.v2.common.BottomNavigationBarChat
import com.example.racebuddy.ui.v2.common.MainScreenTopAppBar

@Composable
fun ProfileScreen(
    athleteInfo: AthleteInfo,
    isUserLoggedIn: Boolean,
    onLogoutButtonClick: () -> Unit,
    onLoginButtonClick: () -> Unit,
    onBottomBarIconClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            MainScreenTopAppBar(
                onSettingsIconClick = {},
                onSearchIconClick = {},
                showSearchIcon = false
            )

        },
        bottomBar = {
            //BottomAppBarUpdated()
            BottomNavigationBarChat(
                selectedItem = 1,
                onItemSelected = { int: Int -> onBottomBarIconClick(int) },
            )
        },
        containerColor = Color.White,
        contentColor = Color.Black
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(innerPadding)
        ) {
            if(isUserLoggedIn) {
                LogoutButton(
                    onClick = onLogoutButtonClick
                )
            }
            else {
                LoginButton(
                    onClick = onLoginButtonClick
                )
            }
        }
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
fun ProfileScreenPreview() {
    ProfileScreen(
        athleteInfo = testAthlete,
        onBottomBarIconClick = {},
        isUserLoggedIn = true,
        onLogoutButtonClick = {},
        onLoginButtonClick = {},
    )
}