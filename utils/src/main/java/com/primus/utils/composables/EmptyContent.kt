package com.primus.utils.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.primus.utils.R
import com.primus.utils.ui.theme.dimens

@Composable
fun EmptyContent(
    onReloadClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.large))
        Image(
            modifier = Modifier.size(MaterialTheme.dimens.emptyContentImageSize),
            painter = painterResource(id = R.drawable.page_is_empty),
            contentDescription = null
        )
        TextButton(onClick = onReloadClick) {
            Text(text = "Reload", style = MaterialTheme.typography.headlineLarge)
        }
    }
}