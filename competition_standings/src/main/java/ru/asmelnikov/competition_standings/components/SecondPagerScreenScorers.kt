package ru.asmelnikov.competition_standings.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.asmelnikov.domain.models.Scorer
import ru.asmelnikov.utils.ui.theme.dimens

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SecondPagerScreenScorers(
    scorers: List<Scorer>,
    seasons: List<String>,
    currentSeasonScorers: String,
    onSeasonScorersUpdate: (String) -> Unit,
    isLoadingScorers: Boolean
) {

    Column(modifier = Modifier.fillMaxSize()) {
        SeasonDropDown(
            modifier = Modifier.fillMaxWidth(0.5f),
            onItemChanged = onSeasonScorersUpdate,
            items = seasons,
            selectedItem = currentSeasonScorers
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.dimens.extraSmall1)
        ) {
            if (isLoadingScorers) LinearProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.primary
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Divider(color = MaterialTheme.colorScheme.primary)
                ScorerItemEmpty()
                Divider(color = MaterialTheme.colorScheme.primary)
            }
            itemsIndexed(
                items = scorers,
                key = { _, scorer -> scorer.player.id }) { index, scorer ->
                ScorerItem(
                    modifier = Modifier.animateItemPlacement(),
                    scorer = scorer,
                    index = index + 1
                )
                Divider(color = MaterialTheme.colorScheme.primary)
            }
            item {
                BottomScorerItem()
            }
        }
    }
}
