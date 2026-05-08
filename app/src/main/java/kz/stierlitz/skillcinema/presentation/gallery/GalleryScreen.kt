package kz.stierlitz.skillcinema.presentation.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.collectLatest
import kz.stierlitz.skillcinema.presentation.common.CommonScaffold
import kz.stierlitz.skillcinema.presentation.common.ErrorView
import kz.stierlitz.skillcinema.presentation.common.LoadingView

@Composable
fun GalleryPage(
    filmId: Int,
    onNavigateBack: () -> Unit,
    viewModel: GalleryViewModel = viewModel()
) {
    LaunchedEffect(filmId) {
        viewModel.onEvent(GalleryIntent.LoadGallery(filmId))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                GalleryEffect.NavigateBack -> onNavigateBack()
                is GalleryEffect.ShowError -> { /* Show error */ }
            }
        }
    }

    val state by viewModel.state.collectAsState()

    GalleryScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun GalleryScreen(
    state: GalleryState,
    onEvent: (GalleryIntent) -> Unit
) {
    CommonScaffold(
        title = "Галерея",
        onBackClick = { onEvent(GalleryIntent.OnBackClick) }
    ) { paddingValues ->
        when {
            state.isLoading -> LoadingView(Modifier.padding(paddingValues))
            state.error != null -> ErrorView(state.error, Modifier.padding(paddingValues))
            else -> GalleryContent(
                state = state,
                onEvent = onEvent,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
private fun GalleryContent(
    state: GalleryState,
    onEvent: (GalleryIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {

        if (state.tabs.isNotEmpty()) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
            ) {
                items(state.tabs.size) { index ->
                    val tab = state.tabs[index]
                    val selected = index == state.selectedTabIndex
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = if (selected) Color(0xFF3D3BFF) else Color.White,
                        border = if (selected) null else androidx.compose.foundation.BorderStroke(1.dp, Color.Gray),
                        modifier = Modifier.clickable { onEvent(GalleryIntent.SelectTab(index)) }
                    ) {
                        Text(
                            text = "${tab.label} ${tab.count}",
                            fontSize = 14.sp,
                            color = if (selected) Color.White else Color.Black,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        val images = state.images
        val groupCount = (images.size + 2) / 3

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(groupCount) { groupIndex ->
                val start = groupIndex * 3
                val first = images.getOrNull(start)
                val second = images.getOrNull(start + 1)
                val third = images.getOrNull(start + 2)

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (first != null || second != null) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            if (first != null) {
                                AsyncImage(
                                    model = first.previewUrl.takeIf { it.isNotEmpty() } ?: first.imageUrl,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(140.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.LightGray)
                                )
                            }
                            if (second != null) {
                                AsyncImage(
                                    model = second.previewUrl.takeIf { it.isNotEmpty() } ?: second.imageUrl,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(140.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.LightGray)
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                    if (third != null) {
                        AsyncImage(
                            model = third.previewUrl.takeIf { it.isNotEmpty() } ?: third.imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray)
                        )
                    }
                }
            }
        }
    }
}

