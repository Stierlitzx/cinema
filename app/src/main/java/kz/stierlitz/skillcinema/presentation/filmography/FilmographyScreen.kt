package kz.stierlitz.skillcinema.presentation.filmography

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kz.stierlitz.skillcinema.domain.model.Movie
import kz.stierlitz.skillcinema.presentation.common.CommonScaffold
import kz.stierlitz.skillcinema.presentation.common.ErrorView
import kz.stierlitz.skillcinema.presentation.common.LoadingView
import kotlinx.coroutines.flow.collectLatest
import androidx.lifecycle.viewmodel.compose.viewModel
import kz.stierlitz.skillcinema.ui.theme.SkillTheme

@Composable
fun FilmographyPage(
    actorId: Int,
    actorName: String,
    onNavigateBack: () -> Unit,
    onNavigateToFilm: (filmId: Int) -> Unit,
    viewModel: FilmographyViewModel = viewModel()
) {
    LaunchedEffect(actorId) {
        viewModel.onEvent(FilmographyEvent.Load(actorId, actorName))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                FilmographyEffect.NavigateBack -> onNavigateBack()
                is FilmographyEffect.NavigateToFilm -> onNavigateToFilm(effect.filmId)
                is FilmographyEffect.ShowError -> { /* optionally show snackbar */ }
            }
        }
    }

    val state by viewModel.state.collectAsState()

    FilmographyScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun FilmographyScreen(
    state: FilmographyState,
    onEvent: (FilmographyEvent) -> Unit
) {
    CommonScaffold(
        title = "Фильмография",
        onBackClick = { onEvent(FilmographyEvent.OnBackClick) }
    ) { paddingValues ->
        when {
            state.isLoading -> LoadingView(Modifier.padding(paddingValues))
            state.error != null -> ErrorView(state.error, Modifier.padding(paddingValues))
            else -> FilmographyContent(
                state = state,
                onEvent = onEvent,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
private fun FilmographyContent(
    state: FilmographyState,
    onEvent: (FilmographyEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {

        Text(
            text = state.actorName,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )

        if (state.tabs.isNotEmpty()) {
            androidx.compose.foundation.lazy.LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                items(state.tabs.size) { index ->
                    val tab = state.tabs[index]
                    val selected = index == state.selectedTabIndex
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = if (selected) Color(0xFF3D3BFF) else Color.White,
                        border = if (selected) null else androidx.compose.foundation.BorderStroke(1.dp, Color.Gray),
                        modifier = Modifier.clickable { onEvent(FilmographyEvent.SelectTab(index)) }
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

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = state.films,
                key = { it.kinopoiskId }
            ) { film ->
                FilmographyItem(
                    film = film,
                    onClick = { onEvent(FilmographyEvent.OnFilmClick(film.kinopoiskId)) }
                )
            }
        }
    }
}

@Composable
fun FilmographyItem(
    film: Movie,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            AsyncImage(
                model = film.posterUrlPreview,
                contentDescription = film.nameRu,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(96.dp)
                    .height(132.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            film.ratingKinopoisk?.let { rating ->
                Box(
                    modifier = Modifier
                        .padding(6.dp)
                        .align(Alignment.TopStart)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.White)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                        text = rating.toString(),
                        style = SkillTheme.typography.graphiksMedium,
                        color = Color.Black,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }

        Spacer(Modifier.width(12.dp))

        Column {
            Text(
                text = film.nameRu ?: film.nameEn ?: "",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = buildString {
                    film.year?.let { append(it) }
                    val genre = film.genres.firstOrNull()?.name
                    if (genre != null) {
                        if (isNotEmpty()) append(", ")
                        append(genre)
                    }
                },
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}
