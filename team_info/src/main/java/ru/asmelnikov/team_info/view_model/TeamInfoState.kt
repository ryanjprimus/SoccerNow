package ru.asmelnikov.team_info.view_model

import android.os.Parcelable
import androidx.compose.material3.SnackbarDuration
import kotlinx.parcelize.Parcelize
import ru.asmelnikov.domain.models.TeamInfo

@Parcelize
data class TeamInfoState(
    val teamId: String = "",
    val teamInfo: TeamInfo = TeamInfo(),
    val isLoading: Boolean = false,
    val colorPalette: Map<String, String> = mapOf()
) : Parcelable

sealed class TeamInfoSideEffects {
    data class Snackbar(val text: String, val duration: SnackbarDuration = SnackbarDuration.Short) :
        TeamInfoSideEffects()

    object BackClick : TeamInfoSideEffects()

}
