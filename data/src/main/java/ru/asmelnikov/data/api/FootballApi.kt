package ru.asmelnikov.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import ru.asmelnikov.data.models.CompetitionModelDTO
import ru.asmelnikov.data.models.CompetitionStandingsModelDTO
import ru.asmelnikov.utils.Constants.API_KEY

interface FootballApi {

    @GET("competitions/")
    @Headers("X-Auth-Token: $API_KEY")
    suspend fun getAllFootballCompetitions(): Response<CompetitionModelDTO>

    @GET("competitions/{competitionId}/standings")
    @Headers("X-Auth-Token: $API_KEY")
    suspend fun getCompetitionStandingById(@Path("competitionId") competitionId: String): Response<CompetitionStandingsModelDTO>
}