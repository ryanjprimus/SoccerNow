package ru.asmelnikov.data.local.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

open class CompetitionScorersEntity(
    @PrimaryKey
    var id: String = "",
    var season: SeasonEntity? = null,
    var scorers: RealmList<ScorerEntity>? = null
) : RealmObject()

@RealmClass(embedded = true)
open class ScorerEntity(
    var assists: Int? = -1,
    var goals: Int? = -1,
    var penalties: Int? = -1,
    var playedMatches: Int? = -1,
    var player: PlayerEntity? = null,
    var team: TeamEntity? = null
) : RealmObject()

@RealmClass(embedded = true)
open class PlayerEntity(
    var dateOfBirth: String? = "",
    var firstName: String? = "",
    var id: Int? = -1,
    var lastName: String? = "",
    var lastUpdated: String? = "",
    var name: String? = "",
    var nationality: String? = "",
    var position: String? = "",
    var section: String? = "",
    var shirtNumber: Int? = -1
) : RealmObject()