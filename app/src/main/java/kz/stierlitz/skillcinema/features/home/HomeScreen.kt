package kz.stierlitz.skillcinema.features.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kz.stierlitz.skillcinema.R
import kz.stierlitz.skillcinema.domain.model.Movie
import kz.stierlitz.skillcinema.features.home.components.MovieCard
import kz.stierlitz.skillcinema.features.home.components.MovieListRow
import kz.stierlitz.skillcinema.ui.theme.SkillTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onNavigateToFilm: (Int) -> Unit = {}
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
                MovieListRow(title = "Премьеры", movies = state.premieres, onMovieClick = onNavigateToFilm)
                MovieListRow(title = "Популярное", movies = state.popular, onMovieClick = onNavigateToFilm)
                MovieListRow(title = "Боевики США", movies = state.actionUsa, onMovieClick = onNavigateToFilm)
                MovieListRow(title = "Топ-250", movies = state.top250, onMovieClick = onNavigateToFilm)
                MovieListRow(title = "Драмы", movies = state.drama, onMovieClick = onNavigateToFilm)
                MovieListRow(title = "Сериалы", movies = state.series, onMovieClick = onNavigateToFilm)
            }
        }
    }
}