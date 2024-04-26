package com.primus.domain.repository

import kotlinx.coroutines.flow.Flow
import com.primus.domain.models.Competition
import com.primus.utils.Resource

interface CompetitionsRepository {

    suspend fun getAllCompetitionsFromRemoteToLocal(): Resource<List<Competition>>

    suspend fun getAllCompetitionsFlowFromLocal(): Flow<List<Competition>>
}