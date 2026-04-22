package kz.stierlitz.skillcinema.features.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kz.stierlitz.skillcinema.domain.model.Movie
import kz.stierlitz.skillcinema.ui.theme.SkillTheme

@Composable
fun MovieListRow(title: String, movies: List<Movie>, onMovieClick: (Int) -> Unit) {
    if (movies.isNotEmpty()) {
        Column {
            Text(
                modifier = Modifier.padding(horizontal = 26.dp),
                text = title,
                style = SkillTheme.typography.graphiksSemiBold,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF272727)
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 26.dp),
            ) {
                items(movies) { movie ->
                    MovieCard(movie = movie, onClick = { onMovieClick(movie.kinopoiskId) })
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}