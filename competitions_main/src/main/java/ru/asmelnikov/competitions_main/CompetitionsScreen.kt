@file:Suppress("DEPRECATION")

package ru.asmelnikov.competitions_main

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color.parseColor
import android.graphics.drawable.BitmapDrawable
import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.red
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.asmelnikov.competitions_main.components.CompetitionItem
import ru.asmelnikov.competitions_main.components.EmptyContent
import ru.asmelnikov.competitions_main.components.GifImage
import ru.asmelnikov.competitions_main.components.ShimmerListItem
import ru.asmelnikov.competitions_main.view_model.CompetitionsScreenSideEffects
import ru.asmelnikov.competitions_main.view_model.CompetitionsScreenViewModel
import ru.asmelnikov.domain.models.Competition
import ru.asmelnikov.utils.composables.MainAppState
import ru.asmelnikov.utils.navigation.Routes
import ru.asmelnikov.utils.navigation.navigateWithArgs
import ru.asmelnikov.utils.ui.theme.dimens

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

            GifImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .parallax(),
                alpha = state.toolbarState.progress
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
                    else -> "Goal pulse!"
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

@Composable
fun Test() {
    val context = LocalContext.current

    var launchedEffectTriggered by remember { mutableStateOf(false) }
    var bitmapState by remember { mutableStateOf<Bitmap?>(null) }
    var palette by remember { mutableStateOf<Palette?>(null) }

    LaunchedEffect(key1 = true) {
        try {
            val bitmap = convertImageUrlToBitmap(
                imageUrl = "https://crests.football-data.org/563.png",
                context = context
            )
            if (bitmap != null) {
                bitmapState = bitmap
                palette = Palette.from(bitmap).generate()
                launchedEffectTriggered = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    if (palette != null && launchedEffectTriggered) {
        val color = parseColorSwatch(palette!!.dominantSwatch)
        if (color != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(color = Color(parseColor(color)))
            )
        }
    }
}

suspend fun convertImageUrlToBitmap(
    imageUrl: String,
    context: Context
): Bitmap? {
    val loader = ImageLoader(context = context)
    val request = ImageRequest.Builder(context = context)
        .data(imageUrl)
        .allowHardware(false)
        .build()
    val imageResult = loader.execute(request = request)
    return if (imageResult is SuccessResult) {
        (imageResult.drawable as BitmapDrawable).bitmap
    } else {
        null
    }
}

private fun parseColorSwatch(color: Palette.Swatch?): String? {
    return if (color != null) {
        val parsedColor = Integer.toHexString(color.rgb)
        return "#$parsedColor"
    } else {
        null
    }
}


