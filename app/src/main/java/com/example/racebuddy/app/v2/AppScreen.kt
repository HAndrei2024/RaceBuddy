package com.example.racebuddy.app.v2

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import androidx.room.util.createCancellationSignal
import com.example.racebuddy.Application
import com.example.racebuddy.app.v2.AppScreen
import com.example.racebuddy.app.v1.AppUiState
import com.example.racebuddy.data.database.AppRepository
import com.example.racebuddy.data.database.UserPreferencesRepository
import com.example.racebuddy.ui.v2.login.LoginScreen
import com.example.racebuddy.ui.v2.login.LoginScreenViewModel
import com.example.racebuddy.ui.v2.main.MainScreen
import com.example.racebuddy.ui.v2.signup.SignUpFirstScreen
import com.example.racebuddy.ui.v2.signup.SignupScreensViewModel
import com.example.racebuddy.ui.v2.signup.SignupSecondScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.sign

enum class AppScreen {
    Login,
    SignUpFirst,
    SignUpSecond,
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
    signupScreensViewModel: SignupScreensViewModel = viewModel(
        factory = SignupScreensViewModel.factory
    ),
    navController: NavHostController = rememberNavController()
) {
    val loginScreenUiState by loginScreenViewModel.uiState.collectAsState()
    val signupScreensUiState by signupScreensViewModel.uiState.collectAsState()

    val startDestination = AppScreen.Login.name


    //appViewModel.updateScreenSelected(AppScreen.valueOf(startDestination))
    // is this state needed? (Selected Screen)

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = AppScreen.Login.name) {
            LaunchedEffect(loginScreenUiState) {
                if (loginScreenUiState.loginSucces) {
                    navController.navigate(AppScreen.Main.name)
                }
            }
            LoginScreen(
                onEmailTexFieldChange = { loginScreenViewModel.onEmailChange(it) },
                emailStringValue = loginScreenUiState.email,
                onPasswordTextFieldChange = { loginScreenViewModel.onPasswordChange(it) },
                passwordStringValue = loginScreenUiState.password,
                errorMessage = loginScreenUiState.errorMessage,
                onLoginClick = {
                    loginScreenViewModel.onLoginButtonClick()
               },
                onSignUpClick = {
                    loginScreenViewModel.onSignUpTextClick()
                    navController.navigate(AppScreen.SignUpFirst.name)
                },
                onSkipClick = {
                    navController.navigate(AppScreen.Main.name)
                },
            )
        }

        composable(route = AppScreen.Main.name) {
            MainScreen()
        }

        composable(
            route = AppScreen.SignUpFirst.name,
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec =  tween(700)
                )
            }
        ) {
            SignUpFirstScreen(
                onFirstNameTextFieldChange = { signupScreensViewModel.onFirstNameChange(it) },
                firstNameStringValue = signupScreensUiState.firstName,
                onLastNameTextFieldChange = { signupScreensViewModel.onLastNameChange(it) },
                lastNameStringValue = signupScreensUiState.lastName,
                onEmailTexFieldChange = { signupScreensViewModel.onEmailChange(it) },
                emailStringValue = signupScreensUiState.email,
                onPasswordTextFieldChange = { signupScreensViewModel.onPasswordChange(it) },
                passwordStringValue = signupScreensUiState.password,
                errorMessage = signupScreensUiState.errorMessage,
                onContinueClick = {
                    signupScreensViewModel.onContinueButtonClick()
                    navController.navigate(AppScreen.SignUpSecond.name)
                },
            )
        }

        composable(
            route = AppScreen.SignUpSecond.name,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(700)
                )
            }
        ) {
            SignupSecondScreen(
                onEmailTexFieldChange = {},
                emailStringValue = "",
                onPasswordTextFieldChange = {},
                passwordStringValue = "",
                errorMessage = false,
                onContinueClick = {},
                onBirthdateTextFieldClick = { signupScreensViewModel.onBirthdateTextfieldClick() },
                birthdateStringValue = "",
                showDatePicker = signupScreensUiState.showDatePicker,
                onDatePickerDoneClick = { signupScreensViewModel.onDatePickerDoneClick() },
                onDatePickerDissmisClick = { signupScreensViewModel.onDatePickerDissmisClick() },
                onNationalityTextFieldChange = { signupScreensViewModel.onNationalityChange(it) },
                nationalityStringValue = signupScreensUiState.nationality,
                onDayTextFieldChange = { signupScreensViewModel.onDayChange(it) },
                dayStringValue = signupScreensUiState.day,
                onMonthTextFieldChange = { signupScreensViewModel.onMonthChange(it) },
                monthStringValue = signupScreensUiState.month,
                onYearTextFieldChange = { signupScreensViewModel.onYearChange(it) },
                yearStringValue = signupScreensUiState.year,
                selectedGender = signupScreensUiState.gender,
                onGenderButtonClick = { signupScreensViewModel.onGenderChange(it) },
                onLicenseNumberTextFieldChange = { signupScreensViewModel.onLicenseNumberChange(it)},
                licenseNumberStringValue = signupScreensUiState.localRegistrationNumber,
                modifier = Modifier,
            )
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