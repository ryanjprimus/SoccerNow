package com.primus.soccernow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.mxalbert.sharedelements.SharedElementsRoot
import com.primus.soccernow.navigation.NavGraph
import com.primus.soccernow.ui.theme.SoccerNowTheme
import com.primus.utils.composables.rememberAppState

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {

            val appState = rememberAppState()

            SoccerNowTheme {
                Scaffold(
                    contentWindowInsets = WindowInsets.navigationBars,
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background,
                    snackbarHost = {
                        SnackbarHost(appState.snackbarState)
                    }
                ) {
                    SharedElementsRoot{
                        NavGraph(
                            appState = appState,
                            paddingValues = it,
                            showSnackbar = { message, duration, label, action ->
                                appState.showSnackbar(
                                    message = message,
                                    duration = duration,
                                    actionLabel = label,
                                    actionPerformed = action
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}