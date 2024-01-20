package ru.asmelnikov.competitions_main.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.mxalbert.sharedelements.FadeMode
import com.mxalbert.sharedelements.MaterialContainerTransformSpec
import com.mxalbert.sharedelements.SharedMaterialContainer
import ru.asmelnikov.domain.models.Competition
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.composables.SubComposeAsyncImageCommon
import ru.asmelnikov.utils.navigation.Routes

@Composable
fun CompetitionItem(
    competition: Competition,
    onCompClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCompClick(competition.id.toString())
            },
        shape = RoundedCornerShape(0.dp),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            SharedMaterialContainer(
                key = competition.emblem,
                screenKey = Routes.Competitions_Main,
                color = Color.Transparent,
                transitionSpec = MaterialContainerTransformSpec(
                    durationMillis = 1000,
                    fadeMode = FadeMode.Out
                )
            ) {
                SubComposeAsyncImageCommon(
                    imageUri = competition.emblem,
                    shape = if (competition.emblem == competition.area.flag) CircleShape else RoundedCornerShape(
                        0.dp
                    )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Text(
                        text = competition.name,
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    SubComposeAsyncImageCommon(
                        imageUri = competition.area.flag.ifBlank { R.drawable.unknown_flag },
                        shape = CircleShape,
                        size = 24.dp
                    )
                }

                Text(
                    text = "current match day - ${competition.currentSeason.currentMatchDay}",
                    style = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                )
                Text(
                    text = "start date - ${competition.currentSeason.startDate}",
                    style = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                )
                Text(
                    text = "end date - ${competition.currentSeason.endDate}",
                    style = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                )
            }
        }
    }
}