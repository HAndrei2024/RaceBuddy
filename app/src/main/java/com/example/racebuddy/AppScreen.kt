package com.example.racebuddy

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
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
import com.example.racebuddy.data.database.AppRepository
import com.example.racebuddy.data.database.UserPreferencesRepository
import com.example.racebuddy.ui.common.BottomAppBar
import com.example.racebuddy.ui.favorite.FavoriteScreen
import com.example.racebuddy.ui.login.LoginScreen
import com.example.racebuddy.ui.login.LoginScreenViewModel
import com.example.racebuddy.ui.main.MainScreen
import com.example.racebuddy.ui.main.MainScreenUiState
import com.example.racebuddy.ui.main.MainScreenViewModel
import com.example.racebuddy.ui.profile.ProfileScreen
import com.example.racebuddy.ui.signup.SignUpScreen
import com.example.racebuddy.ui.signup.SignUpViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class AppScreen {
    Login,
    SignUp,
    Main,
    Favorite,
    Profile
}

@Composable
fun App(
    loginScreenViewModel: LoginScreenViewModel = viewModel(
        factory = LoginScreenViewModel.factory
    ),
    signUpViewModel: SignUpViewModel = viewModel(
        factory = SignUpViewModel.factory
    ),
    mainScreenViewModel: MainScreenViewModel = viewModel(
        factory = MainScreenViewModel.factory
    ),
    appViewModel: AppViewModel = viewModel(
        factory = AppViewModel.factory
    ),
    navController: NavHostController = rememberNavController()
) {
    val loginUiState by loginScreenViewModel.uiState.collectAsState()
    val signUpUiState by signUpViewModel.uiState.collectAsState()
    val mainScreenUiState by mainScreenViewModel.uiState.collectAsState()
    //val appScreenUiState by appViewModel.uiState.collectAsState()
    val startDestination = if(mainScreenUiState.athleteLoginId == -1 || mainScreenUiState.athleteLoginId == 0)
        AppScreen.Login.name else AppScreen.Main.name

    appViewModel.updateScreenSelected(AppScreen.valueOf(startDestination))
    // is this state needed? (Selected Screen)

    NavHost(
        navController = navController,
        startDestination = startDestination
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
                },
                onSkipClick = {
                    navController.navigate(AppScreen.Main.name)
                    loginScreenViewModel.updateDataSourceOnSkipButtonClicked()
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
            MainScreen(
                    onHomeClick = {
                        navController.navigate(AppScreen.Main.name)
                        appViewModel.updateScreenSelected(AppScreen.Main)
                                  },
                    onFavoriteClick = {
                        navController.navigate(AppScreen.Favorite.name)
                        appViewModel.updateScreenSelected(AppScreen.Favorite)
                                      },
                    onProfileClick = {
                        navController.navigate(AppScreen.Profile.name)
                        appViewModel.updateScreenSelected(AppScreen.Profile)
                                     },
                    isHomeSelected = true,
                    isFavoriteSelected = false,
                    isProfileSelected = false,
                )
        }

        composable(route = AppScreen.Favorite.name) {
            val favoriteEvents = mainScreenViewModel.favoriteEventList.collectAsState().value
            FavoriteScreen(
                mainScreenViewModel = mainScreenViewModel,
                favoriteEventsList = favoriteEvents,
                onHomeClick = {
                    navController.navigate(AppScreen.Main.name)
                    appViewModel.updateScreenSelected(AppScreen.Main)
                },
                onFavoriteClick = {
                    navController.navigate(AppScreen.Favorite.name)
                    appViewModel.updateScreenSelected(AppScreen.Favorite)
                },
                onProfileClick = {
                    navController.navigate(AppScreen.Profile.name)
                    appViewModel.updateScreenSelected(AppScreen.Profile)
                },
                isHomeSelected = false,
                isFavoriteSelected = true,
                isProfileSelected = false,
                )
        }

        composable(route = AppScreen.Profile.name) {

            val athlete = mainScreenViewModel.athlete.collectAsState().value

            ProfileScreen(
                athlete = athlete,
                onHomeClick = {
                    navController.navigate(AppScreen.Main.name)
                    appViewModel.updateScreenSelected(AppScreen.Main)
                },
                onFavoriteClick = {
                    navController.navigate(AppScreen.Favorite.name)
                    appViewModel.updateScreenSelected(AppScreen.Favorite)
                },
                onProfileClick = {
                    navController.navigate(AppScreen.Profile.name)
                    appViewModel.updateScreenSelected(AppScreen.Profile)
                },
                onLogoutButtonClick = {}
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