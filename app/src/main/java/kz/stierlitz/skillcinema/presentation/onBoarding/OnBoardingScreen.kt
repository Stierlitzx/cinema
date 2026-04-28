package kz.stierlitz.skillcinema.presentation.onBoarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kz.stierlitz.skillcinema.R
import kz.stierlitz.skillcinema.presentation.onBoarding.components.OnBoardingPager
import kz.stierlitz.skillcinema.presentation.onBoarding.components.PagerIndicator
import kz.stierlitz.skillcinema.ui.theme.SkillTheme

@Composable
fun OnBoardingScreen(
    onFinished: () -> Unit,
    viewModel: OnBoardingViewModel
) {
    val state by viewModel.state.collectAsState()
    val realPageCount = 3
    val pagerState = rememberPagerState(pageCount = { realPageCount + 1 })
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage == realPageCount) {
            onFinished()
        }
    }

    LaunchedEffect(state.currentStep) {
        if (pagerState.currentPage != state.currentStep) {
            pagerState.animateScrollToPage(state.currentStep)
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .padding(26.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_title),
                    contentDescription = "Skill cinema",
                    modifier = Modifier
                        .width(120.dp)
                        .height(18.24152183532715.dp)
                )
                Text(
                    text = "Пропустить",
                    style = SkillTheme.typography.graphiksMedium,
                    color = Color(0xFFB5B5C9),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .background(shape = RoundedCornerShape(100), color = Color.Transparent)
                        .padding(0.dp)
                        .clickable(
                            indication = null,
                            interactionSource = null
                        ) { onFinished() }
                )
            }

            OnBoardingPager(
                pagerState = pagerState,
                modifier = Modifier.weight(1f),
                onBoardingItems = listOf(
                    R.drawable.onboarding1,
                    R.drawable.onboarding2,
                    R.drawable.onboarding3,
                )
            )

            PagerIndicator(
                currentPage = if (pagerState.currentPage < realPageCount) pagerState.currentPage else realPageCount - 1,
                pageCount = realPageCount,
                modifier = Modifier
                    .weight(0.1f)
                    .padding(start = 26.dp, end = 26.dp)
            )
        }
    }

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