package com.example.racebuddy.app.v2

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
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
import com.example.racebuddy.Application
import com.example.racebuddy.app.v1.App
import com.example.racebuddy.data.database.AppRepository
import com.example.racebuddy.data.database.EventInfo
import com.example.racebuddy.data.database.RemoteDataSource
import com.example.racebuddy.data.database.UserPreferencesRepository
import com.example.racebuddy.ui.v2.favorite.FavoriteScreen
import com.example.racebuddy.ui.v2.login.LoginScreen
import com.example.racebuddy.ui.v2.login.LoginScreenViewModel
import com.example.racebuddy.ui.v2.main.MainScreen
import com.example.racebuddy.ui.v2.main.MainScreenViewModel
import com.example.racebuddy.ui.v2.profile.ProfileScreen
import com.example.racebuddy.ui.v2.profile.ProfileScreenViewModel
import com.example.racebuddy.ui.v2.search.SearchScreen
import com.example.racebuddy.ui.v2.signup.SignUpFirstScreen
import com.example.racebuddy.ui.v2.signup.SignupScreensViewModel
import com.example.racebuddy.ui.v2.signup.SignupSecondScreen
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class AppScreen {
    Login,
    SignUpFirst,
    SignUpSecond,
    Main,
    Favorite,
    Profile,
    Event,
    Search
}

