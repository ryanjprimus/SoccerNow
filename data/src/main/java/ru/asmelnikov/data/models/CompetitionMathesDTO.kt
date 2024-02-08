package ru.asmelnikov.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompetitionMatchesDTO(
    @Json(name = "competition") val competition: CompetitionDTO?,
    @Json(name = "filters") val filters: FiltersDTO?,
    @Json(name = "matches") val matches: List<MatchDTO>?,
)

@JsonClass(generateAdapter = true)
data class MatchDTO(
    @Json(name = "area") val area: AreaDTO?,
    @Json(name = "awayTeam") val awayTeam: AwayTeamDTO?,
    @Json(name = "competition") val competition: CompetitionDTO?,
    @Json(name = "group") val group: String?,
    @Json(name = "homeTeam") val homeTeam: HomeTeamDTO?,
    @Json(name = "id") val id: Int?,
    @Json(name = "lastUpdated") val lastUpdated: String?,
    @Json(name = "matchday") val matchday: Int?,
    @Json(name = "referees") val referees: List<RefereeDTO>?,
    @Json(name = "score") val score: ScoreDTO?,
    @Json(name = "season") val season: SeasonDTO?,
    @Json(name = "stage") val stage: String?,
    @Json(name = "status") val status: String?,
    @Json(name = "utcDate") val utcDate: String?
)

@JsonClass(generateAdapter = true)
data class AwayTeamDTO(
    @Json(name = "crest") val crest: String?,
    @Json(name = "id") val id: Int?,
    @Json(name = "name") val name: String?,
    @Json(name = "shortName") val shortName: String?,
    @Json(name = "tla") val tla: String?
)

@JsonClass(generateAdapter = true)
data class HomeTeamDTO(
    @Json(name = "crest") val crest: String?,
    @Json(name = "id") val id: Int?,
    @Json(name = "name") val name: String?,
    @Json(name = "shortName") val shortName: String?,
    @Json(name = "tla") val tla: String?
)

@JsonClass(generateAdapter = true)
data class RefereeDTO(
    @Json(name = "id") val id: Int?,
    @Json(name = "name") val name: String?,
    @Json(name = "nationality") val nationality: String?,
    @Json(name = "type") val type: String?
)

@JsonClass(generateAdapter = true)
data class ScoreDTO(
    @Json(name = "duration") val duration: String?,
    @Json(name = "fullTime") val fullTime: FullTimeDTO?,
    @Json(name = "halfTime") val halfTime: HalfTimeDTO?,
    @Json(name = "winner") val winner: String?
)

@JsonClass(generateAdapter = true)
data class FullTimeDTO(
    @Json(name = "away") val away: Int?,
    @Json(name = "home") val home: Int?
)

@JsonClass(generateAdapter = true)
data class HalfTimeDTO(
    @Json(name = "away") val away: Int?,
    @Json(name = "home") val home: Int?
)

