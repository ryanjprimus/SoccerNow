package com.primus.data.mappers

import io.realm.RealmList
import com.primus.data.local.models.MatchEntity
import com.primus.data.local.models.TeamMatchesEntity
import com.primus.data.models.CompetitionMatchesDTO
import com.primus.data.models.MatchDTO
import com.primus.domain.models.TeamMatches

fun CompetitionMatchesDTO.toTeamMatchesEntity(teamId: String): TeamMatchesEntity {
    return TeamMatchesEntity(
        id = teamId,
        season = createYearRange(
            this.matches?.firstOrNull()?.season?.startDate ?: "",
            this.matches?.firstOrNull()?.season?.endDate ?: ""
        ),
        matchesCompleted = groupMatchesByDateCompletedDTO(matches ?: emptyList()),
        matchesAhead = groupMatchesByDateAheadDTO(matches ?: emptyList())
    )
}

fun groupMatchesByDateCompletedDTO(matches: List<MatchDTO>): RealmList<MatchEntity> {
    return RealmList<MatchEntity>().apply {
        addAll(matches.filter { it.status == "FINISHED" && it.homeTeam?.id != null && it.awayTeam?.id != null }
            .sortedByDescending { it.utcDate }
            .map { it.toMatchEntity() })
    }
}

fun groupMatchesByDateAheadDTO(matches: List<MatchDTO>): RealmList<MatchEntity> {
    return RealmList<MatchEntity>().apply {
        addAll(matches.filter { it.status != "FINISHED" && it.homeTeam?.id != null && it.awayTeam?.id != null }
            .sortedBy { it.utcDate }
            .map { it.toMatchEntity() })
    }
}

fun TeamMatchesEntity.toTeamMatches(): TeamMatches {
    return TeamMatches(
        id = id,
        season = season,
        matchesCompleted = matchesCompleted?.map { it.toMatches() } ?: emptyList(),
        matchesAhead = matchesAhead?.map { it.toMatches() } ?: emptyList()
    )
}