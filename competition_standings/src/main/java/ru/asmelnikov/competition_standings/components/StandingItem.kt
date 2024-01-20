package ru.asmelnikov.competition_standings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.asmelnikov.domain.models.Table
import ru.asmelnikov.utils.composables.SubComposeAsyncImageCommon

@Composable
fun StandingItem(
    modifier: Modifier = Modifier,
    table: Table,
    dataWeight: Float = 0.08f,
    firstBoxColor: Color = Color.Transparent
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .clickable { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(dataWeight)
                .background(firstBoxColor)
                .rightBorder(strokeWidth = 1.dp, color = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = table.position.toString(),
                textAlign = TextAlign.Center
            )
        }

        SubComposeAsyncImageCommon(
            modifier = Modifier.padding(horizontal = 8.dp),
            imageUri = table.team.crest,
            shape = RoundedCornerShape(0.dp),
            size = 20.dp
        )
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.37f)
                .rightBorder(strokeWidth = 1.dp, color = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterStart),
                text = table.team.shortName,
                textAlign = TextAlign.Center
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(dataWeight)
                .rightBorder(strokeWidth = 1.dp, color = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = table.playedGames.toString(),
                textAlign = TextAlign.Center
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(dataWeight)
                .rightBorder(strokeWidth = 1.dp, color = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = table.won.toString(),
                textAlign = TextAlign.Center
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(dataWeight)
                .rightBorder(strokeWidth = 1.dp, color = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = table.draw.toString(),
                textAlign = TextAlign.Center
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(dataWeight)
                .rightBorder(strokeWidth = 1.dp, color = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = table.lost.toString(),
                textAlign = TextAlign.Center
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(dataWeight)
                .rightBorder(strokeWidth = 1.dp, color = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = table.goalsFor.toString(),
                textAlign = TextAlign.Center
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(dataWeight)
                .rightBorder(strokeWidth = 1.dp, color = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = table.goalsAgainst.toString(),
                textAlign = TextAlign.Center
            )
        }
        Text(
            modifier = Modifier.weight(dataWeight),
            text = table.points.toString(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun StandingTopItem(
    modifier: Modifier = Modifier,
    dataWeight: Float = 0.08f
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(dataWeight)
                .rightBorder(strokeWidth = 1.dp, color = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "№",
                textAlign = TextAlign.Center,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.47f)
                .rightBorder(strokeWidth = 1.dp, color = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterStart).padding(start = 8.dp),
                text = "Team",
                textAlign = TextAlign.Center,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(dataWeight)
                .rightBorder(strokeWidth = 1.dp, color = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "M",
                textAlign = TextAlign.Center,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(dataWeight)
                .rightBorder(strokeWidth = 1.dp, color = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "W",
                textAlign = TextAlign.Center,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(dataWeight)
                .rightBorder(strokeWidth = 1.dp, color = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "D",
                textAlign = TextAlign.Center,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(dataWeight)
                .rightBorder(strokeWidth = 1.dp, color = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "L",
                textAlign = TextAlign.Center,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(dataWeight)
                .rightBorder(strokeWidth = 1.dp, color = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "GF",
                textAlign = TextAlign.Center,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(dataWeight)
                .rightBorder(strokeWidth = 1.dp, color = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "GA",
                textAlign = TextAlign.Center,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
        }
        Text(
            modifier = Modifier.weight(dataWeight),
            text = "P",
            textAlign = TextAlign.Center,
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
    }
}

fun Modifier.rightBorder(strokeWidth: Dp, color: Color) =
    composed {
        composed(
            factory = {
                val density = LocalDensity.current
                val strokeWidthPx = density.run { strokeWidth.toPx() }

                Modifier.drawBehind {
                    val width = size.width - strokeWidthPx / 2
                    val height = size.height

                    drawLine(
                        color = color,
                        start = Offset(x = width, y = 0f),
                        end = Offset(x = width, y = height),
                        strokeWidth = strokeWidthPx
                    )
                }
            }
        )
    }