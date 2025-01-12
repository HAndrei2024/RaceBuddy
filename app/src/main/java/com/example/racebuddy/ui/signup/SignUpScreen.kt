package com.example.racebuddy.ui.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.racebuddy.ui.common.LoginTopAppBar
import com.example.racebuddy.ui.login.UserButton
import com.example.racebuddy.ui.login.UserTextField
import kotlin.math.sign

@Composable
fun SignUpScreen(
    signUpViewModel: SignUpViewModel
) {
    val uiState by signUpViewModel.uiState.collectAsState()
    Scaffold(
        topBar = { LoginTopAppBar() }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            UserTextField(
                value = uiState.name,
                label = "Name",
                onValueChange = { signUpViewModel.onNameChange(it) }
            )
            UserTextField(
                value = uiState.surname,
                label = "Surname",
                onValueChange = { signUpViewModel.onSurnameChange(it) }
            )
            UserTextField(
                value = uiState.username,
                label = "Username",
                onValueChange = { signUpViewModel.onUsernameChange(it) }
            )
            UserTextField(
                value = uiState.password,
                label = "Password",
                onValueChange = { signUpViewModel.onPasswordChange(it) }
            )
            if(uiState.errorMessageFlag) {
                Text(uiState.errorMessage)
            }
            UserButton(
                text = "Sign Up",
                onClick = { signUpViewModel.onSignUpButtonPressed() }
            )
        }
    }

}