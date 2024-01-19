package ru.asmelnikov.competition_standings

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.asmelnikov.competition_standings.view_model.CompetitionStandingSideEffects
import ru.asmelnikov.competition_standings.view_model.CompetitionStandingsViewModel
import ru.asmelnikov.domain.models.CompetitionStandings
import ru.asmelnikov.domain.models.Standing
import ru.asmelnikov.utils.MainAppState
import ru.asmelnikov.utils.Routes
import ru.asmelnikov.utils.navigateWithArgs

@Composable
fun CompetitionStandingsScreen(
    appState: MainAppState,
    compId: String,
    showSnackbar: (
        String,
        SnackbarDuration,
        String?,
        actionPerformed: () -> Unit
    ) -> Unit,
    viewModel: CompetitionStandingsViewModel = koinViewModel()
) {

    LaunchedEffect(key1 = compId) {
        viewModel.compIdToState(compId)
    }

    val state by viewModel.container.stateFlow.collectAsState()

    viewModel.collectSideEffect {
        when (it) {
            is CompetitionStandingSideEffects.Snackbar -> showSnackbar(
                it.text,
                it.duration,
                null
            ) {}

        }
    }

    CompetitionStandingsContent(standings = state.compStandings)

}

@Composable
fun CompetitionStandingsContent(
    standings: List<Standing>
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        standings.forEach { standing ->
            items(standing.table) { table ->
                Row {
                    Text(text = table.position.toString())
                    Text(text = table.team.name)
                    Text(text = table.points.toString())
                }
            }
        }
    }
}