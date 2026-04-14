package kz.stierlitz.skillcinema.features.login

interface LoginContract {
    data class State(
        val email: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val isSuccess: Boolean = false,
        val isPasswordVisible: Boolean = false,
        val errorMessage: String? = null
    )

    sealed class Intent {
        data class OnEmailChange(val email: String) : Intent()
        data class OnPasswordChange(val password: String) : Intent()
        data class OnTogglePasswordVisibility(val isPasswordVisible: Boolean) : Intent()
        object OnLoginWithEmailClick : Intent()
        data class OnGoogleSignInResult(val isSuccess: Boolean, val errorMessage: String?) : Intent()
    }

    sealed class SideEffect {
        object NavigateToLoader : SideEffect()
        object NavigateToRegister : SideEffect()
        data class ShowError(val message: String) : SideEffect()
    }
}

