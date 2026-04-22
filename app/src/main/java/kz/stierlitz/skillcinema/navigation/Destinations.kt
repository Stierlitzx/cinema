package kz.stierlitz.skillcinema.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object AuthGraph : Route

    @Serializable
    data object MainGraph : Route
}

@Serializable
sealed interface Screen {
    @Serializable
    data object OnBoarding : Screen

    @Serializable
    data object Loader : Screen

    @Serializable
    data object Home : Screen

    @Serializable
    data object Registration : Screen

    @Serializable
    data object Login : Screen

    @Serializable
    data object Search : Screen

    @Serializable
    data class Film(val id: Int) : Screen

    @Serializable
    data object Profile : Screen
}