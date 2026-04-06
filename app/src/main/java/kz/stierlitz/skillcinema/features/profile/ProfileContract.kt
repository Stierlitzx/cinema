package kz.stierlitz.skillcinema.features.profile

interface ProfileContract {
    data class State(
        val username: String? = "",
        val profilePictureUrl: String? = "",
        val isLoading: Boolean = true,
    )

    sealed class Intent {
        object LoadUserData : Intent()
        object OnSignOutClick : Intent()
    }

    sealed class SideEffect {
        object NavigateToRegistration : SideEffect()
    }
}