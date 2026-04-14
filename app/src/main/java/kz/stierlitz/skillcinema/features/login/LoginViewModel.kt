package kz.stierlitz.skillcinema.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _state = MutableStateFlow(LoginContract.State())
    val state = _state.asStateFlow()

    private val _effect = Channel<LoginContract.SideEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleIntent(intent: LoginContract.Intent) {
        when (intent) {
            is LoginContract.Intent.OnEmailChange -> {
                _state.update { it.copy(email = intent.email) }
            }
            is LoginContract.Intent.OnPasswordChange -> {
                _state.update { it.copy(password = intent.password) }
            }
            is LoginContract.Intent.OnTogglePasswordVisibility -> {
                _state.update { it.copy(isPasswordVisible = intent.isPasswordVisible) }
            }
            LoginContract.Intent.OnLoginWithEmailClick -> {
                loginWithEmail()
            }
            is LoginContract.Intent.OnGoogleSignInResult -> {
                if (intent.isSuccess) {
                    viewModelScope.launch { _effect.send(LoginContract.SideEffect.NavigateToLoader) }
                } else if (intent.errorMessage != null) {
                    viewModelScope.launch { _effect.send(LoginContract.SideEffect.ShowError(intent.errorMessage)) }
                }
            }

            else -> {}
        }
    }

    private fun loginWithEmail() {
        val currentEmail = _state.value.email
        val currentPassword = _state.value.password

        if (currentEmail.isBlank() || currentPassword.isBlank()) {
            viewModelScope.launch { _effect.send(LoginContract.SideEffect.ShowError("Заполните все поля")) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                auth.signInWithEmailAndPassword(currentEmail, currentPassword).await()

                val user = auth.currentUser
                if (user != null && !user.isEmailVerified) {
                    auth.signOut()
                    _state.update { it.copy(isLoading = false) }
                    _effect.send(LoginContract.SideEffect.ShowError("Пожалуйста, подтвердите вашу почту перед входом"))
                    return@launch
                }

                _state.update { it.copy(isLoading = false, isSuccess = true) }
                _effect.send(LoginContract.SideEffect.NavigateToLoader)
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
                _effect.send(LoginContract.SideEffect.ShowError(e.localizedMessage ?: "Ошибка авторизации"))
            }
        }
    }
}
