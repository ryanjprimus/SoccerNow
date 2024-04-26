package com.primus.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import com.primus.data.models.CompetitionDTO
import com.primus.data.models.CompetitionMatchesDTO
import com.primus.data.models.CompetitionModelDTO
import com.primus.data.models.CompetitionScorersModelDTO
import com.primus.data.models.CompetitionStandingsModelDTO
import com.primus.data.models.Head2headDTO
import com.primus.data.models.PersonDTO
import com.primus.data.models.TeamInfoDTO
import com.primus.utils.Constants.FOOTBALL_API_KEY

interface FootballApi {

    @GET("competitions/")
    @Headers("X-Auth-Token: $FOOTBALL_API_KEY")
    suspend fun getAllFootballCompetitions(): Response<CompetitionModelDTO>

    @GET("competitions/{competitionId}/standings")
    @Headers("X-Auth-Token: $FOOTBALL_API_KEY")
    suspend fun getCompetitionStandingByIdAndSeason(
        @Path("competitionId") competitionId: String,
        @Query("season") season: String?
    ): Response<CompetitionStandingsModelDTO>

    @GET("competitions/{competitionId}")
    @Headers("X-Auth-Token: $FOOTBALL_API_KEY")
    suspend fun getCompetitionSeasonsById(@Path("competitionId") competitionId: String): Response<CompetitionDTO>

    @GET("competitions/{competitionId}/scorers")
    @Headers("X-Auth-Token: $FOOTBALL_API_KEY")
    suspend fun getCompetitionTopScorersBySeason(
        @Path("competitionId") competitionId: String,
        @Query("season") season: String?,
        @Query("limit") limit: Int = 20
    ): Response<CompetitionScorersModelDTO>

    @GET("competitions/{competitionId}/matches")
    @Headers("X-Auth-Token: $FOOTBALL_API_KEY")
    suspend fun getCompetitionMatchesBySeason(
        @Path("competitionId") competitionId: String,
        @Query("season") season: String?
    ): Response<CompetitionMatchesDTO>

    @GET("teams/{teamId}/matches/")
    @Headers("X-Auth-Token: $FOOTBALL_API_KEY")
    suspend fun getTeamMatchesBySeason(
        @Path("teamId") teamId: String,
        @Query("season") season: String?
    ): Response<CompetitionMatchesDTO>

    @GET("matches/{matchId}/head2head")
    @Headers("X-Auth-Token: $FOOTBALL_API_KEY")
    suspend fun getHead2headById(
        @Path("matchId") matchId: String
    ): Response<Head2headDTO>

    @GET("teams/{teamId}")
    @Headers("X-Auth-Token: $FOOTBALL_API_KEY")
    suspend fun getTeamInfoById(
        @Path("teamId") teamId: String
    ): Response<TeamInfoDTO>

    @GET("persons/{personId}")
    @Headers("X-Auth-Token: $FOOTBALL_API_KEY")
    suspend fun getPersonInfo(
        @Path("personId") personId: String
    ): Response<PersonDTO>
}