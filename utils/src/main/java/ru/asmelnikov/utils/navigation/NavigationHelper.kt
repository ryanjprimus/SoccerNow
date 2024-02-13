package ru.asmelnikov.utils.navigation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import ru.asmelnikov.utils.composables.MainAppState

fun MainAppState.popUp() {
    if (navController.canGoBack)
        navController.popBackStack()
}

fun MainAppState.navigate(route: String) {
    navController.navigate(route) {
        launchSingleTop = true
    }
}

fun MainAppState.navigateWithArgs(route: String, args: String) {
    if (navController.canGoBack) {
        navController.navigate("$route/$args") {
            launchSingleTop = true
        }
    }
}

fun MainAppState.navigateAndPopUp(route: String, popUp: String) {
    navController.navigate(route) {
        launchSingleTop = true
        popUpTo(popUp) { inclusive = true }
    }
}

fun MainAppState.navigateSaved(route: String, popUp: String) {
    navController.navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(popUp) { saveState = true }
    }
}

fun MainAppState.clearAndNavigate(route: String) {
    navController.navigate(route) {
        launchSingleTop = true
        popUpTo(0) { inclusive = true }
    }
}

fun MainAppState.navigateToTab(route: String) {
    navController.navigate(route) {
        launchSingleTop = true
        restoreState = true
        navController.graph.startDestinationRoute?.let { screenRoute ->
            popUpTo(screenRoute) {
                saveState = true
            }
        }
    }
}

val NavHostController.canGoBack: Boolean
    get() = this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED