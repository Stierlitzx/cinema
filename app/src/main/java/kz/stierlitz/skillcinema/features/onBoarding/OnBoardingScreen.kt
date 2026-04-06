package kz.stierlitz.skillcinema.features.onBoarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kz.stierlitz.skillcinema.R

@Composable
fun OnBoardingScreen(
    onFinished: () -> Unit,
    viewModel: OnBoardingViewModel
) {
    val state by viewModel.state.collectAsState()

    OnBoardingPage(
        step = state.currentStep,
        onNext = { viewModel.handleIntent(OnBoardingContract.Intent.OnNextClick) },
        onSkip = { viewModel.handleIntent(OnBoardingContract.Intent.OnSkipClick) },
        viewModel = viewModel
    )

//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(text = "экран OnBoarding")
//            Button(
//                onClick = onFinished,
//                modifier = Modifier.padding(top = 16.dp)
//            ) {
//                Text(text = "Регистрация")
//            }
//        }
//    }
}

@Composable
fun OnBoardingPage(
    step: Int,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    viewModel: OnBoardingViewModel
) {
    Scaffold(

    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .padding(26.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_title),
                    contentDescription = "Skill cinema",
                )
                TextButton(
                    onClick = {},
                    Modifier.background(Color.Red)
                ) {
                    Text(
                        text = "Skip",
                        color = Color.Black
                    )
                }
            }
        }
    }
}