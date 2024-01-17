package ru.asmelnikov.domain

import ru.asmelnikov.domain.models.Competition
import ru.asmelnikov.domain.models.CompetitionModel
import ru.asmelnikov.utils.Resource

interface FootballRepository {

    suspend fun getAllCompetitions(): Resource<List<Competition>>
}