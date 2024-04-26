package com.primus.domain.repository

import kotlinx.coroutines.flow.Flow
import com.primus.domain.models.TeamInfo
import com.primus.domain.models.TeamMatches
import com.primus.utils.Resource

interface TeamInfoRepository {

    suspend fun getTeamInfoById(
        teamId: String
    ): Resource<TeamInfo>

    suspend fun getTeamInfoByIdFlowFromLocal(
        teamId: String
    ): Flow<TeamInfo?>

    suspend fun getTeamMatchesFromRemoteToLocal(
        teamId: String,
        season: String? = null
    ): Resource<TeamMatches>

    suspend fun getTeamMatchesFlowFromLocal(
        teamId: String,
    ): Flow<TeamMatches?>
}