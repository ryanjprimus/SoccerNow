@file:Suppress("DEPRECATION")

package com.primus.competitions_main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectSideEffect
import com.primus.competitions_main.components.CompetitionItem
import com.primus.competitions_main.components.ShimmerListItem
import com.primus.competitions_main.view_model.CompetitionsScreenSideEffects
import com.primus.competitions_main.view_model.CompetitionsScreenViewModel
import com.primus.domain.models.Competition
import com.primus.utils.composables.EmptyContent
import com.primus.utils.composables.MainAppState
import com.primus.utils.navigation.Routes
import com.primus.utils.navigation.navigateWithArgs
import com.primus.utils.ui.theme.dimens

@Composable
fun CompetitionsScreen(
    appState: MainAppState,
    showSnackbar: (
        String,
        SnackbarDuration,
        String?,
        actionPerformed: () -> Unit
    ) -> Unit,
    viewModel: CompetitionsScreenViewModel = koinViewModel()
) {

    val state by viewModel.container.stateFlow.collectAsState()

    viewModel.collectSideEffect {
        when (it) {
            is CompetitionsScreenSideEffects.Snackbar -> showSnackbar(
                it.text,
                it.duration,
                null
            ) {}

            is CompetitionsScreenSideEffects.OnCompetitionNavigate -> {
                appState.navigateWithArgs(route = Routes.Competition_Standings, args = it.compId)
            }
        }
    }

    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = state.isLoading
    )

    CompetitionsScreenContent(
        comps = state.comps,
        updateComps = viewModel::updateCompetitionsFromRemoteToLocal,
        swipeRefreshState = swipeRefreshState,
        isLoading = state.isLoading,
        onCompClick = viewModel::onCompClick
    )

}

@Composable
fun CompetitionsScreenContent(
    comps: List<Competition>,
    updateComps: () -> Unit,
    swipeRefreshState: SwipeRefreshState,
    isLoading: Boolean,
    onCompClick: (String) -> Unit
) {

    val state = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        modifier = Modifier.fillMaxSize(),
        state = state,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {

            val textSize = (18 + (30 - 12) * state.toolbarState.progress).sp

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .pin()
            )


            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.dimens.large)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
                    .road(
                        whenCollapsed = Alignment.BottomCenter,
                        whenExpanded = Alignment.BottomCenter
                    )
            )

            Text(
                text = when (state.toolbarState.progress) {
                    0f -> "Available competitions"
                    else -> "SoccerNow"
                },
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = textSize
                ),
                modifier = Modifier
                    .padding(
                        bottom = MaterialTheme.dimens.small3,
                        top = MaterialTheme.dimens.medium3,
                        start = MaterialTheme.dimens.small3,
                        end = MaterialTheme.dimens.small3
                    )
                    .road(whenCollapsed = Alignment.TopStart, whenExpanded = Alignment.BottomCenter)
            )

        }) {

        SwipeRefresh(
            swipeEnabled = state.toolbarState.progress == 1f,
            state = swipeRefreshState,
            onRefresh = updateComps
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
            ) {
                item {
                    Spacer(modifier = Modifier.height(0.dp))
                }

                when {
                    comps.isEmpty() && isLoading -> {
                        items(10) {
                            ShimmerListItem()
                        }
                    }

                    comps.isEmpty() -> {
                        item {
                            EmptyContent(
                                onReloadClick = updateComps
                            )
                        }
                    }

                    else -> {
                        items(items = comps, key = { it.id }) { comp ->
                            CompetitionItem(competition = comp, onCompClick = onCompClick)
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium4))
                }
            }
        }
    }
}


