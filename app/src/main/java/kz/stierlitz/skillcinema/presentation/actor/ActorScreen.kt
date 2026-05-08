package kz.stierlitz.skillcinema.presentation.actor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kz.stierlitz.skillcinema.R
import kz.stierlitz.skillcinema.domain.model.ActorDetail
import kz.stierlitz.skillcinema.domain.model.Movie
import kz.stierlitz.skillcinema.presentation.common.CommonScaffold
import kz.stierlitz.skillcinema.presentation.common.ErrorView
import kz.stierlitz.skillcinema.presentation.common.LoadingView
import kz.stierlitz.skillcinema.presentation.home.components.MovieListRow
import kotlinx.coroutines.flow.collectLatest
import kotlin.collections.isNotEmpty

@Composable
fun ActorScreen(
    actorId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToFilmography: (actorId: Int, actorName: String) -> Unit,
    onNavigateToFilm: (filmId: Int) -> Unit,
    viewModel: ActorViewModel = viewModel()
) {
    LaunchedEffect(actorId) {
        viewModel.onEvent(ActorEvent.LoadActor(actorId))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ActorEffect.NavigateBack -> onNavigateBack()
                is ActorEffect.NavigateToFilmography ->
                    onNavigateToFilmography(effect.actorId, effect.actorName)
                is ActorEffect.NavigateToFilm -> onNavigateToFilm(effect.filmId)
                is ActorEffect.ShowError -> { /* optionally show snackbar */ }
            }
        }
    }

    val state by viewModel.state.collectAsState()

    ActorScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun ActorScreen(
    state: ActorState,
    onEvent: (ActorEvent) -> Unit
) {
    CommonScaffold(
        onBackClick = { onEvent(ActorEvent.OnBackClick) }
    ) { paddingValues ->
        when {
            state.isLoading -> LoadingView(Modifier.padding(paddingValues))
            state.error != null -> ErrorView(state.error, Modifier.padding(paddingValues))
            state.actor != null -> ActorContent(
                actor = state.actor,
                bestFilms = state.bestFilms,
                onFilmClick = { onEvent(ActorEvent.OnFilmClick(it)) },
                onFilmographyClick = { onEvent(ActorEvent.OnFilmographyClick) },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
private fun ActorContent(
    actor: ActorDetail,
    bestFilms: List<Movie>,
    onFilmClick: (Int) -> Unit,
    onFilmographyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(horizontal = 26.dp)) {
            AsyncImage(
                model = actor.posterUrl,
                contentDescription = actor.nameRu,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(146.dp)
                    .height(201.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    text = actor.nameRu ?: actor.nameEn ?: "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = actor.profession ?: "",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        if (bestFilms.isNotEmpty()) {
            MovieListRow(
                title = stringResource(R.string.actor_best),
                listType = "ACTOR_BEST",
                movies = bestFilms,
                onMovieClick = { onFilmClick(it) },
                onSeeAllClick = { _, _ -> onFilmographyClick() }
            )
            Spacer(Modifier.height(24.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onFilmographyClick() }
                .padding(horizontal = 26.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(R.string.actor_filmography),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Text(
                    text = stringResource(R.string.actor_films_count, actor.films?.size ?: 0),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "К списку",
                    color = Color(0xFF3D3BFF),
                    fontSize = 14.sp
                )
                Icon(
                    painter = androidx.compose.ui.res.painterResource(id = R.drawable.ic_arrow_forward), // Assuming this exists as used in MovieListRow
                    contentDescription = null,
                    modifier = Modifier.padding(start = 4.dp).size(12.dp),
                    tint = Color(0xFF3D3BFF)
                )
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}
