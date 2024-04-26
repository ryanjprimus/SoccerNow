package com.primus.domain.repository

import kotlinx.coroutines.flow.Flow
import com.primus.domain.models.CompetitionMatches
import com.primus.domain.models.CompetitionScorers
import com.primus.domain.models.CompetitionStandings
import com.primus.domain.models.Head2head
import com.primus.domain.models.Season
import com.primus.utils.Resource

interface CompetitionStandingsRepository {

    suspend fun getCompetitionStandingsFromRemoteToLocalById(
        compId: String,
        season: String?
    ): Resource<CompetitionStandings>

    suspend fun getCompetitionSeasonsById(compId: String): Resource<List<Season>>

    suspend fun getStandingsFlowFromLocalById(compId: String): Flow<CompetitionStandings?>

    suspend fun getCompetitionTopScorersBySeason(
        compId: String,
        season: String?
    ): Resource<CompetitionScorers?>

    suspend fun getScorersFlowFromLocal(compId: String): Flow<CompetitionScorers?>

    suspend fun getAllMatchesFromRemoteToLocal(
        compId: String,
        season: String?
    ): Resource<CompetitionMatches>

    suspend fun getAllMatchesFlowFromLocal(
        compId: String,
    ): Flow<CompetitionMatches?>

    suspend fun getHead2headById(
        matchId:Int
    ) : Resource<Head2head>
}