package com.primus.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.primus.data.api.FootballApi
import com.primus.data.local.StandingsRealmOptions
import com.primus.data.local.models.CompetitionMatchesEntity
import com.primus.data.local.models.CompetitionScorersEntity
import com.primus.data.local.models.CompetitionStandingsEntity
import com.primus.data.mappers.toCompetition
import com.primus.data.mappers.toCompetitionMatches
import com.primus.data.mappers.toCompetitionMatchesEntity
import com.primus.data.mappers.toCompetitionScorers
import com.primus.data.mappers.toCompetitionScorersEntity
import com.primus.data.mappers.toCompetitionStandings
import com.primus.data.mappers.toCompetitionStandingsEntity
import com.primus.data.mappers.toHead2head
import com.primus.data.retrofit_errors_handler.RetrofitErrorsHandler
import com.primus.domain.models.CompetitionMatches
import com.primus.domain.models.CompetitionScorers
import com.primus.domain.models.CompetitionStandings
import com.primus.domain.models.Head2head
import com.primus.domain.models.Season
import com.primus.domain.repository.CompetitionStandingsRepository
import com.primus.utils.Resource

class CompetitionStandingsRepositoryImpl(
    private val footballApi: FootballApi,
    private val realmOptions: StandingsRealmOptions,
    private val retrofitErrorsHandler: RetrofitErrorsHandler
) : CompetitionStandingsRepository {

    override suspend fun getCompetitionStandingsFromRemoteToLocalById(
        compId: String,
        season: String?
    ): Resource<CompetitionStandings> {
        return retrofitErrorsHandler.executeSafely {
            val response =
                footballApi.getCompetitionStandingByIdAndSeason(compId, season?.substring(0, 4))
            if (response.isSuccessful && response.code() == 200) {
                val standings = response.body()?.toCompetitionStandingsEntity()
                realmOptions.upsertStandingsFromRemoteToLocal(
                    standings ?: CompetitionStandingsEntity()
                )
                Resource.Success(standings.toCompetitionStandings())
            } else {
                retrofitErrorsHandler.responseFailureHandler(response)
            }
        }
    }

    override suspend fun getCompetitionSeasonsById(compId: String): Resource<List<Season>> {
        return retrofitErrorsHandler.executeSafely {
            val response = footballApi.getCompetitionSeasonsById(compId)
            if (response.isSuccessful && response.code() == 200) {
                val comp = response.body()?.toCompetition()
                Resource.Success(comp?.seasons ?: emptyList())
            } else {
                retrofitErrorsHandler.responseFailureHandler(response)
            }
        }
    }

    override suspend fun getStandingsFlowFromLocalById(compId: String): Flow<CompetitionStandings?> {
        return realmOptions.getStandingsFlowById(compId).map {
            it.map { it.toCompetitionStandings() }.firstOrNull()
        }
    }

    override suspend fun getCompetitionTopScorersBySeason(
        compId: String,
        season: String?
    ): Resource<CompetitionScorers?> {
        return retrofitErrorsHandler.executeSafely {
            val response =
                footballApi.getCompetitionTopScorersBySeason(compId, season?.substring(0, 4))
            if (response.isSuccessful && response.code() == 200) {
                val comp = response.body()?.toCompetitionScorersEntity()
                realmOptions.upsertScorersFromRemoteToLocal(
                    comp ?: CompetitionScorersEntity()
                )
                Resource.Success(comp?.toCompetitionScorers())
            } else {
                retrofitErrorsHandler.responseFailureHandler(response)
            }
        }
    }

    override suspend fun getScorersFlowFromLocal(compId: String): Flow<CompetitionScorers?> {
        return realmOptions.getScorersFlowById(compId).map {
            it.map { it.toCompetitionScorers() }.firstOrNull()
        }
    }

    override suspend fun getAllMatchesFromRemoteToLocal(
        compId: String,
        season: String?
    ): Resource<CompetitionMatches> {
        return retrofitErrorsHandler.executeSafely {
            val response =
                footballApi.getCompetitionMatchesBySeason(compId, season?.substring(0, 4))
            if (response.isSuccessful && response.code() == 200) {
                val matches = response.body()?.toCompetitionMatchesEntity()
                realmOptions.upsertMatchesFromRemoteToLocal(
                    matches ?: CompetitionMatchesEntity()
                )
                Resource.Success(matches?.toCompetitionMatches() ?: CompetitionMatches())
            } else {
                retrofitErrorsHandler.responseFailureHandler(response)
            }
        }
    }

    override suspend fun getAllMatchesFlowFromLocal(compId: String): Flow<CompetitionMatches?> {
        return realmOptions.getMatchesFlowById(compId)
            .map { it.map { it.toCompetitionMatches() }.firstOrNull() }
    }

    override suspend fun getHead2headById(matchId: Int): Resource<Head2head> {
        return retrofitErrorsHandler.executeSafely {
            val response =
                footballApi.getHead2headById(matchId.toString())
            if (response.isSuccessful && response.code() == 200) {
                val head2head = response.body()?.toHead2head(matchId)
                Resource.Success(head2head ?: Head2head())
            } else {
                retrofitErrorsHandler.responseFailureHandler(response)
            }
        }
    }
}