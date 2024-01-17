package ru.asmelnikov.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.domain.models.Competition
import ru.asmelnikov.utils.Resource

interface FootballRepository {

    suspend fun getAllCompetitionsFromRemoteToLocal(): Resource<List<Competition>>

    suspend fun getAllCompetitionsFlowFromLocal(): Flow<List<Competition>>
}