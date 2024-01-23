package ru.asmelnikov.competition_standings.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.asmelnikov.domain.models.CompetitionStandings
import ru.asmelnikov.utils.ui.theme.dimens

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FirstPagerScreenStandings(
    competitionStandings: CompetitionStandings?,
    seasons: List<String>,
    currentSeason: String,
    onSeasonUpdate: (String) -> Unit,
    isLoading: Boolean
) {

    Column(modifier = Modifier.fillMaxSize()) {
        SeasonDropDown(
            modifier = Modifier.fillMaxWidth(0.5f),
            onItemChanged = onSeasonUpdate,
            items = seasons,
            selectedItem = currentSeason
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.dimens.extraSmall1)
        ) {
            if (isLoading) LinearProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.primary
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            competitionStandings?.standings?.forEach { standing ->
                item {
                    Divider(color = MaterialTheme.colorScheme.primary)
                    StandingTopItem(
                        tableName = standing.group.ifEmpty { "Team" }
                    )
                    Divider(color = MaterialTheme.colorScheme.primary)
                }
                items(items = standing.table, key = { it.team.id }) { table ->
                    StandingItem(modifier = Modifier.animateItemPlacement(), table = table)
                    Divider(color = MaterialTheme.colorScheme.primary)
                }
                item {
                    BottomStandingItem()
                }
            }
        }
    }
}