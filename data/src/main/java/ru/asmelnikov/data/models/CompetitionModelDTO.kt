package ru.asmelnikov.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompetitionModelDTO(
    @Json(name = "competitions") val competitions: List<CompetitionDTO>?,
    @Json(name = "count") val count: Int?,
    @Json(name = "filters") val filters: FiltersDTO?
)

@JsonClass(generateAdapter = true)
data class CompetitionDTO(
    @Json(name = "area") val area: AreaDTO?,
    @Json(name = "code") val code: String?,
    @Json(name = "currentSeason") val currentSeason: CurrentSeasonDTO?,
    @Json(name = "emblem") val emblem: String?,
    @Json(name = "id") val id: Int?,
    @Json(name = "lastUpdated") val lastUpdated: String?,
    @Json(name = "name") val name: String?,
    @Json(name = "numberOfAvailableSeasons") val numberOfAvailableSeasons: Int?,
    @Json(name = "plan") val plan: String?,
    @Json(name = "type") val type: String?
)

@JsonClass(generateAdapter = true)
data class FiltersDTO(
    @Json(name = "client") val client: String?,
    @Json(name = "season") val season: String?
)

@JsonClass(generateAdapter = true)
data class AreaDTO(
    @Json(name = "code") val code: String?,
    @Json(name = "flag") val flag: String?,
    @Json(name = "id") val id: Int?,
    @Json(name = "name") val name: String?
)

@JsonClass(generateAdapter = true)
data class CurrentSeasonDTO(
    @Json(name = "currentMatchday") val currentMatchDay: Int?,
    @Json(name = "endDate") val endDate: String?,
    @Json(name = "id") val id: Int?,
    @Json(name = "startDate") val startDate: String?,
    @Json(name = "winner") val winner: WinnerDTO?
)

@JsonClass(generateAdapter = true)
data class WinnerDTO(
    @Json(name = "address") val address: String?,
    @Json(name = "clubColors") val clubColors: String?,
    @Json(name = "crest") val crest: String?,
    @Json(name = "founded") val founded: Int?,
    @Json(name = "id") val id: Int?,
    @Json(name = "lastUpdated") val lastUpdated: String?,
    @Json(name = "name") val name: String?,
    @Json(name = "shortName") val shortName: String?,
    @Json(name = "tla") val tla: String?,
    @Json(name = "website") val website: String?,
    @Json(name = "venue") val venue: String?
)
