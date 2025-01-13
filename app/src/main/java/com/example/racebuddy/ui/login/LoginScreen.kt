package com.example.racebuddy.ui.login

import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.racebuddy.ui.common.LoginTopAppBar

@Composable
fun LoginScreen(
    loginScreenViewModel: LoginScreenViewModel,
    onSignUpClick: () -> Unit = { },
    onLogInClick: () -> Unit = { },
    onSkipClick: () -> Unit = { },
    modifier: Modifier = Modifier
) {
    val uiState by loginScreenViewModel.uiState.collectAsState()

    Scaffold(
        topBar = { LoginTopAppBar() }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            UserTextField(
                value = uiState.username,
                label = "Username",
                onValueChange = { loginScreenViewModel.onUsernameChange(it) }
            )
            UserTextField(
                value = uiState.password,
                label = "Password",
                onValueChange = { loginScreenViewModel.onPasswordChange(it) }
            )
            if(uiState.errorMessage) {
                Text("Error Logging in!")
            }
            UserButton(
                text = "Login",
                onClick = onLogInClick
            )
            UserButton(
                text = "Sign Up",
                onClick = onSignUpClick
            )
            UserButton(
                text = "Skip >>>",
                onClick = onSkipClick
            )
        }
    }
}

@Composable
fun UserTextField(
    value: String,
    label: String = "Label",
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        label = { Text(label) },
        onValueChange = onValueChange,
    )
}

@Composable
fun UserButton(
    text: String,
    onClick: () -> Unit = { }
) {
    Button(
        onClick = onClick
    ) {
        Text(
            text = text
        )
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    //LoginScreen()
}

