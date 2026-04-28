package kz.stierlitz.skillcinema.presentation.loader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoaderViewModel(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {
    private val _effect = Channel<LoaderContract.SideEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        checkUserStatus()
    }

    private fun checkUserStatus() {
        viewModelScope.launch {
            delay(1500)
            val currentUser = auth.currentUser
            if (currentUser != null) {
                // If using email/password and not verified, sign out and go to login
                // Google accounts are usually pre-verified
                if (!currentUser.isEmailVerified && currentUser.providerData.any { it.providerId == "password" }) {
                    auth.signOut()
                    _effect.send(LoaderContract.SideEffect.NavigateToLogin)
                } else {
                    _effect.send(LoaderContract.SideEffect.NavigateToHome)
                }
            } else {
                _effect.send(LoaderContract.SideEffect.NavigateToLogin)
            }
        }
    }
}