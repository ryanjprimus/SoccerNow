package ru.asmelnikov.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.domain.models.CompetitionScorers
import ru.asmelnikov.domain.models.CompetitionStandings
import ru.asmelnikov.domain.models.Scorer
import ru.asmelnikov.domain.models.Season
import ru.asmelnikov.utils.Resource

interface CompetitionStandingsRepository {

    suspend fun getCompetitionStandingsFromRemoteToLocalById(
        compId: String,
        season: String?
    ): Resource<CompetitionStandings>

    suspend fun getCompetitionSeasonsById(compId: String): Resource<List<Season>>

    suspend fun getStandingsFlowFromLocalById(compId: String): Flow<CompetitionStandings>

    suspend fun getCompetitionTopScorersBySeason(
        compId: String,
        season: String?
    ): Resource<CompetitionScorers?>

    suspend fun getScorersFlowFromLocal(compId: String): Flow<CompetitionScorers>
}