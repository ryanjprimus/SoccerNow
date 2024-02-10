package ru.asmelnikov.data.local.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

open class CompetitionMatchesEntity(
    @PrimaryKey
    var id: String = "",
    var season: String = "",
    var seasonType: String = "",
    var matchesByTourCompleted: RealmList<MatchesByTourEntity>? = null,
    var matchesByTourAhead: RealmList<MatchesByTourEntity>? = null,
) : RealmObject()

@RealmClass(embedded = true)
open class MatchesByTourEntity(
    var matchday: Int? = -1,
    var stage: String? = "",
    var matches: RealmList<MatchEntity>? = null
) : RealmObject()

@RealmClass(embedded = true)
open class MatchEntity(
    var awayTeam: AwayTeamEntity? = null,
    var group: String? = "",
    var homeTeam: HomeTeamEntity? = null,
    var id: Int? = -1,
    var lastUpdated: String? = "",
    var matchday: Int? = -1,
    var referees: RealmList<RefereeEntity>? = null,
    var score: ScoreEntity? = null,
    var stage: String? = "",
    var status: String? = "",
    var utcDate: String? = ""
) : RealmObject()

@RealmClass(embedded = true)
open class AwayTeamEntity(
    var crest: String? = "",
    var id: Int? = -1,
    var name: String? = "",
    var shortName: String? = "",
    var tla: String? = ""
) : RealmObject()

@RealmClass(embedded = true)
open class HomeTeamEntity(
    var crest: String? = "",
    var id: Int? = -1,
    var name: String? = "",
    var shortName: String? = "",
    var tla: String? = ""
) : RealmObject()

@RealmClass(embedded = true)
open class RefereeEntity(
    var id: Int? = -1,
    var name: String? = "",
    var nationality: String? = "",
    var type: String? = ""
) : RealmObject()

@RealmClass(embedded = true)
open class ScoreEntity(
    var duration: String? = "",
    var fullTime: FullTimeEntity? = null,
    var halfTime: HalfTimeEntity? = null,
    var winner: String? = ""
) : RealmObject()

@RealmClass(embedded = true)
open class FullTimeEntity(
    var away: Int? = -1,
    var home: Int? = -1
) : RealmObject()

@RealmClass(embedded = true)
open class HalfTimeEntity(
    var away: Int? = -1,
    var home: Int? = -1
) : RealmObject()
