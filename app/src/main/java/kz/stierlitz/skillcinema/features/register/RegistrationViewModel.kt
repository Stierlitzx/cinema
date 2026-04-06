package kz.stierlitz.skillcinema.features.register

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

class RegistrationViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _state = MutableStateFlow(RegistrationContract.State())
    val state = _state.asStateFlow()

    private val _effect = Channel<RegistrationContract.SideEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleIntent(intent: RegistrationContract.Intent) {
        when (intent) {
            is RegistrationContract.Intent.OnEmailChange -> {
                _state.update { it.copy(email = intent.email) }
            }
            is RegistrationContract.Intent.OnPasswordChange -> {
                _state.update { it.copy(password = intent.password) }
            }
            is RegistrationContract.Intent.OnConfirmPasswordChange -> {
                _state.update { it.copy(confirmPassword = intent.confirmPassword) }
            }
            RegistrationContract.Intent.OnRegisterWithEmailClick -> {
                registerWithEmail()
            }
            RegistrationContract.Intent.OnGoogleSignInClick -> {
                // Handled in UI layer via Launcher
            }
            is RegistrationContract.Intent.OnGoogleSignInResult -> {
                if (intent.isSuccess) {
                    viewModelScope.launch { _effect.send(RegistrationContract.SideEffect.NavigateToLoader) }
                } else if (intent.errorMessage != null) {
                    viewModelScope.launch { _effect.send(RegistrationContract.SideEffect.ShowError(intent.errorMessage)) }
                }
            }
            RegistrationContract.Intent.ResetError -> {
                _state.update { it.copy(errorMessage = null) }
            }
        }
    }

    private fun registerWithEmail() {
        val currentEmail = _state.value.email
        val currentPassword = _state.value.password
        val currentConfirmPassword = _state.value.confirmPassword

        if (currentEmail.isBlank() || currentPassword.isBlank() || currentConfirmPassword.isBlank()) {
            viewModelScope.launch { _effect.send(RegistrationContract.SideEffect.ShowError("Заполните все поля")) }
            return
        }

        if (currentPassword != currentConfirmPassword) {
            viewModelScope.launch { _effect.send(RegistrationContract.SideEffect.ShowError("Пароли не совпадают")) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                auth.createUserWithEmailAndPassword(currentEmail, currentPassword).await()
                auth.currentUser?.sendEmailVerification()?.await()
                auth.signOut() // Sign out to force login after verification

                _state.update { it.copy(isLoading = false, isSuccess = true) }
                _effect.send(RegistrationContract.SideEffect.ShowMessage("Регистрация успешна! Пожалуйста, подтвердите email."))
                _effect.send(RegistrationContract.SideEffect.NavigateToLogin)
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
                _effect.send(RegistrationContract.SideEffect.ShowError(e.localizedMessage ?: "Ошибка регистрации"))
            }
        }
    }
}
