package ru.asmelnikov.goalpulse.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.asmelnikov.competitions_main.CompetitionsScreen
import ru.asmelnikov.utils.MainAppState
import ru.asmelnikov.utils.Routes

@Composable
fun NavGraph(
    appState: MainAppState,
    paddingValues: PaddingValues,
    showSnackbar: (
        String,
        SnackbarDuration,
        String?,
        actionPerformed: () -> Unit
    ) -> Unit,
) {

    NavHost(
        navController = appState.navController,
        startDestination = Routes.Main,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(route = Routes.Main) {
            CompetitionsScreen(appState = appState, showSnackbar = showSnackbar)
        }
    }
}