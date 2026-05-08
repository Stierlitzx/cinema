package kz.stierlitz.skillcinema.presentation.search.filter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import kz.stierlitz.skillcinema.R
import kz.stierlitz.skillcinema.ui.theme.BlueAccent
import kz.stierlitz.skillcinema.ui.theme.DividerColor
import kz.stierlitz.skillcinema.ui.theme.GrayText
import kz.stierlitz.skillcinema.ui.theme.SkillTheme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SliderDefaults
import androidx.compose.ui.text.style.TextOverflow

object FilterSharedState {
    var selectedCountries = mutableStateOf(setOf<String>())
    var selectedGenres = mutableStateOf(setOf<String>())
    var ratingRange = mutableStateOf(1f..10f)
    var yearFrom = mutableStateOf<Int?>(null)
    var yearTo = mutableStateOf<Int?>(null)
    var selectedTypeIndex = mutableIntStateOf(0)
    var selectedSortIndex = mutableIntStateOf(0)
}

@Composable
fun SegmentedControl(
    items: List<String>,
    selectedIndex: Int,
    onItemSelection: (selectedIndex: Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = selectedIndex == index
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(if (isSelected) BlueAccent else Color.White)
                    .clickable { onItemSelection(index) }
                    .then(
                        if (index < items.size - 1) {
                            Modifier.border(0.5.dp, Color.Black)
                        } else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item,
                    color = if (isSelected) Color.White else Color.Black,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    onBack: () -> Unit,
    onNavigateToCountry: () -> Unit,
    onNavigateToGenre: () -> Unit,
    onNavigateToYear: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки поиска", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(painter = painterResource(id = R.drawable.ic_arrow_back), contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            var selectedTypeIndex by FilterSharedState.selectedTypeIndex
            var selectedSortIndex by FilterSharedState.selectedSortIndex
            var ratingRange by FilterSharedState.ratingRange
            val selectedCountries by FilterSharedState.selectedCountries
            val selectedGenres by FilterSharedState.selectedGenres

            Column(modifier = Modifier.padding(horizontal = 26.dp)) {
                Spacer(modifier = Modifier.height(24.dp))
                
                Text("Показывать", fontSize = 14.sp, color = GrayText, modifier = Modifier.padding(bottom = 8.dp))
                SegmentedControl(
                    items = listOf("Все", "Фильмы", "Сериалы"),
                    selectedIndex = selectedTypeIndex,
                    onItemSelection = { selectedTypeIndex = it }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            HorizontalDivider(color = DividerColor)

            Row(modifier = Modifier.fillMaxWidth().clickable { onNavigateToCountry() }.padding(horizontal = 26.dp, vertical = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Страна", fontSize = 14.sp)
                Text(
                    text = if (selectedCountries.isEmpty()) "Любая страна" else selectedCountries.joinToString(", "),
                    fontSize = 14.sp,
                    color = GrayText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false).padding(start = 16.dp),
                    textAlign = TextAlign.End
                )
            }
            
            HorizontalDivider(color = DividerColor)

            Row(modifier = Modifier.fillMaxWidth().clickable { onNavigateToGenre() }.padding(horizontal = 26.dp, vertical = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Жанр", fontSize = 14.sp)
                Text(
                    text = if (selectedGenres.isEmpty()) "Любой жанр" else selectedGenres.joinToString(", "),
                    fontSize = 14.sp,
                    color = GrayText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false).padding(start = 16.dp),
                    textAlign = TextAlign.End
                )
            }
            
            HorizontalDivider(color = DividerColor)

            Row(modifier = Modifier.fillMaxWidth().clickable { onNavigateToYear() }.padding(horizontal = 26.dp, vertical = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Год", fontSize = 14.sp)
                val from = FilterSharedState.yearFrom.value
                val to = FilterSharedState.yearTo.value
                val periodText = if (from == null && to == null) "любой"
                else if (from != null && to == null) "с $from"
                else if (from == null && to != null) "до $to"
                else "с $from до $to"
                Text(periodText, fontSize = 14.sp, color = GrayText)
            }
            
            HorizontalDivider(color = DividerColor)
            
            Column(modifier = Modifier.padding(horizontal = 26.dp, vertical = 16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Рейтинг", fontSize = 14.sp)
                    Text(if (ratingRange == 1f..10f) "любой" else "${ratingRange.start.toInt()} - ${ratingRange.endInclusive.toInt()}", fontSize = 14.sp, color = GrayText)
                }
                
                RangeSlider(
                    value = ratingRange,
                    onValueChange = { ratingRange = it },
                    valueRange = 1f..10f,
                    steps = 8,
                    colors = SliderDefaults.colors(
                        thumbColor = BlueAccent,
                        activeTrackColor = BlueAccent,
                        inactiveTrackColor = DividerColor,
                        activeTickColor = Color.White,
                        inactiveTickColor = Color.White
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
                
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("1", fontSize = 12.sp, color = GrayText)
                    Text("10", fontSize = 12.sp, color = GrayText)
                }
            }
            
            HorizontalDivider(color = DividerColor)
            
            Column(modifier = Modifier.padding(horizontal = 26.dp, vertical = 16.dp)) {
                Text("Сортировать", fontSize = 14.sp, color = GrayText, modifier = Modifier.padding(bottom = 8.dp))
                SegmentedControl(
                    items = listOf("Дата", "Популярность", "Рейтинг"),
                    selectedIndex = selectedSortIndex,
                    onItemSelection = { selectedSortIndex = it }
                )
            }
            
            HorizontalDivider(color = DividerColor)
            
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 26.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                // Assuming you have an eye off icon, replacing with a standard icon for now. You might need to add it if it doesn't exist.
                // Icon(painter = painterResource(id = R.drawable.ic_eye_off), contentDescription = null, modifier = Modifier.size(24.dp))
                Text("Не просмотрен", fontSize = 14.sp, modifier = Modifier.padding(start = 16.dp))
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(26.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlueAccent),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Применить", fontSize = 16.sp, color = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryFilterScreen(onBack: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCountries by FilterSharedState.selectedCountries

    val countries = listOf("Россия", "Великобритания", "Германия", "США", "Франция", "Италия", "Испания", "Канада", "Япония", "Южная Корея", "Австралия", "Китай")
    val filteredCountries = if (searchQuery.isEmpty()) {
        listOf("Любая страна") + countries
    } else {
        countries.filter { it.contains(searchQuery, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Страна", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(painter = painterResource(id = R.drawable.ic_arrow_back), contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 26.dp, vertical = 8.dp),
                placeholder = { Text("Введите страну", color = GrayText) },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = DividerColor.copy(alpha = 0.5f),
                    unfocusedContainerColor = DividerColor.copy(alpha = 0.5f)
                ),
                singleLine = true
            )
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(filteredCountries) { country ->
                    val isSelected = if (country == "Любая страна") selectedCountries.isEmpty() else selectedCountries.contains(country)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (country == "Любая страна") {
                                    selectedCountries = emptySet()
                                } else {
                                    selectedCountries = if (isSelected) selectedCountries - country else selectedCountries + country
                                }
                            }
                            .padding(horizontal = 26.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = country,
                            fontSize = 16.sp
                        )
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = null,
                            colors = CheckboxDefaults.colors(checkedColor = BlueAccent)
                        )
                    }
                    HorizontalDivider(color = DividerColor)
                }
            }
            
            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(26.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlueAccent),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Выбрать", fontSize = 16.sp, color = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenreFilterScreen(onBack: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedGenres by FilterSharedState.selectedGenres

    val genres = listOf("Комедия", "Мелодрама", "Боевик", "Вестерн", "Драма", "Триллер", "Криминал", "Детектив", "Фантастика", "Приключения", "Биография", "Анимация", "Фэнтези", "История")
    val filteredGenres = if (searchQuery.isEmpty()) {
        listOf("Любой жанр") + genres
    } else {
        genres.filter { it.contains(searchQuery, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Жанр", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(painter = painterResource(id = R.drawable.ic_arrow_back), contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 26.dp, vertical = 8.dp),
                placeholder = { Text("Введите жанр", color = GrayText) },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = DividerColor.copy(alpha = 0.5f),
                    unfocusedContainerColor = DividerColor.copy(alpha = 0.5f)
                ),
                singleLine = true
            )
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(filteredGenres) { genre ->
                    val isSelected = if (genre == "Любой жанр") selectedGenres.isEmpty() else selectedGenres.contains(genre)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (genre == "Любой жанр") {
                                    selectedGenres = emptySet()
                                } else {
                                    selectedGenres = if (isSelected) selectedGenres - genre else selectedGenres + genre
                                }
                            }
                            .padding(horizontal = 26.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = genre,
                            fontSize = 16.sp
                        )
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = null,
                            colors = CheckboxDefaults.colors(checkedColor = BlueAccent)
                        )
                    }
                    HorizontalDivider(color = DividerColor)
                }
            }
            
            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(26.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlueAccent),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Выбрать", fontSize = 16.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun YearPickerControl(
    title: String,
    selectedYear: Int?,
    onYearSelected: (Int) -> Unit
) {
    var baseYear by remember { mutableIntStateOf(selectedYear?.let { it - ((it - 1998) % 12) } ?: 1998) }

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        Text(title, fontSize = 14.sp, color = GrayText, modifier = Modifier.padding(bottom = 8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$baseYear - ${baseYear + 11}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = BlueAccent
                    )
                    Row {
                        IconButton(onClick = { baseYear -= 12 }, modifier = Modifier.size(32.dp)) {
                            // You can replace the text with painterResource if you have icons
                            Text("<", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                        IconButton(onClick = { baseYear += 12 }, modifier = Modifier.size(32.dp)) {
                            Text(">", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                val years = (baseYear..baseYear + 11).toList()
                val chunkedYears = years.chunked(3)

                chunkedYears.forEach { rowYears ->
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        rowYears.forEach { year ->
                            val isSelected = year == selectedYear
                            Text(
                                text = year.toString(),
                                fontSize = 16.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) BlueAccent else Color.Black,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onYearSelected(year) }
                                    .padding(vertical = 4.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        // Handle incomplete rows if any
                        repeat(3 - rowYears.size) {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearFilterScreen(onBack: () -> Unit) {
    var yearFrom by remember { mutableStateOf(FilterSharedState.yearFrom.value) }
    var yearTo by remember { mutableStateOf(FilterSharedState.yearTo.value) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Период", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(painter = painterResource(id = R.drawable.ic_arrow_back), contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            androidx.compose.foundation.rememberScrollState().let { scrollState ->
                Column(modifier = Modifier.weight(1f).verticalScroll(scrollState).padding(horizontal = 26.dp)) {
                    Spacer(modifier = Modifier.height(16.dp))
                    YearPickerControl(
                        title = "Искать в период с",
                        selectedYear = yearFrom,
                        onYearSelected = { yearFrom = it }
                    )
                    YearPickerControl(
                        title = "Искать в период до",
                        selectedYear = yearTo,
                        onYearSelected = { yearTo = it }
                    )
                }
            }

            Button(
                onClick = {
                    FilterSharedState.yearFrom.value = yearFrom
                    FilterSharedState.yearTo.value = yearTo
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(26.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlueAccent),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Выбрать", fontSize = 16.sp, color = Color.White)
            }
        }
    }
}