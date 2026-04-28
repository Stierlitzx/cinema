package kz.stierlitz.skillcinema.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kz.stierlitz.skillcinema.R
import kz.stierlitz.skillcinema.domain.model.Movie
import kz.stierlitz.skillcinema.ui.theme.SkillTheme

@Composable
fun MovieListRow(title: String, listType: String, movies: List<Movie>, onMovieClick: (Int) -> Unit, onSeeAllClick: (String, String) -> Unit = { _, _ -> }) {
    if (movies.isNotEmpty()) {
        Column {
            Row() {
                Text(
                    modifier = Modifier.padding(horizontal = 26.dp).weight(1f),
                    text = title,
                    style = SkillTheme.typography.graphiksSemiBold,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF272727)
                )
                Text(
                    modifier = Modifier
                        .padding(horizontal = 26.dp)
                        .clickable {
                            onSeeAllClick(listType, title)
                        },
                    text = "Все",
                    style = SkillTheme.typography.graphiksMedium,
                    fontSize = 14.sp,
                    color = Color(0xFF3D3BFF)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 26.dp),
            ) {
                items(movies) { movie ->
                    MovieCard(movie = movie, onClick = { onMovieClick(movie.kinopoiskId) })
                }
                item {
                    Column(
                        modifier = Modifier
                            .height(250.dp)
                            .padding(end = 16.dp)
                            .clickable {
                                onSeeAllClick(listType, title)
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_forward), // We'll add this drawable
                                contentDescription = "Show All",
                                tint = Color(0xFF3D3BFF),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Показать все",
                            fontSize = 12.sp,
                            color = Color(0xFF272727),
                            style = SkillTheme.typography.graphiksRegular
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}