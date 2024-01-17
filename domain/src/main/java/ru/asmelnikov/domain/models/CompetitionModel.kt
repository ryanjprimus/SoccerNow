package ru.asmelnikov.domain.models

data class CompetitionModel(
    val competitions: List<Competition>,
    val count: Int,
    val filters: Filters
)

data class Competition(
    val area: Area,
    val code: String,
    val currentSeason: CurrentSeason,
    val emblem: String,
    val id: Int,
    val lastUpdated: String,
    val name: String,
    val numberOfAvailableSeasons: Int,
    val plan: String,
    val type: String
)

data class Filters(
    val client: String
)

data class Area(
    val code: String,
    val flag: String,
    val id: Int,
    val name: String
)

data class CurrentSeason(
    val currentMatchDay: Int,
    val endDate: String,
    val id: Int,
    val startDate: String,
    val winner: Winner
)

data class Winner(
    val address: String,
    val clubColors: String,
    val crest: String,
    val founded: Int,
    val id: Int,
    val lastUpdated: String,
    val name: String,
    val shortName: String,
    val tla: String,
    val website: String
)