@Composable
fun Appv2(
    loginScreenViewModel: LoginScreenViewModel = viewModel(
        factory = LoginScreenViewModel.factory
    ),
    signupScreensViewModel: SignupScreensViewModel = viewModel(
        factory = SignupScreensViewModel.factory
    ),
    mainScreenViewModel: MainScreenViewModel = viewModel(
        factory = MainScreenViewModel.factory
    ),
    profileScreenViewModel: ProfileScreenViewModel = viewModel(
        factory = ProfileScreenViewModel.factory
    ),
    appScreenViewModel: AppScreenViewModel = viewModel(
        factory = AppScreenViewModel.factory
    ),
    navController: NavHostController = rememberNavController()
) {
    val loginScreenUiState by loginScreenViewModel.uiState.collectAsState()
    val signupScreensUiState by signupScreensViewModel.uiState.collectAsState()
    val mainScreenUiState by mainScreenViewModel.uiState.collectAsState()

    val athleteInfo by appScreenViewModel.athleteInfo.collectAsState()

    val startDestination = if(athleteInfo.athleteId == "-1") AppScreen.Login.name else AppScreen.Main.name


    //appViewModel.updateScreenSelected(AppScreen.valueOf(startDestination))
    // is this state needed? (Selected Screen)

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = AppScreen.Login.name) {
            LaunchedEffect(loginScreenUiState) {
                if (loginScreenUiState.loginSucces) {
                    navController.navigate(AppScreen.Main.name) {
                        launchSingleTop = true
                    }
                    //mainScreenViewModel.updateAthlete()
                }
            }
            LoginScreen(
                onEmailTexFieldChange = { loginScreenViewModel.onEmailChange(it) },
                emailStringValue = loginScreenUiState.email,
                onPasswordTextFieldChange = { loginScreenViewModel.onPasswordChange(it) },
                passwordStringValue = loginScreenUiState.password,
                errorMessage = loginScreenUiState.errorMessage,
                isLoading = loginScreenUiState.isLoading,
                onLoginClick = {
                    loginScreenViewModel.onLoginButtonClick()
               },
                onSignUpClick = {
                    loginScreenViewModel.onSignUpTextClick()
                    navController.navigate(AppScreen.SignUpFirst.name) {
                        launchSingleTop = true
                    }
                },
                onSkipClick = {
                    navController.navigate(AppScreen.Main.name) {
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(route = AppScreen.Main.name) {
            MainScreen(
                athleteInfo = athleteInfo,
                events = mainScreenUiState.filteredEvents, //cyclingEvents,
                searchEvents = mainScreenUiState.events,
                onFavoriteIconClick = { eventUuid: String, delete: Boolean ->
                    mainScreenViewModel.onFavoriteIconClick(eventUuid, delete)
                },
                onFilterButtonClick = { category: String ->
                    mainScreenViewModel.updateFilteredEventsByCategory(
                        category
                    )
                },
                favoriteEventsId = mainScreenUiState.favoriteEventIds.map { it.eventUuid },
                onSearchIconClick = {
                    //navController.navigate(AppScreen.Search.name)
                },
                onBottomBarIconClicked = { int: Int ->
                    when(int) {
                        1 -> navController.navigate(AppScreen.Favorite.name) {
                            launchSingleTop = true
                        }
                        2 -> navController.navigate(AppScreen.Profile.name) {
                            launchSingleTop = true
                        }
                        else -> {

                        }
                    }

                },
                onFavoriteIconBottomBarClick = {

                },
                onProfileIconBottomBarClick = {},
                isUserLoggedIn = RemoteDataSource.SupabaseClient.client.auth.currentUserOrNull() != null,
                modifier = Modifier,
            )
            BackHandler {  }
        }
        
//        composable(
//            route = AppScreen.Search.name,
//            enterTransition = { slideInVertically(initialOffsetY = { it }) },
//            exitTransition = { slideOutVertically(targetOffsetY = { it }) },
//            popEnterTransition = { slideInVertically(initialOffsetY = { it }) },
//            popExitTransition = { slideOutVertically(targetOffsetY = { it }) }
//        ) {
//            SearchScreen(
//                events = emptyList(),
//                onCloseClick = {
//                    //navController.popBackStack()
//                },
//            )
//        }

        composable(
            route = AppScreen.SignUpFirst.name,
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec =  tween(700)
                )
            }
        ) {
            LaunchedEffect(signupScreensUiState) {
                if (signupScreensUiState.signupSucces) {
                    mainScreenViewModel.updateAthlete()
                    navController.navigate(AppScreen.SignUpSecond.name) {
                        launchSingleTop = true
                    }
                }
            }

            SignUpFirstScreen(
                onFirstNameTextFieldChange = { signupScreensViewModel.onFirstNameChange(it) },
                firstNameStringValue = signupScreensUiState.firstName,
                onLastNameTextFieldChange = { signupScreensViewModel.onLastNameChange(it) },
                lastNameStringValue = signupScreensUiState.lastName,
                onEmailTexFieldChange = { signupScreensViewModel.onEmailChange(it) },
                emailStringValue = signupScreensUiState.email,
                onPasswordTextFieldChange = { signupScreensViewModel.onPasswordChange(it) },
                passwordStringValue = signupScreensUiState.password,
                onVerifyPasswordTextFieldChange = { signupScreensViewModel.onVerifyPasswordChange(it) },
                verifyPasswordStringValue = signupScreensUiState.verifyPassword,
                errorMessage = signupScreensUiState.errorMessage,
                showError = signupScreensUiState.showError,
                isLoading = signupScreensUiState.isLoading,
                onContinueClick = {
                    //signupScreensViewModel.signUp()
                    signupScreensViewModel.onContinueButtonClick()
                    //navController.navigate(AppScreen.SignUpSecond.name)
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
            LaunchedEffect(signupScreensUiState) {
//                if(signupScreensUiState.signupSucces) {
//                    //val snackbarHostState = remember { SnackbarHostState() }
//
//                    // Triggered when the composable enters the composition
//
//                    signupScreensUiState.snackbarHostState.showSnackbar("Account created succesfuly!")
//
//                }
                if (signupScreensUiState.updatedDatabase) {
                    navController.navigate(AppScreen.Main.name) {
                        launchSingleTop = true
                    }
                }
            }



            SignupSecondScreen(
                onFirstNameChange = { signupScreensViewModel.onFirstNameChange(it) },
                firstNameValue = signupScreensUiState.firstName,
                onLastNameChange = { signupScreensViewModel.onLastNameChange(it) },
                lastNameValue = signupScreensUiState.lastName,
                errorMessage = signupScreensUiState.errorMessage,
                showError = signupScreensUiState.showError,
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
                onSkipClick = {
                    navController.navigate(AppScreen.Main.name) {
                        launchSingleTop = true
                    }
                },
                onDoneClick = {
                    signupScreensViewModel.onDoneClick()
                    //navController.navigate(AppScreen.Main.name)
                },
                modifier = Modifier,
            )
            BackHandler {  }
        }

        composable(
            route = AppScreen.Favorite.name,
        ) {

            val favoriteEventIds: List<String> = mainScreenUiState.favoriteEventIds.map { it -> it.eventUuid }
            val favoriteEventsInfo: List<EventInfo> = mainScreenUiState.events.filter { eventInfo ->
                favoriteEventIds.contains(eventInfo.evenUuid)
            }
            FavoriteScreen(
                favoriteEvents = favoriteEventsInfo,
                onFavoriteIconClick = {id: String, value: Boolean -> mainScreenViewModel.onFavoriteIconClick(id, value) },
                isUserLoggedIn = RemoteDataSource.SupabaseClient.client.auth.currentUserOrNull() != null,
                onHomeIconClick = {

                },
                onProfileIconClick = {

                },
                onBottomBarIconClick = {int: Int ->
                    when(int) {
                        0 -> navController.navigate(AppScreen.Main.name) {
                            launchSingleTop = true
                        }
                        2 -> navController.navigate(AppScreen.Profile.name) {
                            launchSingleTop = true
                        }
                        else -> {

                        }
                    }
                }
            )
        }
        
        composable(route = AppScreen.Profile.name) { 
            ProfileScreen(
                athleteInfo = athleteInfo,
                isUserLoggedIn = athleteInfo.athleteId != "-1", //TODO: is it possible to be ""?
                onLogoutButtonClick = {
                    loginScreenViewModel.loginSuccesUpdate(false)
                    profileScreenViewModel.onLogoutClick()
                },
                onLoginButtonClick = {
                    navController.navigate(AppScreen.Login.name) {
                        launchSingleTop = true
                    }
                },
                onBottomBarIconClick = {int: Int ->
                    when(int) {
                        0 -> navController.navigate(AppScreen.Main.name) {
                            launchSingleTop = true
                        }
                        1 -> navController.navigate(AppScreen.Favorite.name) {
                            launchSingleTop = true
                        }
                        else -> {

                        }
                    }
                }
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