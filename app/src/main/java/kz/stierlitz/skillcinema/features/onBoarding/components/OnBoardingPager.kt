package kz.stierlitz.skillcinema.features.onBoarding.components

import android.R.attr.fontWeight
import android.R.attr.text
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kz.stierlitz.skillcinema.R
import kz.stierlitz.skillcinema.ui.theme.SkillTheme

@Composable
fun OnBoardingPager(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    onBoardingItems: List<Int>
    ) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxHeight(0.8f),
                userScrollEnabled = true
            ) { page ->
                if (page < onBoardingItems.size) {
                    Column() {
                        Spacer(modifier = Modifier.weight(0.2f))
                        Image(
                            painter = painterResource(id = onBoardingItems[page]),
                            contentDescription = "Onboarding image $pagerState",
                            modifier = Modifier
                                .weight(0.5f)
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(20.dp),
                        )
                        val onBoardingText: List<String> = listOf(
                            "Узнавай\nо премьерах",
                            "Создавай\nколлекции",
                            "Делись\nс друзьями",
                        )

                        Spacer(modifier = Modifier.weight(0.2f))

                        Text(
                            modifier = Modifier.padding(26.dp),
                            text = onBoardingText[page],
                            style = SkillTheme.typography.graphiksMedium,
                            fontWeight = FontWeight.W500,
                            color = Color(0xFF272727),
                            fontSize = 32.sp,
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

@Composable
fun PagerIndicator(
    currentPage: Int,
    pageCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { iteration ->
            val color = if (currentPage == iteration) Color(0xFF121616) else Color(0xFFD9D9D9)

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}