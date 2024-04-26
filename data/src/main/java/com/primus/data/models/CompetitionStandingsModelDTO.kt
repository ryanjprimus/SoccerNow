package com.primus.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompetitionStandingsModelDTO(
    @Json(name = "area") val area: AreaDTO?,
    @Json(name = "competition") val competition: CompetitionDTO?,
    @Json(name = "filters") val filters: FiltersDTO?,
    @Json(name = "season") val season: SeasonDTO?,
    @Json(name = "standings") val standings: List<StandingDTO>?
)

@JsonClass(generateAdapter = true)
data class SeasonDTO(
    @Json(name = "currentMatchday") val currentMatchday: Int?,
    @Json(name = "endDate") val endDate: String?,
    @Json(name = "id") val id: Int?,
    @Json(name = "startDate") val startDate: String?,
    @Json(name = "winner") val winner: WinnerDTO?
)

@JsonClass(generateAdapter = true)
data class StandingDTO(
    @Json(name = "group") val group: String?,
    @Json(name = "stage") val stage: String?,
    @Json(name = "table") val table: List<TableDTO>?,
    @Json(name = "type") val type: String?
)

@JsonClass(generateAdapter = true)
data class TableDTO(
    @Json(name = "draw") val draw: Int?,
    @Json(name = "form") val form: String?,
    @Json(name = "goalDifference") val goalDifference: Int?,
    @Json(name = "goalsAgainst") val goalsAgainst: Int?,
    @Json(name = "goalsFor") val goalsFor: Int?,
    @Json(name = "lost") val lost: Int?,
    @Json(name = "playedGames") val playedGames: Int?,
    @Json(name = "points") val points: Int?,
    @Json(name = "position") val position: Int?,
    @Json(name = "team") val team: TeamDTO?,
    @Json(name = "won") val won: Int?
)

@JsonClass(generateAdapter = true)
data class TeamDTO(
    @Json(name = "crest") val crest: String?,
    @Json(name = "id") val id: Int?,
    @Json(name = "name") val name: String?,
    @Json(name = "shortName") val shortName: String?,
    @Json(name = "tla") val tla: String?
)
