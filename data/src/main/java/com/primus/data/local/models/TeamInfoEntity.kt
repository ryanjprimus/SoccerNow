package com.primus.data.local.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

open class TeamInfoEntity(
    var address: String = "",
    var area: AreaEntity? = null,
    var clubColors: String = "",
    var coach: CoachEntity? = null,
    var crest: String = "",
    var founded: Int = -1,
    @PrimaryKey
    var id: String = "",
    var lastUpdated: String = "",
    var name: String = "",
    var shortName: String = "",
    var squadByPosition: RealmList<SquadByPositionEntity>? = null,
    var tla: String = "",
    var venue: String = "",
    var website: String = ""
) : RealmObject()

@RealmClass(embedded = true)
open class CoachEntity(
    var contract: ContractEntity? = null,
    var dateOfBirth: String = "",
    var firstName: String = "",
    var id: Int = -1,
    var lastName: String = "",
    var name: String = "",
    var nationality: String = ""
) : RealmObject()

@RealmClass(embedded = true)
open class SquadByPositionEntity(
    var position: String = "",
    var squad: RealmList<SquadEntity>? = null
) : RealmObject()

@RealmClass(embedded = true)
open class SquadEntity(
    var dateOfBirth: String = "",
    var id: Int = -1,
    var name: String = "",
    var nationality: String = ""
) : RealmObject()

@RealmClass(embedded = true)
open class ContractEntity(
    var start: String = "",
    var until: String = ""
) : RealmObject()