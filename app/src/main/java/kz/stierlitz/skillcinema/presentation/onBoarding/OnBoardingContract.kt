package kz.stierlitz.skillcinema.presentation.onBoarding

interface OnBoardingContract {
    data class State(
        val currentStep: Int = 0,
        val isCompleted: Boolean = false
    )

    sealed class Intent {
        object OnNextClick : Intent()
        object OnSkipClick : Intent()
    }

    sealed class SideEffect {
        object NavigateToLogin : SideEffect()
    }
}