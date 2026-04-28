package kz.stierlitz.skillcinema.presentation.search

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kz.stierlitz.skillcinema.R
import kz.stierlitz.skillcinema.domain.model.Movie
import kz.stierlitz.skillcinema.ui.theme.SkillTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = viewModel(),
    onNavigateToFilm: (Int) -> Unit = {},
    onNavigateToFilter: () -> Unit = {}
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SearchContract.Effect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 26.dp)
            .padding(top = 24.dp)
    ) {
        SearchBar(
            query = state.query,
            onQueryChange = { viewModel.handleIntent(SearchContract.Intent.SearchTextChange(it)) },
            modifier = Modifier.fillMaxWidth(),
            onNavigateToFilter = onNavigateToFilter
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.query.isNotBlank() && state.results.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "К сожалению, по вашему запросу ничего не найдено",
                    fontSize = 14.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(state.results) { movie ->
                    SearchResultItem(movie = movie, onClick = { onNavigateToFilm(movie.kinopoiskId) })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToFilter: () -> Unit = {}
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp)),
        placeholder = { Text("Фильмы, актёры, режиссёры", color = Color(0xFF838390), fontSize = 14.sp) },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "Search",
                tint = Color(0xFF838390)
            )
        },
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_filter),
                contentDescription = "Filter",
                tint = Color(0xFF838390),
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onNavigateToFilter() }
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF4F4F8),
            unfocusedContainerColor = Color(0xFFF4F4F8),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true
    )
}

@Composable
fun SearchResultItem(movie: Movie, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .width(110.dp)
                .height(160.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            AsyncImage(
                model = movie.posterUrlPreview.takeIf { it.isNotEmpty() } ?: movie.posterUrl,
                contentDescription = movie.nameRu ?: movie.nameEn,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            val rating = movie.ratingKinopoisk ?: movie.ratingImdb
            if (rating != null) {
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

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                text = movie.nameRu ?: movie.nameOriginal ?: movie.nameEn ?: "Без названия",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            val details = mutableListOf<String>()
            if (movie.year != null && movie.year > 0) details.add(movie.year.toString())
            if (movie.genres.isNotEmpty()) details.add(movie.genres.first().name)

            Text(
                text = details.joinToString(", "),
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
