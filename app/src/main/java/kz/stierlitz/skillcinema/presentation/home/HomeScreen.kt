package kz.stierlitz.skillcinema.presentation.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kz.stierlitz.skillcinema.R
import kz.stierlitz.skillcinema.presentation.home.components.MovieCard
import kz.stierlitz.skillcinema.presentation.home.components.MovieListRow
import kz.stierlitz.skillcinema.ui.theme.SkillTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onNavigateToFilm: (Int) -> Unit = {},
    onNavigateToFilmList: (String, String) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeContract.Effect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 26.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_title),
                    contentDescription = "Skill cinema",
                    modifier = Modifier
                        .width(120.dp)
                        .height(18.24152183532715.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(horizontal = 26.dp)
                    )
                }
            } else if (state.error != null) {
                Text(text = "Ошибка: Нету интернета: ${state.error}", color = Color.Red, modifier = Modifier.padding(horizontal = 26.dp))
            } else {
                MovieListRow(title = "Премьеры", listType = "CLOSES_RELEASES", movies = state.premieres, onMovieClick = onNavigateToFilm, onSeeAllClick = onNavigateToFilmList)
                MovieListRow(title = "Популярное", listType = "TOP_POPULAR_ALL", movies = state.popular, onMovieClick = onNavigateToFilm, onSeeAllClick = onNavigateToFilmList)
                MovieListRow(title = "Боевики США", listType = "ACTION_USA", movies = state.actionUsa, onMovieClick = onNavigateToFilm, onSeeAllClick = onNavigateToFilmList)
                MovieListRow(title = "Топ-250", listType = "TOP_250_MOVIES", movies = state.top250, onMovieClick = onNavigateToFilm, onSeeAllClick = onNavigateToFilmList)
                MovieListRow(title = "Драмы", listType = "DRAMA_FRANCE", movies = state.drama, onMovieClick = onNavigateToFilm, onSeeAllClick = onNavigateToFilmList)
                MovieListRow(title = "Сериалы", listType = "POPULAR_SERIES", movies = state.series, onMovieClick = onNavigateToFilm, onSeeAllClick = onNavigateToFilmList)
            }
        }
    }
}