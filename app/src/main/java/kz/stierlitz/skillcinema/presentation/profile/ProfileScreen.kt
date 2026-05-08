package kz.stierlitz.skillcinema.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.BoxWithConstraints
import coil.compose.AsyncImage
import kz.stierlitz.skillcinema.R
import kz.stierlitz.skillcinema.presentation.home.components.MovieCard

// Размеры совпадают с MovieCard: width=140dp, aspectRatio=2/3
private val CARD_WIDTH = 140.dp
private val CARD_ASPECT = 2f / 3f

@Composable
fun ProfileScreen(
    state: ProfileContract.State,
    onSignOut: () -> Unit,
    onIntent: (ProfileContract.Intent) -> Unit = {},
    onMovieClick: (Int) -> Unit = {},
    onNavigateToFilmList: (type: String, title: String) -> Unit = { _, _ -> }
) {
    var showCreateDialog by remember { mutableStateOf(false) }

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
                                onIntent(ProfileContract.Intent.CreateCollection(trimmedName))
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Аккаунт — слева, компактно
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (state.profilePictureUrl != null) {
                AsyncImage(
                    model = state.profilePictureUrl,
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                if (state.username != null) {
                    Text(
                        text = state.username,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF272727)
                    )
                }
                Text(
                    text = "Sign out",
                    color = Color(0xFF3D3BFF),
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onSignOut() }
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        if (state.watchedMovies.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 26.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Просмотрено",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF272727)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        onNavigateToFilmList("watched", "Просмотрено")
                    }
                ) {
                    Text(
                        text = "${state.watchedMovies.size}",
                        color = Color(0xFF3D3BFF),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "See all",
                        tint = Color(0xFF3D3BFF),
                        modifier = Modifier
                            .size(12.dp)
                            .rotate(180f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 26.dp)
            ) {
                items(state.watchedMovies) { movie ->
                    MovieCard(
                        modifier = Modifier.width(CARD_WIDTH),
                        movie = movie,
                        onClick = { onMovieClick(movie.kinopoiskId) }
                    )
                }
                item {
                    // Точно такой же размер как постер MovieCard (width=140, aspectRatio=2/3)
                    Column(modifier = Modifier.width(CARD_WIDTH)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(CARD_ASPECT)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onIntent(ProfileContract.Intent.ClearHistory) },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFF0F0F0)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_delete),
                                        contentDescription = "Clear history",
                                        tint = Color.Unspecified,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Очистить\nисторию",
                                    fontSize = 12.sp,
                                    color = Color(0xFF272727),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        // Резервируем место под текст названия и жанра — как у MovieCard
                        Spacer(modifier = Modifier.height(36.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Коллекции",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 26.dp),
            color = Color(0xFF272727)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp)
                .clickable { showCreateDialog = true },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "+", fontSize = 20.sp, color = Color(0xFF272727))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Создать свою коллекцию", fontSize = 16.sp, color = Color(0xFF272727))
        }
        Spacer(modifier = Modifier.height(16.dp))

        // BoxWithConstraints — единственный способ сделать квадраты в Row через weight
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp)
        ) {
            val cardSize = (maxWidth - 16.dp) / 2
            val chunks = state.collections.chunked(2)
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                chunks.forEach { rowCols ->
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        rowCols.forEach { col ->
                            CollectionCard(
                                modifier = Modifier.size(cardSize),
                                icon = {
                                    Icon(
                                        painter = painterResource(
                                            id = if (!col.isCustom && col.name == "Любимые") R.drawable.ic_like_profile
                                            else if (!col.isCustom && col.name == "Хочу посмотреть") R.drawable.ic_wishlist_profile
                                            else R.drawable.ic_person_profile
                                        ),
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                title = col.name,
                                count = (state.collectionCounts[col.id] ?: 0).toString(),
                                showDelete = col.isCustom,
                                onDelete = { onIntent(ProfileContract.Intent.DeleteCollection(col.id)) },
                                onClickCollection = { onNavigateToFilmList(col.name, col.name) }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun CollectionCard(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    title: String,
    count: String,
    showDelete: Boolean = false,
    onDelete: () -> Unit = {},
    onClickCollection: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .border(1.dp, Color(0xFFD0D0D0), RoundedCornerShape(8.dp))
            .clickable { onClickCollection() }
    ) {
        if (showDelete) {
            Text(
                text = "✕",
                fontSize = 14.sp,
                color = Color(0xFF272727),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .clickable { onDelete() }
            )
        }
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            icon()
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color(0xFF272727)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .background(Color(0xFF3D3BFF), CircleShape)
                    .defaultMinSize(minWidth = 24.dp, minHeight = 24.dp)
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = count, color = Color.White, fontSize = 10.sp)
            }
        }
    }
}