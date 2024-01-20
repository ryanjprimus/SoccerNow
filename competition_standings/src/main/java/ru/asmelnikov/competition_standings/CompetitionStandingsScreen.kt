package ru.asmelnikov.competition_standings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import ru.asmelnikov.competition_standings.components.StandingItem
import ru.asmelnikov.competition_standings.components.StandingTopItem
import ru.asmelnikov.competition_standings.view_model.CompetitionStandingSideEffects
import ru.asmelnikov.competition_standings.view_model.CompetitionStandingsViewModel
import ru.asmelnikov.domain.models.CompetitionStandings
import ru.asmelnikov.utils.composables.MainAppState
import ru.asmelnikov.utils.composables.SubComposeAsyncImageCommon
import ru.asmelnikov.utils.navigation.Routes
import ru.asmelnikov.utils.navigation.popUp

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
        competitionStandings = state.competitionStandings,
        onBackClick = {
            appState.popUp()
        }
    )

}

@Composable
fun CompetitionStandingsContent(
    competitionStandings: CompetitionStandings?,
    onBackClick: () -> Unit
) {

    val state = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        modifier = Modifier,
        state = state,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
            val progress = state.toolbarState.progress
            val textSize = (18 + (30 - 12) * progress).sp
            val mainImageStartPadding = (40 - (40 * progress)).dp

            SubComposeAsyncImageCommon(
                imageUri = competitionStandings?.area?.flag ?: "",
                shape = RoundedCornerShape(0.dp),
                size = 240.dp,
                alpha = state.toolbarState.progress,
                modifier = Modifier
                    .fillMaxWidth().parallax()
            )

            Box(
                modifier = Modifier
                    .padding(
                        start = mainImageStartPadding,
                        top = 8.dp,
                        bottom = 8.dp
                    )
                    .road(
                        whenCollapsed = Alignment.TopStart,
                        whenExpanded = Alignment.Center
                    )
            ) {

                val imgSize = (40 + (100 * progress)).dp

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
                        size = imgSize
                    )
                }
            }

            IconButton(onClick = onBackClick) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
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
            modifier = Modifier.fillMaxSize()
        ) {
            competitionStandings?.standings?.forEach { standing ->
                item {
                    Divider(color = MaterialTheme.colorScheme.primary)
                    StandingTopItem()
                    Divider(color = MaterialTheme.colorScheme.primary)
                }
                items(items = standing.table, key = { it.team.id }) { table ->
                    StandingItem(table = table)
                    Divider(color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}
