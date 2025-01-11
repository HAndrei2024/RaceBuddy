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
import com.example.racebuddy.LoginTopAppBar

@Composable
fun LoginScreen(
    title: String,
    loginScreenViewModel: LoginScreenViewModel = viewModel(
        factory = LoginScreenViewModel.factory
    ),
    modifier: Modifier = Modifier
) {
    val uiState by loginScreenViewModel.uiState.collectAsState()

    Scaffold(
        topBar = { LoginTopAppBar(title) }
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
            LoginButton(
                text = "Login",
                onClick = { loginScreenViewModel.onLoginButtonPressed() }
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
fun LoginButton(
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
    LoginScreen(
        title = "RaceBuddy"
    )
}

