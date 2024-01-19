package ru.asmelnikov.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.asmelnikov.data.api.FootballApi
import ru.asmelnikov.data.local.StandingsRealmOptions
import ru.asmelnikov.data.local.models.CompetitionStandingsEntity
import ru.asmelnikov.data.mappers.toCompetitionStandings
import ru.asmelnikov.data.mappers.toCompetitionStandingsEntity
import ru.asmelnikov.data.retrofit_errors_handler.RetrofitErrorsHandler
import ru.asmelnikov.domain.models.CompetitionStandings
import ru.asmelnikov.domain.repository.CompetitionStandingsRepository
import ru.asmelnikov.utils.Resource

class CompetitionStandingsRepositoryImpl(
    private val footballApi: FootballApi,
    private val realmOptions: StandingsRealmOptions,
    private val retrofitErrorsHandler: RetrofitErrorsHandler
) : CompetitionStandingsRepository {

    override suspend fun getCompetitionStandingsFromRemoteToLocalById(compId: String): Resource<CompetitionStandings> {
        return retrofitErrorsHandler.executeSafely {
            val response = footballApi.getCompetitionStandingById(compId)
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

    override suspend fun getStandingsFlowFromLocalById(compId: String): Flow<CompetitionStandings> {
        return realmOptions.getStandingsFlowById(compId).map {
            it.toCompetitionStandings()
        }
    }

}