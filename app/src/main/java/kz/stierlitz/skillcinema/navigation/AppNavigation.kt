package kz.stierlitz.skillcinema.navigation

import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch
import kz.stierlitz.skillcinema.features.loader.LoaderScreen
import kz.stierlitz.skillcinema.features.loader.LoaderViewModel
import kz.stierlitz.skillcinema.features.onBoarding.OnBoardingScreen
import kz.stierlitz.skillcinema.features.onBoarding.OnBoardingViewModel
import kz.stierlitz.skillcinema.features.register.RegistrationScreen
import kz.stierlitz.skillcinema.features.register.RegistrationViewModel
import kz.stierlitz.skillcinema.features.register.RegistrationContract
import kz.stierlitz.skillcinema.features.login.LoginScreen
import kz.stierlitz.skillcinema.features.login.LoginViewModel
import kz.stierlitz.skillcinema.features.login.LoginContract
import kz.stierlitz.skillcinema.features.profile.ProfileScreen
import kz.stierlitz.skillcinema.features.profile.ProfileViewModel
import kz.stierlitz.skillcinema.data.remote.auth.GoogleAuthUiClient
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current.applicationContext

    val googleAuthUiClient = GoogleAuthUiClient(
        context = context,
        oneTapClient = Identity.getSignInClient(context)
    )

    NavHost(
        navController = navController,
        startDestination = Route.AuthGraph
    ) {
        navigation<Route.AuthGraph>(startDestination = Screen.OnBoarding) {
            composable<Screen.OnBoarding> {
                val viewModel: OnBoardingViewModel = viewModel<OnBoardingViewModel>()

                OnBoardingScreen(
                    onFinished = {
                        navController.navigate(Screen.Registration) {
                            popUpTo<Screen.OnBoarding> { inclusive = true }
                        }
                    },
                    viewModel = viewModel
                )
            }

            composable<Screen.Registration> {
                val viewModel: RegistrationViewModel = viewModel<RegistrationViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()
                val scope = rememberCoroutineScope()
                
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == RESULT_OK) {
                            scope.launch {
                                val signInResult = googleAuthUiClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                viewModel.handleIntent(
                                    RegistrationContract.Intent.OnGoogleSignInResult(
                                        isSuccess = signInResult.data != null,
                                        errorMessage = signInResult.errorMessage
                                    )
                                )
                            }
                        }
                    }
                )

                RegistrationScreen(
                    state = state,
                    effectFlow = viewModel.effect,
                    onIntent = viewModel::handleIntent,
                    onNavigateToLoader = {
                        navController.navigate(Screen.Loader) {
                            popUpTo(Route.AuthGraph) { inclusive = false }
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigate(Screen.Login) {
                            popUpTo(Screen.Registration) { inclusive = true }
                        }
                    },
                    onGoogleSignInClick = {
                        scope.launch {
                            val signInIntentSender = googleAuthUiClient.signIn()
                            if (signInIntentSender != null) {
                                launcher.launch(
                                    IntentSenderRequest.Builder(signInIntentSender).build()
                                )
                            }
                        }
                    }
                )
            }

            composable<Screen.Login> {
                val viewModel: LoginViewModel = viewModel<LoginViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()
                val scope = rememberCoroutineScope()
                
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == RESULT_OK) {
                            scope.launch {
                                val signInResult = googleAuthUiClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                viewModel.handleIntent(
                                    LoginContract.Intent.OnGoogleSignInResult(
                                        isSuccess = signInResult.data != null,
                                        errorMessage = signInResult.errorMessage
                                    )
                                )
                            }
                        }
                    }
                )

                LoginScreen(
                    state = state,
                    effectFlow = viewModel.effect,
                    onIntent = viewModel::handleIntent,
                    onNavigateToLoader = {
                        navController.navigate(Screen.Loader) {
                            popUpTo(Route.AuthGraph) { inclusive = false }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Screen.Registration) {
                            popUpTo(Screen.Login) { inclusive = true }
                        }
                    },
                    onGoogleSignInClick = {
                        scope.launch {
                            val signInIntentSender = googleAuthUiClient.signIn()
                            if (signInIntentSender != null) {
                                launcher.launch(
                                    IntentSenderRequest.Builder(signInIntentSender).build()
                                )
                            }
                        }
                    }
                )
            }

            composable<Screen.Loader> {
                val viewModel: LoaderViewModel = viewModel<LoaderViewModel>()

                LoaderScreen(
                    onNavigateToHome = {
                        navController.navigate(Route.MainGraph) {
                            popUpTo(Route.AuthGraph) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigate(Screen.Login) {
                            popUpTo(Screen.Loader) { inclusive = true }
                        }
                    },
                    viewModel = viewModel
                )
            }
        }

        navigation<Route.MainGraph>(startDestination = Screen.Home) {
            composable<Screen.Home> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Button(onClick = { navController.navigate(Screen.Profile) }) {
                        Text("Перейти в профиль")
                    }
                }
            }

            composable<Screen.Profile> {
                val viewModel = viewModel<ProfileViewModel>(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return ProfileViewModel(googleAuthUiClient) as T
                        }
                    }
                )
                val state by viewModel.state.collectAsStateWithLifecycle()

                ProfileScreen(
                    state = state,
                    onSignOut = {
                        viewModel.handleIntent(kz.stierlitz.skillcinema.features.profile.ProfileContract.Intent.OnSignOutClick)
                        navController.navigate(Route.AuthGraph) {
                            popUpTo(Route.MainGraph) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}