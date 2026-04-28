package kz.stierlitz.skillcinema.presentation.seasons

import kz.stierlitz.skillcinema.domain.model.Season

interface SeasonsContract {
    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val seasons: List<Season> = emptyList(),
        val selectedSeasonIndex: Int = 0,
        val filmName: String = ""
    )

    sealed class Intent {
        data class LoadSeasons(val filmId: Int, val filmName: String) : Intent()
        data class SelectSeason(val index: Int) : Intent()
    }

    sealed class Effect {
        data class ShowError(val message: String) : Effect()
    }
}

