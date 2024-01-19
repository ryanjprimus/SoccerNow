package ru.asmelnikov.competition_standings.view_model

import android.os.Parcelable
import androidx.compose.material3.SnackbarDuration
import kotlinx.parcelize.Parcelize
import ru.asmelnikov.domain.models.CompetitionStandings
import ru.asmelnikov.domain.models.Standing


@Parcelize
data class CompetitionStandingsState(
    val compId: String = "",
    val filter: String = "",
    val compStandings: List<Standing> = emptyList(),
    val isLoading: Boolean = false
) : Parcelable

sealed class CompetitionStandingSideEffects {
    data class Snackbar(val text: String, val duration: SnackbarDuration = SnackbarDuration.Short) :
        CompetitionStandingSideEffects()

}
