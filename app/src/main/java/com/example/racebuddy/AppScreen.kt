package com.example.racebuddy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.racebuddy.ui.login.LoginScreen
import com.example.racebuddy.ui.login.LoginScreenViewModel
import com.example.racebuddy.ui.main.MainScreen
import com.example.racebuddy.ui.signup.SignUpScreen
import com.example.racebuddy.ui.signup.SignUpViewModel

enum class AppScreen {
    Login,
    SignUp,
    Main
}

@Composable
fun App(
    loginScreenViewModel: LoginScreenViewModel = viewModel(
        factory = LoginScreenViewModel.factory
    ),
    signUpViewModel: SignUpViewModel = viewModel(
        factory = SignUpViewModel.factory
    ),
    navController: NavHostController = rememberNavController()
) {
    val loginUiState by loginScreenViewModel.uiState.collectAsState()
    val signUpUiState by signUpViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = AppScreen.Login.name
    ) {
        composable(route = AppScreen.Login.name) {
            LaunchedEffect(loginUiState) {
                if (loginUiState.logInSucces) {
                    navController.navigate(AppScreen.Main.name)
                    //TODO
                }
            }
            LoginScreen(
                loginScreenViewModel = loginScreenViewModel,
                onSignUpClick = { navController.navigate(AppScreen.SignUp.name) },
                onLogInClick = {
                    loginScreenViewModel.onLoginButtonPressed()
                }
            )
        }

        composable(route = AppScreen.SignUp.name) {
            LaunchedEffect(signUpUiState) {
                if (signUpUiState.signUpSuccess) {
                    navController.navigate(AppScreen.Login.name)
                    signUpViewModel.updateSignUpSuccess(false)
                }
            }
            SignUpScreen(signUpViewModel)
        }

        composable(route = AppScreen.Main.name) {
            MainScreen()
        }
    }
}