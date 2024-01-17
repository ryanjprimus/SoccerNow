package ru.asmelnikov.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import ru.asmelnikov.data.models.CompetitionModelDTO

interface FootballApi {

    @GET("competitions/")
    @Headers("X-Auth-Token: 8c02a856ac284753b67eacd7ab31d010")
    suspend fun getAllFootballCompetitions(): Response<CompetitionModelDTO>
}