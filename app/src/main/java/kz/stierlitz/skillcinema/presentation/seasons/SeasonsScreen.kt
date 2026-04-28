package kz.stierlitz.skillcinema.presentation.seasons

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kz.stierlitz.skillcinema.R
import kz.stierlitz.skillcinema.ui.theme.SkillTheme

@Composable
fun SeasonsScreen(
    filmId: Int,
    filmName: String,
    viewModel: SeasonsViewModel = viewModel(),
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(filmId) {
        viewModel.handleIntent(SeasonsContract.Intent.LoadSeasons(filmId, filmName))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            if (effect is SeasonsContract.Effect.ShowError) {
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.size(32.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
            }

            Text(
                modifier = Modifier.weight(1f),
                text = state.filmName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.size(32.dp))
        }

        if (state.isLoading) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            if (state.seasons.isNotEmpty()) {
                val selectedSeason = state.seasons.getOrNull(state.selectedSeasonIndex)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 26.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Сезон",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF272727)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(state.seasons) { index, season ->
                            val isSelected = index == state.selectedSeasonIndex
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isSelected) Color(0xFF3D3BFF) else Color.White)
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) Color.Transparent else Color.Black,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        viewModel.handleIntent(SeasonsContract.Intent.SelectSeason(index))
                                    }
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = season.number.toString(),
                                    fontSize = 14.sp,
                                    color = if (isSelected) Color.White else Color.Black,
                                    style = SkillTheme.typography.graphiksRegular
                                )
                            }
                        }
                    }
                }

                if (selectedSeason != null) {
                    Text(
                        modifier = Modifier.padding(horizontal = 26.dp, vertical = 12.dp),
                        text = "${selectedSeason.number} сезон, ${selectedSeason.episodes.size} серий",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 26.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        items(selectedSeason.episodes) { episode ->
                            Column {
                                Text(
                                    text = "${episode.episodeNumber} серия. ${episode.nameRu ?: episode.nameEn ?: "Введение"}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF272727)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = episode.releaseDate ?: "Неизвестно",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("Нет информации о сезонах", color = Color.Gray)
                }
            }
        }
    }
}

