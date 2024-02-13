package ru.asmelnikov.data.local.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class TeamMatchesEntity(
    @PrimaryKey
    var id: String = "",
    var season: String = "",
    var matchesCompleted: RealmList<MatchEntity>? = null,
    var matchesAhead: RealmList<MatchEntity>? = null,
) : RealmObject()
