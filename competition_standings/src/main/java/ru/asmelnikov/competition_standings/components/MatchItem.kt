package ru.asmelnikov.competition_standings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.asmelnikov.domain.models.Match
import ru.asmelnikov.utils.composables.SubComposeAsyncImageCommon
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun MatchItem(
    modifier: Modifier = Modifier,
    match: Match,
    isAhead: Boolean
) {

    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.dimens.medium1),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            SubComposeAsyncImageCommon(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1),
                imageUri = match.homeTeam.crest,
                shape = RoundedCornerShape(0.dp),
                size = MaterialTheme.dimens.medium4
            )
            if (isAhead) {
                Text(
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimens.medium1),
                    text = match.bigDate,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            } else {
                ScoreItem(
                    match.score.fullTime.home.toString(),
                    match.score.fullTime.away.toString()
                )
            }
            SubComposeAsyncImageCommon(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1),
                imageUri = match.awayTeam.crest,
                shape = RoundedCornerShape(0.dp),
                size = MaterialTheme.dimens.medium4
            )
        }
        Text(
            modifier = modifier.padding(top = MaterialTheme.dimens.small3),
            text = "${match.homeTeam.shortName} - ${match.awayTeam.shortName}",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = modifier.padding(bottom = MaterialTheme.dimens.medium1),
            text = match.utcDate,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun ScoreItem(
    scoreHome: String,
    scoreAway: String
) {
    Card(
        modifier = Modifier.padding(horizontal = MaterialTheme.dimens.medium1),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
    ) {
        Row(
            modifier = Modifier.padding(MaterialTheme.dimens.small1),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$scoreHome - $scoreAway",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
