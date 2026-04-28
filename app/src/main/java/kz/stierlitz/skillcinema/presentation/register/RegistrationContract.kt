package kz.stierlitz.skillcinema.presentation.register

interface RegistrationContract {
    data class State(
        val name: String = "",
        val email: String = "",
        val password: String = "",
        val confirmPassword: String = "",
        val isLoading: Boolean = false,
        val isPasswordVisible: Boolean = false,
        val isConfirmPasswordVisible: Boolean = false,
        val isSuccess: Boolean = false,
        val errorMessage: String? = null
    )

    sealed class Intent {
        data class OnNameChange(val name: String) : Intent()
        data class OnEmailChange(val email: String) : Intent()
        data class OnPasswordChange(val password: String) : Intent()
        data class OnConfirmPasswordChange(val confirmPassword: String) : Intent()
        data class OnTogglePasswordVisibility(val isPasswordVisible: Boolean) : Intent()
        data class OnToggleConfirmPasswordVisibility(val isConfirmPasswordVisible: Boolean) : Intent()
        object OnRegisterWithEmailClick : Intent()
        object OnGoogleSignInClick : Intent()
        data class OnGoogleSignInResult(val isSuccess: Boolean, val errorMessage: String?) : Intent()
        object ResetError : Intent()
    }

    sealed class SideEffect {
        object NavigateToLoader : SideEffect()
        object NavigateToLogin : SideEffect()
        data class ShowError(val message: String) : SideEffect()
        data class ShowMessage(val message: String) : SideEffect()
    }
}
