package ru.asmelnikov.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.domain.models.TeamInfo
import ru.asmelnikov.utils.Resource

interface TeamInfoRepository {

    suspend fun getTeamInfoById(
        teamId: String
    ): Resource<TeamInfo>

    suspend fun getTeamInfoByIdFlowFromLocal(
        teamId: String
    ): Flow<TeamInfo>
}