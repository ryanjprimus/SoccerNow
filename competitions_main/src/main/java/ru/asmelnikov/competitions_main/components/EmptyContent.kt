package ru.asmelnikov.competitions_main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.asmelnikov.utils.R

@Composable
fun EmptyContent(
    onReloadClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        Image(
            modifier = Modifier.size(240.dp),
            painter = painterResource(id = R.drawable.page_is_empty),
            contentDescription = null
        )
        TextButton(onClick = onReloadClick) {
            Text(text = "Reload", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 32.sp))
        }
    }
}