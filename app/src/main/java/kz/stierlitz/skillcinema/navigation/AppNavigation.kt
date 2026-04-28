package kz.stierlitz.skillcinema.navigation

import android.R.attr.shadowColor
import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
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
import kz.stierlitz.skillcinema.presentation.loader.LoaderScreen
import kz.stierlitz.skillcinema.presentation.loader.LoaderViewModel
import kz.stierlitz.skillcinema.presentation.onBoarding.OnBoardingScreen
import kz.stierlitz.skillcinema.presentation.onBoarding.OnBoardingViewModel
import kz.stierlitz.skillcinema.presentation.register.RegistrationScreen
import kz.stierlitz.skillcinema.presentation.register.RegistrationViewModel
import kz.stierlitz.skillcinema.presentation.register.RegistrationContract
import kz.stierlitz.skillcinema.presentation.login.LoginScreen
import kz.stierlitz.skillcinema.presentation.login.LoginViewModel
import kz.stierlitz.skillcinema.presentation.login.LoginContract
import kz.stierlitz.skillcinema.presentation.profile.ProfileScreen
import kz.stierlitz.skillcinema.presentation.profile.ProfileViewModel
import kz.stierlitz.skillcinema.data.remote.auth.GoogleAuthUiClient
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import kz.stierlitz.skillcinema.R
import kz.stierlitz.skillcinema.presentation.film.FilmScreen
import kz.stierlitz.skillcinema.presentation.home.HomeScreen

data class BottomNavItem(
    val route: Screen,
    val iconResId: Int
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current.applicationContext

    val googleAuthUiClient = GoogleAuthUiClient(
        context = context,
        oneTapClient = Identity.getSignInClient(context)
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            val isMainGraphDestination = currentDestination?.hierarchy?.any {
                it.route?.substringBefore("?") == Route.MainGraph::class.qualifiedName
            } == true

            val hideBottomBarScreens = listOf(
                Screen.Film::class.qualifiedName,
                Screen.FilmList::class.qualifiedName,
                Screen.Seasons::class.qualifiedName,
                Screen.Filter::class.qualifiedName,
                Screen.CountryFilter::class.qualifiedName,
                Screen.GenreFilter::class.qualifiedName,
                Screen.YearFilter::class.qualifiedName
            )
            val shouldHideBottomBar = currentDestination?.hierarchy?.any { dest ->
                hideBottomBarScreens.contains(dest.route?.substringBefore("?"))
            } == true

            if (isMainGraphDestination && !shouldHideBottomBar) {
                Surface(
                    modifier = Modifier
                        .height(78.dp)
                        .fillMaxWidth()
                        .drawBehind {
                            drawIntoCanvas { canvas ->
                                val paint = Paint()
                                val frameworkPaint = paint.asFrameworkPaint()
                                frameworkPaint.color = Color.Transparent.toArgb()
                                frameworkPaint.setShadowLayer(
                                    8.dp.toPx(),
                                    0f,
                                    (-4).dp.toPx(),
                                    shadowColor
                                )
                                canvas.drawRoundRect(
                                    0f, 0f, size.width, size.height,
                                    16.dp.toPx(), 16.dp.toPx(),
                                    paint
                                )
                            }
                        },
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                ) {
                    NavigationBar(
                        containerColor = Color.White
                    ) {
                        val items = listOf(
                            BottomNavItem(Screen.Home, R.drawable.ic_home),
                            BottomNavItem(Screen.Search, R.drawable.ic_search),
                            BottomNavItem(Screen.Profile, R.drawable.ic_person)
                        )

                        items.forEach { item ->
                            val itemRoute = item.route::class.qualifiedName ?: item.route::class.simpleName.orEmpty()
                            val selected = currentDestination.hierarchy.any { destination ->
                                destination.route?.substringBefore("?") == itemRoute
                            } == true

                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        painter = painterResource(id = item.iconResId),
                                        contentDescription = null
                                    )
                                },
                                selected = selected,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                alwaysShowLabel = false,
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color(0xFF3D3BFF),
                                    unselectedIconColor = Color.Black,
                                    indicatorColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = Route.MainGraph //.AuthGraph
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
                    HomeScreen(
                        onNavigateToFilm = { filmId ->
                            navController.navigate(Screen.Film(filmId))
                        },
                        onNavigateToFilmList = { type, title ->
                            navController.navigate(Screen.FilmList(type, title))
                        }
                    )
                }

                composable<Screen.Search> {
                    kz.stierlitz.skillcinema.presentation.search.SearchScreen(
                        onNavigateToFilm = { filmId ->
                            navController.navigate(Screen.Film(filmId))
                        },
                        onNavigateToFilter = {
                            navController.navigate(Screen.Filter)
                        }
                    )
                }

                composable<Screen.Filter> {
                    kz.stierlitz.skillcinema.presentation.search.filter.FilterScreen(
                        onBack = { navController.navigateUp() },
                        onNavigateToCountry = { navController.navigate(Screen.CountryFilter) },
                        onNavigateToGenre = { navController.navigate(Screen.GenreFilter) },
                        onNavigateToYear = { navController.navigate(Screen.YearFilter) }
                    )
                }

                composable<Screen.CountryFilter> {
                    kz.stierlitz.skillcinema.presentation.search.filter.CountryFilterScreen(
                        onBack = { navController.navigateUp() }
                    )
                }

                composable<Screen.GenreFilter> {
                    kz.stierlitz.skillcinema.presentation.search.filter.GenreFilterScreen(
                        onBack = { navController.navigateUp() }
                    )
                }

                composable<Screen.YearFilter> {
                    kz.stierlitz.skillcinema.presentation.search.filter.YearFilterScreen(
                        onBack = { navController.navigateUp() }
                    )
                }

                composable<Screen.Profile> {
                    val viewModel = viewModel<ProfileViewModel>(
                        factory = object : ViewModelProvider.Factory {
                            @Suppress("UNCHECKED_CAST")
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return ProfileViewModel(googleAuthUiClient) as T
                            }
                        }
                    )
                    val state by viewModel.state.collectAsStateWithLifecycle()

                    ProfileScreen(
                        state = state,
                        onSignOut = {
                            viewModel.handleIntent(kz.stierlitz.skillcinema.presentation.profile.ProfileContract.Intent.OnSignOutClick)
                            navController.navigate(Route.AuthGraph) {
                                popUpTo(Route.MainGraph) { inclusive = true }
                            }
                        }
                    )
                }

                composable<Screen.FilmList> { backStackEntry ->
                    val args = backStackEntry.toRoute<Screen.FilmList>()
                    kz.stierlitz.skillcinema.presentation.filmList.FilmListScreen(
                        title = args.title,
                        type = args.type,
                        onBack = { navController.navigateUp() },
                        onNavigateToFilm = { filmId ->
                            navController.navigate(Screen.Film(filmId))
                        }
                    )
                }

                composable<Screen.Film> { backStackEntry ->
                    val args = backStackEntry.toRoute<Screen.Film>()

                    FilmScreen(
                        filmId = args.id,
                        onBack = { navController.navigateUp() },
                        onNavigateToSeasons = { filmId, filmName ->
                            navController.navigate(Screen.Seasons(filmId, filmName))
                        }
                    )
                }

                composable<Screen.Seasons> { backStackEntry ->
                    val args = backStackEntry.toRoute<Screen.Seasons>()
                    kz.stierlitz.skillcinema.presentation.seasons.SeasonsScreen(
                        filmId = args.filmId,
                        filmName = args.filmName,
                        onBack = { navController.navigateUp() }
                    )
                }
            }
        }
    }
}