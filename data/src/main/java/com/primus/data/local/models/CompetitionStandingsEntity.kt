package com.primus.data.local.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

open class CompetitionStandingsEntity(
    @PrimaryKey
    var id: String = "",
    var area: AreaEntity? = null,
    var competition: CompetitionEmbeddedEntity? = null,
    var filters: FiltersEntity? = null,
    var season: SeasonEntity? = null,
    var standings: RealmList<StandingEntity>? = null
) : RealmObject()

@RealmClass(embedded = true)
open class CompetitionEmbeddedEntity(
    var id: Int = -1,
    var area: AreaEntity? = null,
    var code: String = "",
    var currentSeason: CurrentSeasonEntity? = null,
    var emblem: String = "",
    var lastUpdated: String = "",
    var name: String = "",
    var numberOfAvailableSeasons: Int = -1,
    var plan: String = "",
    var type: String = "",
    var seasons: RealmList<SeasonEntity>? = null
) : RealmObject()

@RealmClass(embedded = true)
open class FiltersEntity(
    var season: String = ""
) : RealmObject()

@RealmClass(embedded = true)
open class SeasonEntity(
    var currentMatchday: Int = -1,
    var endDate: String = "",
    var id: Int = -1,
    var startDate: String = "",
    var winner: WinnerEntity? = null
) : RealmObject()

@RealmClass(embedded = true)
open class StandingEntity(
    var group: String = "",
    var stage: String = "",
    var table: RealmList<TableEntity>? = null,
    var type: String = ""
) : RealmObject()

@RealmClass(embedded = true)
open class TableEntity(
    var draw: Int = -1,
    var form: String = "",
    var goalDifference: Int = -1,
    var goalsAgainst: Int = -1,
    var goalsFor: Int = -1,
    var lost: Int = -1,
    var playedGames: Int = -1,
    var points: Int = -1,
    var position: Int = -1,
    var team: TeamEntity? = null,
    var won: Int = -1
) : RealmObject()

@RealmClass(embedded = true)
open class TeamEntity(
    var crest: String = "",
    var id: Int = -1,
    var name: String = "",
    var shortName: String = "",
    var tla: String = ""
) : RealmObject()