package kz.stierlitz.skillcinema.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kz.stierlitz.skillcinema.core.network.NetworkModule
import kz.stierlitz.skillcinema.data.repository.MovieRepositoryImpl
import kz.stierlitz.skillcinema.domain.repository.MovieRepository
import kz.stierlitz.skillcinema.presentation.search.filter.FilterSharedState

class SearchViewModel : ViewModel() {
    private val repository: MovieRepository = MovieRepositoryImpl(NetworkModule.kinopoiskApi)

    private val _state = MutableStateFlow(SearchContract.State())
    val state: StateFlow<SearchContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SearchContract.Effect>()
    val effect: SharedFlow<SearchContract.Effect> = _effect.asSharedFlow()

    private var searchJob: Job? = null

    fun handleIntent(intent: SearchContract.Intent) {
        when (intent) {
            is SearchContract.Intent.SearchTextChange -> {
                _state.update { it.copy(query = intent.query) }
                searchMovies(intent.query)
            }
        }
    }

    private fun searchMovies(query: String) {
        searchJob?.cancel()
        if (query.isBlank()) {
            _state.update { it.copy(results = emptyList(), isLoading = false, error = null) }
            return
        }

        searchJob = viewModelScope.launch {
            delay(500)
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val countryId  = resolveCountryId(FilterSharedState.selectedCountries.value)
                val genreId    = resolveGenreId(FilterSharedState.selectedGenres.value)
                val ratingRange = FilterSharedState.ratingRange.value
                val results = repository.getFilmsByFilters(
                    countries = countryId,
                    genres    = genreId,
                    keyword   = query,
                    ratingFrom = ratingRange.start,
                    ratingTo = ratingRange.endInclusive,
                    yearFrom = FilterSharedState.yearFrom.value,
                    yearTo = FilterSharedState.yearTo.value
                )
                _state.update { it.copy(isLoading = false, results = results) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
                _effect.emit(SearchContract.Effect.ShowError(e.message ?: "Unknown error"))
            }
        }
    }

    private val countryNameToId = mapOf(
        "Россия" to 34,
        "США" to 1,
        "Великобритания" to 68,
        "Германия" to 7,
        "Франция" to 5,
        "Италия" to 9,
        "Испания" to 26,
        "Канада" to 3,
        "Япония" to 36,
        "Южная Корея" to 46,
        "Австралия" to 16,
        "Китай" to 13
    )

    private val genreNameToId = mapOf(
        "Комедия" to 13,
        "Мелодрама" to 1,
        "Боевик" to 3,
        "Вестерн" to 11,
        "Драма" to 2,
        "Триллер" to 4,
        "Криминал" to 6,
        "Детектив" to 17,
        "Фантастика" to 5,
        "Приключения" to 9,
        "Биография" to 18,
        "Анимация" to 15,
        "Фэнтези" to 7,
        "История" to 19
    )

    private fun resolveCountryId(names: Set<String>): Int? =
        names.firstOrNull()?.let { countryNameToId[it] }

    private fun resolveGenreId(names: Set<String>): Int? =
        names.firstOrNull()?.let { genreNameToId[it] }
}
