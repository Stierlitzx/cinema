package kz.stierlitz.skillcinema.presentation.film

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kz.stierlitz.skillcinema.R
import kz.stierlitz.skillcinema.presentation.film.components.StaffListRow
import kz.stierlitz.skillcinema.presentation.filmography.FilmographyItem
import kz.stierlitz.skillcinema.ui.theme.SkillTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmScreen(
    filmId: Int,
    viewModel: FilmViewModel = viewModel(),
    onBack: () -> Unit = {},
    onNavigateToSeasons: (Int, String) -> Unit = { _, _ -> },
    onNavigateToActor: (Int) -> Unit = {},
    onNavigateToGallery: (Int) -> Unit = {}
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var showCreateDialog by remember { mutableStateOf(false) }

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

    if (showCreateDialog) {
        var collectionName by remember { mutableStateOf("") }
        androidx.compose.ui.window.Dialog(onDismissRequest = { showCreateDialog = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                Column {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        androidx.compose.foundation.text.BasicTextField(
                            value = collectionName,
                            onValueChange = { collectionName = it },
                            textStyle = androidx.compose.ui.text.TextStyle(
                                fontSize = 16.sp,
                                color = Color(0xFF272727)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 24.dp),
                            decorationBox = { innerTextField ->
                                if (collectionName.isEmpty()) {
                                    Text(
                                        text = "Придумайте название\nдля вашей новой коллекции",
                                        fontSize = 16.sp,
                                        color = Color(0xFF8D8D8D)
                                    )
                                }
                                innerTextField()
                            }
                        )
                        Text(
                            text = "✕",
                            fontSize = 18.sp,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .clickable { showCreateDialog = false }
                        )
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    androidx.compose.material3.Button(
                        onClick = {
                            val trimmedName = collectionName.trim().replace("\n", "")
                            if (trimmedName.isNotBlank()) {
                                viewModel.handleIntent(FilmContract.Intent.CreateCollection(trimmedName))
                                showCreateDialog = false
                            }
                        },
                        modifier = Modifier.align(Alignment.End),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color(0xFF3D3BFF))
                    ) {
                        Text("Готово", color = Color.White)
                    }
                }
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

    val favoriteCollection = state.collections.firstOrNull { it.name == "Любимые" }
    val isFavorite = favoriteCollection?.id?.let { state.movieCollections.contains(it) } == true

    val bookmarkCollection = state.collections.firstOrNull { it.name == "Хочу посмотреть" }
    val isBookmarked = bookmarkCollection?.id?.let { state.movieCollections.contains(it) } == true

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(modifier = Modifier.padding(bottom = 32.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    FilmographyItem(
                        film = film,
                        onClick = {}
                    )
                    Text(
                        text = "✕",
                        fontSize = 18.sp,
                        color = Color(0xFF272727),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .clickable { showBottomSheet = false }
                    )
                }

                HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add_collection),
                        contentDescription = null,
                        tint = Color(0xFF272727),
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Добавить в коллекцию",
                        fontSize = 18.sp,
                        color = Color(0xFF272727)
                    )
                }

                HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)

                // List of collections
                state.collections.forEach { collection ->
                    val isIncluded = state.movieCollections.contains(collection.id)
                    val count = state.collectionCounts[collection.id] ?: 0
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.handleIntent(FilmContract.Intent.ToggleCollectionById(collection.id)) }
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Custom Checkbox representation
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .border(
                                    1.dp,
                                    if (isIncluded) Color.Transparent else Color(0xFF272727),
                                    RoundedCornerShape(4.dp)
                                )
                                .background(
                                    if (isIncluded) Color(0xFF3D3BFF) else Color.Transparent,
                                    RoundedCornerShape(4.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isIncluded) {
                                Text(
                                    text = "✓",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = collection.name,
                            fontSize = 16.sp,
                            color = Color(0xFF272727),
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = count.toString(),
                            fontSize = 16.sp,
                            color = Color(0xFF272727)
                        )
                    }
                    HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp, modifier = Modifier.padding(start = 64.dp))
                }

                // Add custom collection row with icon
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showCreateDialog = true }
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "+", fontSize = 24.sp, color = Color(0xFF272727))
                    Spacer(modifier = Modifier.width(24.dp))
                    Text(text = "Создать свою коллекцию", fontSize = 16.sp, color = Color(0xFF272727))
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
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
                    text = "${film.year ?: ""}, ${film.genres.joinToString(", ") { it.name }}",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
                Text(
                    text = "${film.countries.firstOrNull()?.name ?: ""}, ${film.filmLength?.let { "$it мин" } ?: ""}, ${film.ratingAgeLimits?.replace("age", "") ?: ""}+",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { viewModel.handleIntent(FilmContract.Intent.ToggleCollectionByName("Любимые")) }) {
                        Icon(painter = painterResource(id = R.drawable.ic_favourite), contentDescription = "Favorite", tint = if (isFavorite) Color.Red else Color.White)
                    }
                    IconButton(onClick = { viewModel.handleIntent(FilmContract.Intent.ToggleCollectionByName("Хочу посмотреть")) }) {
                        Icon(painter = painterResource(id = R.drawable.ic_bookmark), contentDescription = "Bookmark", tint = if (isBookmarked) Color(0xFF3D3BFF) else Color.White)
                    }
                    IconButton(onClick = { /* TODO: Hide */ }) {
                        Icon(painter = painterResource(id = R.drawable.ic_hide), contentDescription = "Hide", tint = Color.White)
                    }
                    IconButton(onClick = { /* TODO: Share */ }) {
                        Icon(painter = painterResource(id = R.drawable.ic_share), contentDescription = "Share", tint = Color.White)
                    }
                    IconButton(onClick = { showBottomSheet = true }) {
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

            if (state.seasons.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Сезоны и серии", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                        val episodesCount = state.seasons.sumOf { it.episodes.size }
                        Text(
                            text = "${state.seasons.size} сезон, $episodesCount серий",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    Text(
                        modifier = Modifier.clickable {
                            onNavigateToSeasons(film.kinopoiskId, film.nameRu ?: film.nameEn ?: "")
                        }.padding(start = 16.dp),
                        text = "Все",
                        style = SkillTheme.typography.graphiksMedium,
                        fontSize = 14.sp,
                        color = Color(0xFF3D3BFF)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            if (state.actors.isNotEmpty()) {
                Text(text = "В фильме снимались", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))
                StaffListRow(
                    staffList = state.actors,
                    onStaffClick = onNavigateToActor
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (state.workers.isNotEmpty()) {
                Text(text = "Над фильмом работали", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))
                StaffListRow(
                    staffList = state.workers,
                    onStaffClick = onNavigateToActor
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Галерея", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                if (state.gallery.isNotEmpty()) {
                    Text(
                        modifier = Modifier
                            .clickable { onNavigateToGallery(filmId) }
                            .padding(start = 16.dp),
                        text = "Все",
                        style = SkillTheme.typography.graphiksMedium,
                        fontSize = 14.sp,
                        color = Color(0xFF3D3BFF)
                    )
                }
            }
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

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
