package ru.asmelnikov.team_info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.asmelnikov.domain.models.TeamInfo
import ru.asmelnikov.team_info.view_model.TeamInfoSideEffects
import ru.asmelnikov.team_info.view_model.TeamInfoViewModel
import ru.asmelnikov.utils.composables.MainAppState
import ru.asmelnikov.utils.navigation.popUp

@Composable
fun TeamInfoScreen(
    appState: MainAppState,
    showSnackbar: (
        String,
        SnackbarDuration,
        String?,
        actionPerformed: () -> Unit
    ) -> Unit,
    viewModel: TeamInfoViewModel = koinViewModel()
) {

    val state by viewModel.container.stateFlow.collectAsState()


    viewModel.collectSideEffect {
        when (it) {
            is TeamInfoSideEffects.Snackbar -> showSnackbar(
                it.text,
                it.duration,
                null
            ) {}

            is TeamInfoSideEffects.BackClick -> appState.popUp()

        }
    }

    TeamInfoScreenContent(teamInfo = state.teamInfo, isLoading = state.isLoading)

}

@Composable
fun TeamInfoScreenContent(
    teamInfo: TeamInfo,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        }
        if (teamInfo.name.isNotEmpty()) {
            Text(text = teamInfo.name)
            Text(text = teamInfo.address)
            Text(text = teamInfo.venue)
            Text(text = teamInfo.website)
        }
    }
}