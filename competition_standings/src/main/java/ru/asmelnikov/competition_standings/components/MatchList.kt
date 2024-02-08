package ru.asmelnikov.competition_standings.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.asmelnikov.domain.models.MatchesByTour

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MatchList(
    matchesCompleted: List<MatchesByTour>,
    matchesAhead: List<MatchesByTour>,
    isAhead: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        val list = if (!isAhead) matchesCompleted else matchesAhead
        list.forEach { matchesByTour ->
            stickyHeader {
                StickyHeader(matchesByTour = matchesByTour)
            }
            itemsIndexed(
                items = matchesByTour.matches,
                key = { _, match -> match.id }
            ) { index, match ->
                MatchItem(
                    modifier = Modifier.animateItemPlacement(),
                    match = match,
                    isAhead = isAhead
                )
                if (index < matchesByTour.matches.size - 1) {
                    Divider(color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}