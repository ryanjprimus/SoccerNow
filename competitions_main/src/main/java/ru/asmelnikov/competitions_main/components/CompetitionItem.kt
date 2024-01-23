package ru.asmelnikov.competitions_main.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mxalbert.sharedelements.FadeMode
import com.mxalbert.sharedelements.MaterialContainerTransformSpec
import com.mxalbert.sharedelements.SharedMaterialContainer
import ru.asmelnikov.domain.models.Competition
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.composables.SubComposeAsyncImageCommon
import ru.asmelnikov.utils.navigation.Routes
import ru.asmelnikov.utils.ui.theme.dimens

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
                .padding(MaterialTheme.dimens.medium1),
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
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.dimens.small1))

                    SubComposeAsyncImageCommon(
                        imageUri = competition.area.flag.ifBlank { R.drawable.unknown_flag },
                        shape = CircleShape,
                        size = MaterialTheme.dimens.medium2
                    )
                }

                Text(
                    text = "current match day - ${competition.currentSeason.currentMatchDay}",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = "start date - ${competition.currentSeason.startDate}",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = "end date - ${competition.currentSeason.endDate}",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}