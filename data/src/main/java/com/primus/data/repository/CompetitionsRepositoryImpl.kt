package com.primus.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import com.primus.data.api.FootballApi
import com.primus.data.local.CompetitionsRealmOptions
import com.primus.data.mappers.toCompetition
import com.primus.data.mappers.toCompetitionEntity
import com.primus.data.models.CompetitionModelDTO
import com.primus.data.retrofit_errors_handler.RetrofitErrorsHandler
import com.primus.domain.models.Competition
import com.primus.domain.repository.CompetitionsRepository
import com.primus.utils.Resource

class CompetitionsRepositoryImpl(
    private val footballApi: FootballApi,
    private val realmOptions: CompetitionsRealmOptions,
    private val retrofitErrorsHandler: RetrofitErrorsHandler
) : CompetitionsRepository {

    override suspend fun getAllCompetitionsFromRemoteToLocal(): Resource<List<Competition>> {
        return retrofitErrorsHandler.executeSafely {
            val response: Response<CompetitionModelDTO> = footballApi.getAllFootballCompetitions()
            if (response.isSuccessful && response.code() == 200) {
                val competitions = response.body()?.competitions?.map {
                    it.toCompetitionEntity()
                } ?: emptyList()
                realmOptions.upsertCompetitionsDataFromRemoteToLocal(competitions)
                Resource.Success(competitions.map { it.toCompetition() })
            } else {
                retrofitErrorsHandler.responseFailureHandler(response)
            }
        }
    }

    override suspend fun getAllCompetitionsFlowFromLocal(): Flow<List<Competition>> {
        return realmOptions.getCompetitionsFlowFromLocal()
            .map { comps -> comps.map { comp -> comp.toCompetition() } }
    }
}