package com.primus.utils.composables

import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class MainAppState(
    val snackbarState: SnackbarHostState,
    val snackbarScope: CoroutineScope,
    val navController: NavHostController,
    val bottomSheetState: SheetState,
    val bottomSheetScaffoldState: BottomSheetScaffoldState
) {

    fun showSnackbar(
        message: String,
        duration: SnackbarDuration = SnackbarDuration.Short,
        actionLabel: String? = null,
        actionPerformed: () -> Unit
    ) {

        snackbarScope.launch {
            snackbarState.currentSnackbarData?.dismiss()
            val snackResult = snackbarState.showSnackbar(
                message = message,
                duration = duration,
                actionLabel = actionLabel
            )
            when (snackResult) {
                SnackbarResult.ActionPerformed -> actionPerformed()
                SnackbarResult.Dismissed -> {}
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberAppState(
    snackbarState: SnackbarHostState = remember {
        SnackbarHostState()
    },
    navController: NavHostController = rememberNavController(),
    snackbarScope: CoroutineScope = rememberCoroutineScope(),
    bottomSheetState: SheetState = rememberModalBottomSheetState(),
    bottomSheetScaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState()
) = remember(snackbarState, navController, snackbarScope) {
    MainAppState(
        snackbarState = snackbarState,
        snackbarScope = snackbarScope,
        navController = navController,
        bottomSheetState = bottomSheetState,
        bottomSheetScaffoldState = bottomSheetScaffoldState
    )
}