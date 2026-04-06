package kz.stierlitz.skillcinema.features.onBoarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnBoardingViewModel : ViewModel() {

    private val _state = MutableStateFlow(OnBoardingContract.State())
    val state: StateFlow<OnBoardingContract.State> = _state.asStateFlow()

    fun handleIntent(intent: OnBoardingContract.Intent) {
        when (intent) {
            is OnBoardingContract.Intent.OnNextClick -> moveNext()
            is OnBoardingContract.Intent.OnSkipClick -> moveToLoader()
        }
    }

    private fun moveNext() {
        val currentState = _state.value
        if (currentState.currentStep < 2) {
            _state.update { it.copy(currentStep = currentState.currentStep + 1) }
        } else {
            moveToLoader()
        }
    }

    private fun moveToLoader() {
        viewModelScope.launch {
            delay(2000)
        }
    }
}