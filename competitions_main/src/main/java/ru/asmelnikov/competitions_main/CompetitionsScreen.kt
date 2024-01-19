@file:Suppress("DEPRECATION")

package ru.asmelnikov.competitions_main

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.asmelnikov.competitions_main.components.CompetitionItem
import ru.asmelnikov.competitions_main.components.GifImage
import ru.asmelnikov.competitions_main.view_model.CompetitionsScreenSideEffects
import ru.asmelnikov.competitions_main.view_model.CompetitionsScreenViewModel
import ru.asmelnikov.domain.models.Competition
import ru.asmelnikov.utils.MainAppState
import ru.asmelnikov.utils.R

@Composable
fun CompetitionsScreen(
    appState: MainAppState,
    viewModel: CompetitionsScreenViewModel = koinViewModel(),
    showSnackbar: (
        String,
        SnackbarDuration,
        String?,
        actionPerformed: () -> Unit
    ) -> Unit
) {

    val state by viewModel.container.stateFlow.collectAsState()

    viewModel.collectSideEffect {
        when (it) {
            is CompetitionsScreenSideEffects.Snackbar -> showSnackbar(
                it.text,
                it.duration,
                null
            ) {}
        }
    }

    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = state.isLoadingRemote
    )

    CompetitionsScreenContent(
        comps = state.comps,
        updateComps = viewModel::updateCompetitionsFromRemoteToLocal,
        swipeRefreshState = swipeRefreshState,
        isLocalLoading = state.isLoadingLocal
    )

}

@Composable
fun CompetitionsScreenContent(
    comps: List<Competition>,
    updateComps: () -> Unit,
    swipeRefreshState: SwipeRefreshState,
    isLocalLoading: Boolean
) {

    val state = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        modifier = Modifier,
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

            GifImage(
                modifier = Modifier.fillMaxWidth(),
                alpha = state.toolbarState.progress
            )

            Text(
                text = when (state.toolbarState.progress) {
                    0f -> "Available competitions"
                    else -> "Goal pulse!"
                },
                style = TextStyle(color = Color.Black, fontSize = textSize),
                modifier = Modifier
                    .padding(16.dp)
                    .road(whenCollapsed = Alignment.TopStart, whenExpanded = Alignment.BottomEnd)
            )

        }) {

        SwipeRefresh(
            swipeEnabled = state.toolbarState.progress == 1f,
            state = swipeRefreshState,
            onRefresh = updateComps
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(0.dp))
                }
                items(items = comps, key = { it.id }) { comp ->
                    CompetitionItem(competition = comp)
                }
            }
        }

    }
}