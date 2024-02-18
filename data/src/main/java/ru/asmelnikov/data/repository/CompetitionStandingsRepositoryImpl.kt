package ru.asmelnikov.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.asmelnikov.data.api.FootballApi
import ru.asmelnikov.data.local.StandingsRealmOptions
import ru.asmelnikov.data.local.models.CompetitionMatchesEntity
import ru.asmelnikov.data.local.models.CompetitionScorersEntity
import ru.asmelnikov.data.local.models.CompetitionStandingsEntity
import ru.asmelnikov.data.mappers.toCompetition
import ru.asmelnikov.data.mappers.toCompetitionMatches
import ru.asmelnikov.data.mappers.toCompetitionMatchesEntity
import ru.asmelnikov.data.mappers.toCompetitionScorers
import ru.asmelnikov.data.mappers.toCompetitionScorersEntity
import ru.asmelnikov.data.mappers.toCompetitionStandings
import ru.asmelnikov.data.mappers.toCompetitionStandingsEntity
import ru.asmelnikov.data.mappers.toHead2head
import ru.asmelnikov.data.retrofit_errors_handler.RetrofitErrorsHandler
import ru.asmelnikov.domain.models.CompetitionMatches
import ru.asmelnikov.domain.models.CompetitionScorers
import ru.asmelnikov.domain.models.CompetitionStandings
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.Season
import ru.asmelnikov.domain.repository.CompetitionStandingsRepository
import ru.asmelnikov.utils.Resource

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