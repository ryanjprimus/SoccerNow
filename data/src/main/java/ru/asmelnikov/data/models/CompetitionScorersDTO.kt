package ru.asmelnikov.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompetitionScorersModelDTO(
    @Json(name = "competition") val competition: CompetitionDTO?,
    @Json(name = "season") val season: SeasonDTO?,
    @Json(name = "scorers") val scorers: List<ScorerDTO>?
)

@JsonClass(generateAdapter = true)
data class ScorerDTO(
    @Json(name = "assists") val assists: Int?,
    @Json(name = "goals") val goals: Int?,
    @Json(name = "penalties") val penalties: Int?,
    @Json(name = "playedMatches") val playedMatches: Int?,
    @Json(name = "player") val player: PlayerDTO?,
    @Json(name = "team") val team: TeamDTO?
)

@JsonClass(generateAdapter = true)
data class PlayerDTO(
    @Json(name = "dateOfBirth") val dateOfBirth: String?,
    @Json(name = "firstName") val firstName: String?,
    @Json(name = "id") val id: Int?,
    @Json(name = "lastName") val lastName: String?,
    @Json(name = "lastUpdated") val lastUpdated: String?,
    @Json(name = "name") val name: String?,
    @Json(name = "nationality") val nationality: String?,
    @Json(name = "position") val position: String?,
    @Json(name = "section") val section: String?,
    @Json(name = "shirtNumber") val shirtNumber: Int?
)