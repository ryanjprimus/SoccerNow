package ru.asmelnikov.data.local.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

open class CompetitionEntity(
    @PrimaryKey
    var id: Int = -1,
    var area: AreaEntity? = null,
    var code: String = "",
    var currentSeason: CurrentSeasonEntity? = null,
    var emblem: String = "",
    var lastUpdated: String = "",
    var name: String = "",
    var numberOfAvailableSeasons: Int = -1,
    var plan: String = "",
    var type: String = ""
) : RealmObject()

@RealmClass(embedded = true)
open class AreaEntity(
    var code: String = "",
    var flag: String = "",
    var id: Int = -1,
    var name: String = ""
) : RealmObject()

@RealmClass(embedded = true)
open class CurrentSeasonEntity(
    var currentMatchDay: Int = -1,
    var endDate: String = "",
    var id: Int = -1,
    var startDate: String = "",
    var winner: WinnerEntity? = null
) : RealmObject()

@RealmClass(embedded = true)
open class WinnerEntity(
    var address: String = "",
    var clubColors: String = "",
    var crest: String = "",
    var founded: Int = -1,
    var id: Int = -1,
    var lastUpdated: String = "",
    var name: String = "",
    var shortName: String = "",
    var tla: String = "",
    var website: String = "",
    var venue: String = ""
) : RealmObject()