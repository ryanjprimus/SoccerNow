package ru.asmelnikov.competition_standings

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mxalbert.sharedelements.FadeMode
import com.mxalbert.sharedelements.MaterialContainerTransformSpec
import com.mxalbert.sharedelements.SharedMaterialContainer
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.asmelnikov.competition_standings.view_model.CompetitionStandingSideEffects
import ru.asmelnikov.competition_standings.view_model.CompetitionStandingsViewModel
import ru.asmelnikov.domain.models.CompetitionStandings
import ru.asmelnikov.domain.models.Standing
import ru.asmelnikov.utils.composables.MainAppState
import ru.asmelnikov.utils.composables.SubComposeAsyncImageCommon
import ru.asmelnikov.utils.navigation.Routes

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

    CompetitionStandingsContent(
        competitionStandings = state.competitionStandings
    )

}

@Composable
fun CompetitionStandingsContent(
    competitionStandings: CompetitionStandings?
) {

    val state = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        modifier = Modifier,
        state = state,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {

            val textSize = (18 + (30 - 12) * state.toolbarState.progress).sp

            SubComposeAsyncImageCommon(
                imageUri = competitionStandings?.area?.flag ?: "",
                shape = RoundedCornerShape(0.dp),
                size = 240.dp,
                alpha = state.toolbarState.progress,
                modifier = Modifier
                    .fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .road(
                        whenCollapsed = Alignment.TopStart,
                        whenExpanded = Alignment.Center
                    )
            ) {
                val progress = state.toolbarState.progress
                val imgSize = 40 + (100 * progress)

                SharedMaterialContainer(
                    key = competitionStandings?.competition?.emblem ?: "",
                    screenKey = Routes.Competition_Standings,
                    color = Color.Transparent,
                    transitionSpec = MaterialContainerTransformSpec(
                        durationMillis = 1000,
                        fadeMode = FadeMode.Out
                    )
                ) {
                    SubComposeAsyncImageCommon(
                        imageUri = competitionStandings?.competition?.emblem ?: "",
                        shape = RoundedCornerShape(0.dp),
                        size = imgSize.dp
                    )
                }
            }

            Text(
                text = competitionStandings?.competition?.name ?: "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(color = Color.Black, fontSize = textSize),
                modifier = Modifier
                    .padding(16.dp)
                    .road(whenCollapsed = Alignment.TopCenter, whenExpanded = Alignment.BottomEnd)
            )

        }) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            competitionStandings?.standings?.forEach { standing ->
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
}