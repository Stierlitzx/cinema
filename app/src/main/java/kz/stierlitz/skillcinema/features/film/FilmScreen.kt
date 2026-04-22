package kz.stierlitz.skillcinema.features.film

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
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
import kz.stierlitz.skillcinema.data.remote.kinopoisk.StaffResponse
import kz.stierlitz.skillcinema.features.film.components.StaffListRow

@Composable
fun FilmScreen(
    filmId: Int,
    viewModel: FilmViewModel = viewModel(),
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    LaunchedEffect(filmId) {
        viewModel.handleIntent(FilmContract.Intent.LoadFilm(filmId))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            if (effect is FilmContract.Effect.ShowError) {
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val film = state.film ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        ) {
            AsyncImage(
                model = film.coverUrl ?: film.posterUrl,
                contentDescription = film.nameRu,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                        )
                    )
            )

            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(top = 8.dp, start = 8.dp)
                    .align(Alignment.TopStart)
                    .size(32.dp)
            ) {
                Icon(
                    modifier = Modifier.size(14.dp),
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!film.logoUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = film.logoUrl,
                        contentDescription = "Logo",
                        modifier = Modifier.height(60.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                val rating = film.ratingKinopoisk ?: film.ratingImdb
                Text(
                    text = "${rating?.toString() ?: "-"} ${film.nameRu ?: film.nameEn ?: ""}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = "${film.year ?: ""}, ${film.genres.joinToString(", ") { it.genre ?: "" }}",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
                Text(
                    text = "${film.countries.firstOrNull()?.country ?: ""}, ${film.filmLength?.let { "$it мин" } ?: ""}, ${film.ratingAgeLimits?.replace("age", "") ?: ""}+",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* TODO: Favorite */ }) {
                        Icon(painter = painterResource(id = R.drawable.ic_favourite), contentDescription = "Favorite", tint = Color.White)
                    }
                    IconButton(onClick = { /* TODO: Bookmark */ }) {
                        Icon(painter = painterResource(id = R.drawable.ic_bookmark), contentDescription = "Bookmark", tint = Color.White)
                    }
                    IconButton(onClick = { /* TODO: Hide */ }) {
                        Icon(painter = painterResource(id = R.drawable.ic_hide), contentDescription = "Hide", tint = Color.White)
                    }
                    IconButton(onClick = { /* TODO: Share */ }) {
                        Icon(painter = painterResource(id = R.drawable.ic_share), contentDescription = "Share", tint = Color.White)
                    }
                    IconButton(onClick = { /* TODO: More */ }) {
                        Icon(painter = painterResource(id = R.drawable.ic_more), contentDescription = "More", tint = Color.White)
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(26.dp)) {
            if (!film.shortDescription.isNullOrEmpty()) {
                Text(text = film.shortDescription, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = film.description ?: "",
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (state.actors.isNotEmpty()) {
                Text(text = "В фильме снимались", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))
                StaffListRow(staffList = state.actors)
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (state.workers.isNotEmpty()) {
                Text(text = "Над фильмом работали", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))
                StaffListRow(staffList = state.workers)
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Text(text = "Галерея", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))
            
            if (state.gallery.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(state.gallery) { image ->
                        AsyncImage(
                            model = image.imageUrl,
                            contentDescription = "Film frame",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(120.dp)
                                .width(180.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray)
                        )
                    }
                }
            } else {
                Text(text = "не были добавлены", fontSize = 14.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}
