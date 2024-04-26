package com.primus.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PersonDTO(
    @Json(name = "currentTeam") val currentTeam: CurrentTeamDTO?,
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

@JsonClass(generateAdapter = true)
data class CurrentTeamDTO(
    @Json(name = "address") val address: String?,
    @Json(name = "area") val area: AreaDTO?,
    @Json(name = "clubColors") val clubColors: String?,
    @Json(name = "contract") val contract: ContractDTO,
    @Json(name = "crest") val crest: String?,
    @Json(name = "founded") val founded: Int?,
    @Json(name = "id") val id: Int?,
    @Json(name = "name") val name: String?,
    @Json(name = "runningCompetitions") val runningCompetitions: List<RunningCompetitionDTO>?,
    @Json(name = "shortName") val shortName: String?,
    @Json(name = "tla") val tla: String?,
    @Json(name = "venue") val venue: String?,
    @Json(name = "website") val website: String?
)

@JsonClass(generateAdapter = true)
data class RunningCompetitionDTO(
    @Json(name = "code") val code: String?,
    @Json(name = "emblem") val emblem: String?,
    @Json(name = "id") val id: Int?,
    @Json(name = "name") val name: String?,
    @Json(name = "type") val type: String?
)
