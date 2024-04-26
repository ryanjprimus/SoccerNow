package com.primus.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.primus.data.api.FootballApi
import com.primus.data.local.TeamInfoRealmOptions
import com.primus.data.local.models.TeamInfoEntity
import com.primus.data.local.models.TeamMatchesEntity
import com.primus.data.mappers.toTeamInfo
import com.primus.data.mappers.toTeamInfoEntity
import com.primus.data.mappers.toTeamMatches
import com.primus.data.mappers.toTeamMatchesEntity
import com.primus.data.retrofit_errors_handler.RetrofitErrorsHandler
import com.primus.domain.models.TeamInfo
import com.primus.domain.models.TeamMatches
import com.primus.domain.repository.TeamInfoRepository
import com.primus.utils.Resource

class TeamInfoRepositoryImpl(
    private val footballApi: FootballApi,
    private val realmOptions: TeamInfoRealmOptions,
    private val retrofitErrorsHandler: RetrofitErrorsHandler
) : TeamInfoRepository {
    override suspend fun getTeamInfoById(teamId: String): Resource<TeamInfo> {
        return retrofitErrorsHandler.executeSafely {
            val response =
                footballApi.getTeamInfoById(teamId)
            if (response.isSuccessful && response.code() == 200) {
                val team = response.body()?.toTeamInfoEntity()
                realmOptions.upsertTeamInfoFromRemoteToLocal(
                    team ?: TeamInfoEntity()
                )
                Resource.Success(team?.toTeamInfo() ?: TeamInfo())
            } else {
                retrofitErrorsHandler.responseFailureHandler(response)
            }
        }
    }

    override suspend fun getTeamInfoByIdFlowFromLocal(teamId: String): Flow<TeamInfo?> {
        return realmOptions.getTeamInfoFlowById(
            teamId = teamId
        ).map {
            it.map { it.toTeamInfo() }.firstOrNull()
        }
    }

    override suspend fun getTeamMatchesFromRemoteToLocal(
        teamId: String,
        season: String?
    ): Resource<TeamMatches> {
        return retrofitErrorsHandler.executeSafely {
            val response =
                footballApi.getTeamMatchesBySeason(teamId, season?.substring(0, 4))
            if (response.isSuccessful && response.code() == 200) {
                val matches = response.body()?.toTeamMatchesEntity(teamId)
                realmOptions.upsertMatchesFromRemoteToLocal(
                    matches ?: TeamMatchesEntity()
                )
                Resource.Success(matches?.toTeamMatches() ?: TeamMatches())
            } else {
                retrofitErrorsHandler.responseFailureHandler(response)
            }
        }
    }

    override suspend fun getTeamMatchesFlowFromLocal(teamId: String): Flow<TeamMatches?> {
        return realmOptions.getMatchesFlowById(teamId)
            .map { it.map { it.toTeamMatches() }.firstOrNull() }
    }
}