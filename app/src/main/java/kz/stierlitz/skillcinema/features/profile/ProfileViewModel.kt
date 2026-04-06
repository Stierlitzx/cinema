package kz.stierlitz.skillcinema.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kz.stierlitz.skillcinema.data.remote.auth.GoogleAuthUiClient

class ProfileViewModel(
    private val googleAuthUiClient: GoogleAuthUiClient
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileContract.State())
    val state = _state.asStateFlow()

    private val _effect = Channel<ProfileContract.SideEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        handleIntent(ProfileContract.Intent.LoadUserData)
    }

    fun handleIntent(intent: ProfileContract.Intent) {
        when (intent) {
            is ProfileContract.Intent.LoadUserData -> loadUserData()
            is ProfileContract.Intent.OnSignOutClick -> signOut()
        }
    }

    private fun loadUserData() {
        val user = googleAuthUiClient.getSignedInUser()
        _state.update {
            it.copy(
                username = user?.username,
                profilePictureUrl = user?.profilePictureUrl,
                isLoading = false
            )
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            googleAuthUiClient.signOut()
            _effect.send(ProfileContract.SideEffect.NavigateToRegistration)
        }
    }
}