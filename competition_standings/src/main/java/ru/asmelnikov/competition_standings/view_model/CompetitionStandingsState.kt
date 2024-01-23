package ru.asmelnikov.competition_standings.view_model

import android.os.Parcelable
import androidx.compose.material3.SnackbarDuration
import kotlinx.parcelize.Parcelize
import ru.asmelnikov.domain.models.CompetitionStandings
import ru.asmelnikov.domain.models.Scorer
import ru.asmelnikov.domain.models.Season


@Parcelize
data class CompetitionStandingsState(
    val compId: String = "",
    val competitionStandings: CompetitionStandings? = null,
    val isLoadingStandings: Boolean = false,
    val isLoadingScorers: Boolean = false,
    val seasons: List<Season> = emptyList(),
    val currentSeasonStandings: String = "",
    val scorers: List<Scorer> = emptyList(),
    val currentSeasonScorers: String = ""
) : Parcelable

sealed class CompetitionStandingSideEffects {
    data class Snackbar(val text: String, val duration: SnackbarDuration = SnackbarDuration.Short) :
        CompetitionStandingSideEffects()

    object BackClick : CompetitionStandingSideEffects()

}
