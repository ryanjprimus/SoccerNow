package com.primus.competition_standings.view_model

import android.os.Parcelable
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import com.primus.domain.models.CompetitionStandings
import com.primus.domain.models.Head2head
import com.primus.domain.models.MatchesByTour
import com.primus.domain.models.Scorer
import com.primus.domain.models.Season

@Immutable
@Parcelize
data class CompetitionStandingsState(
    val compId: String = "",
    val competitionStandings: CompetitionStandings? = null,
    val matchesCompleted: List<MatchesByTour> = emptyList(),
    val matchesAhead: List<MatchesByTour> = emptyList(),
    val isLoadingStandings: Boolean = false,
    val isLoadingScorers: Boolean = false,
    val isLoadingMatches: Boolean = false,
    val seasons: List<Season> = emptyList(),
    val currentSeasonStandings: String = "",
    val currentSeasonMatches: String = "",
    val scorers: List<Scorer> = emptyList(),
    val currentSeasonScorers: String = "",
    val expandedItem: Int = -1,
    val head2head: Head2head = Head2head(),
    val isHead2headLoading: Boolean = false
) : Parcelable

sealed class CompetitionStandingSideEffects {
    data class Snackbar(val text: String, val duration: SnackbarDuration = SnackbarDuration.Short) :
        CompetitionStandingSideEffects()

    object BackClick : CompetitionStandingSideEffects()

    data class OnTeamInfoNavigate(val teamId: String) :
        CompetitionStandingSideEffects()

    data class OnPersonInfoNavigate(val personId: String) :
        CompetitionStandingSideEffects()

}
