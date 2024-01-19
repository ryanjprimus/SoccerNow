package ru.asmelnikov.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.domain.models.CompetitionStandings
import ru.asmelnikov.utils.Resource

interface CompetitionStandingsRepository {

    suspend fun getCompetitionStandingsFromRemoteToLocalById(compId: String): Resource<CompetitionStandings>

    suspend fun getStandingsFlowFromLocalById(compId: String): Flow<CompetitionStandings>
}