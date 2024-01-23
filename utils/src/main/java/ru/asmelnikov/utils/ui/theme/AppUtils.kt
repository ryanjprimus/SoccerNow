package ru.asmelnikov.utils.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember

@Composable
fun AppUtils(
    appDimens: Dimens,
    content: @Composable () -> Unit
) {

    val appDimens = remember {
        appDimens
    }

    CompositionLocalProvider(LocalAppDimens provides appDimens) {
        content()
    }

}

val LocalAppDimens = compositionLocalOf {
    CompactDimens
}

val MaterialTheme.dimens
    @Composable
    get() = LocalAppDimens.current