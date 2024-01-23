package ru.asmelnikov.competition_standings

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mxalbert.sharedelements.FadeMode
import com.mxalbert.sharedelements.MaterialContainerTransformSpec
import com.mxalbert.sharedelements.SharedMaterialContainer
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ExperimentalToolbarApi
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.asmelnikov.competition_standings.components.SeasonDropDown
import ru.asmelnikov.competition_standings.components.StandingItem
import ru.asmelnikov.competition_standings.components.StandingTopItem
import ru.asmelnikov.competition_standings.view_model.CompetitionStandingSideEffects
import ru.asmelnikov.competition_standings.view_model.CompetitionStandingsViewModel
import ru.asmelnikov.domain.models.CompetitionStandings
import ru.asmelnikov.utils.composables.MainAppState
import ru.asmelnikov.utils.composables.SubComposeAsyncImageCommon
import ru.asmelnikov.utils.navigation.Routes
import ru.asmelnikov.utils.navigation.popUp
import ru.asmelnikov.utils.ui.theme.dimens

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

            is CompetitionStandingSideEffects.BackClick -> appState.popUp()

        }
    }

    CompetitionStandingsContent(
        competitionStandings = state.competitionStandings,
        seasons = state.seasons.map { it.startDateEndDate },
        currentSeason = state.currentSeason,
        onSeasonUpdate = viewModel::getStandingsBySeason,
        onBackClick = viewModel::onBackClick,
        isLoading = state.isLoading
    )

}

@OptIn(ExperimentalToolbarApi::class)
@Composable
fun CompetitionStandingsContent(
    competitionStandings: CompetitionStandings?,
    seasons: List<String>,
    onBackClick: () -> Unit,
    currentSeason: String,
    onSeasonUpdate: (String) -> Unit,
    isLoading: Boolean
) {

    val configuration = LocalConfiguration.current
    val orientation =
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) "LANDSCAPE" else "PORTRAIT"

    Box {

        val state = rememberCollapsingToolbarScaffoldState()

        LaunchedEffect(key1 = orientation) {
            if (orientation == "LANDSCAPE")
                state.toolbarState.collapse()
        }

        CollapsingToolbarScaffold(
            modifier = Modifier.fillMaxSize(),
            state = state,
            enabled = orientation == "PORTRAIT",
            scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
            toolbar = {
                val progress = state.toolbarState.progress
                val textSize = (18 + (18 * progress)).sp

                SubComposeAsyncImageCommon(
                    imageUri = competitionStandings?.area?.flag ?: "",
                    shape = RoundedCornerShape(0.dp),
                    size = MaterialTheme.dimens.emptyContentImageSize,
                    alpha = state.toolbarState.progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .parallax()
                        .pin()
                )

                Box(
                    modifier = Modifier
                        .padding(
                            horizontal = MaterialTheme.dimens.small1,
                            vertical = MaterialTheme.dimens.medium2
                        )
                        .road(
                            whenCollapsed = Alignment.TopEnd,
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

                Text(
                    text = competitionStandings?.competition?.name ?: "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = textSize,
                    modifier = Modifier
                        .padding(
                            bottom = MaterialTheme.dimens.medium1,
                            top = MaterialTheme.dimens.medium3
                        )
                        .road(
                            whenCollapsed = Alignment.TopCenter,
                            whenExpanded = Alignment.BottomCenter
                        )
                )

            }) {

            Column(modifier = Modifier.fillMaxSize()) {
                SeasonDropDown(
                    modifier = Modifier.fillMaxWidth(0.5f),
                    onItemChanged = onSeasonUpdate,
                    items = seasons,
                    selectedItem = currentSeason
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(MaterialTheme.dimens.extraSmall1)
                ) {
                    if (isLoading) LinearProgressIndicator(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.Red
                    )
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    competitionStandings?.standings?.forEach { standing ->
                        item {
                            Divider(color = MaterialTheme.colorScheme.primary)
                            StandingTopItem(
                                tableName = standing.group.ifEmpty { "Team" }
                            )
                            Divider(color = MaterialTheme.colorScheme.primary)
                        }
                        items(items = standing.table) { table ->
                            StandingItem(table = table)
                            Divider(color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }

        }
        IconButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = MaterialTheme.dimens.medium1), onClick = onBackClick
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
