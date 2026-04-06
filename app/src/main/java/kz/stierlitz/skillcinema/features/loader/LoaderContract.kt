package kz.stierlitz.skillcinema.features.loader

interface LoaderContract {
    data class State(
        val isLoading: Boolean = true
    )

    sealed class Intent {
        object CheckAuth : Intent()
    }

    sealed class SideEffect {
        object NavigateToHome : SideEffect()
        object NavigateToLogin : SideEffect()
    }
}