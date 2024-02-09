package ru.asmelnikov.goalpulse.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.asmelnikov.competition_standings.CompetitionStandingsScreen
import ru.asmelnikov.competitions_main.CompetitionsScreen
import ru.asmelnikov.team_info.TeamInfoScreen
import ru.asmelnikov.utils.composables.MainAppState
import ru.asmelnikov.utils.navigation.Routes

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
        startDestination = Routes.Competitions_Main,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(route = Routes.Competitions_Main) {
            CompetitionsScreen(appState = appState, showSnackbar = showSnackbar)
        }
        composable(
            route = "${Routes.Competition_Standings}/{compId}",
            arguments = listOf(
                navArgument("compId") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )) {
            CompetitionStandingsScreen(
                appState = appState,
                showSnackbar = showSnackbar
            )
        }
        composable(
            route = "${Routes.Team_Info}/{teamId}",
            arguments = listOf(
                navArgument("teamId") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )) {
            TeamInfoScreen(
                appState = appState,
                showSnackbar = showSnackbar
            )
        }
    }
}