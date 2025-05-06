package com.example.racebuddy.app.v2

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.racebuddy.Application
import com.example.racebuddy.app.v2.AppScreen
import com.example.racebuddy.app.v1.AppUiState
import com.example.racebuddy.data.database.AppRepository
import com.example.racebuddy.data.database.UserPreferencesRepository
import com.example.racebuddy.ui.v2.login.LoginScreen
import com.example.racebuddy.ui.v2.login.LoginScreenViewModel
import com.example.racebuddy.ui.v2.main.MainScreen
import com.example.racebuddy.ui.v2.signup.SignUpScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class AppScreen {
    Login,
    SignUp,
    Main,
    Favorite,
    Profile,
    Event
}

@Composable
fun Appv2(
    loginScreenViewModel: LoginScreenViewModel = viewModel(
        factory = LoginScreenViewModel.factory
    ),
    navController: NavHostController = rememberNavController()
) {
    val loginScreenUiState by loginScreenViewModel.uiState.collectAsState()


    val startDestination = AppScreen.Login.name


    //appViewModel.updateScreenSelected(AppScreen.valueOf(startDestination))
    // is this state needed? (Selected Screen)

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = AppScreen.Login.name) {
            LoginScreen(
                onEmailTexFieldChange = { loginScreenViewModel.onEmailChange(it) },
                emailStringValue = loginScreenUiState.email,
                onPasswordTextFieldChange = { loginScreenViewModel.onPasswordChange(it) },
                passwordStringValue = loginScreenUiState.password,
                onLoginClick = {
                    loginScreenViewModel.onLoginButtonClick()
                    //navController.navigate(AppScreen.Main)
               },
                onSignUpClick = {
                    loginScreenViewModel.onSignUpTextClick()
                    navController.navigate(AppScreen.SignUp)
                },
                onSkipClick = {
                    navController.navigate(AppScreen.Main)
                },
            )
        }

        composable(route = AppScreen.Main.name) {
            MainScreen()
        }

        composable(route = AppScreen.SignUp.name) {
            SignUpScreen()
        }
    }
}


class AppViewModel(
    val appRepository: AppRepository,
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState(AppScreen.Main))
    val uiState = _uiState.asStateFlow()

    fun updateScreenSelected(screen: AppScreen) {
        _uiState.update { currentState ->
            currentState.copy(
                screenSelected = screen
            )
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                AppViewModel(
                    appRepository = application.container.appRepository,
                    userPreferencesRepository = application.userPreferencesRepository
                )
            }
        }
    }
}

data class AppUiState(
    val screenSelected: AppScreen
)