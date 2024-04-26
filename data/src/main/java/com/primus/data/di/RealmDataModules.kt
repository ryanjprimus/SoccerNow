package com.primus.data.di

import io.realm.annotations.RealmModule
import com.primus.data.local.models.AreaEntity
import com.primus.data.local.models.AwayTeamEntity
import com.primus.data.local.models.CoachEntity
import com.primus.data.local.models.CompetitionEmbeddedEntity
import com.primus.data.local.models.CompetitionEntity
import com.primus.data.local.models.CompetitionMatchesEntity
import com.primus.data.local.models.CompetitionScorersEntity
import com.primus.data.local.models.CompetitionStandingsEntity
import com.primus.data.local.models.ContractEntity
import com.primus.data.local.models.CurrentSeasonEntity
import com.primus.data.local.models.FiltersEntity
import com.primus.data.local.models.FullTimeEntity
import com.primus.data.local.models.HalfTimeEntity
import com.primus.data.local.models.HomeTeamEntity
import com.primus.data.local.models.MatchEntity
import com.primus.data.local.models.MatchesByTourEntity
import com.primus.data.local.models.PlayerEntity
import com.primus.data.local.models.RefereeEntity
import com.primus.data.local.models.ScoreEntity
import com.primus.data.local.models.ScorerEntity
import com.primus.data.local.models.SeasonEntity
import com.primus.data.local.models.SquadByPositionEntity
import com.primus.data.local.models.SquadEntity
import com.primus.data.local.models.StandingEntity
import com.primus.data.local.models.TableEntity
import com.primus.data.local.models.TeamEntity
import com.primus.data.local.models.TeamInfoEntity
import com.primus.data.local.models.TeamMatchesEntity
import com.primus.data.local.models.WinnerEntity

@RealmModule(library = false, classes = [CompetitionScorersEntity::class])
data class CompetitionScorersDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [ScorerEntity::class])
data class ScorerDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [PlayerEntity::class])
data class PlayerDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [CompetitionStandingsEntity::class])
data class CompetitionStandingsDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [CompetitionEmbeddedEntity::class])
data class CompetitionEmbeddedDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [FiltersEntity::class])
data class FiltersDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [SeasonEntity::class])
data class SeasonDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [StandingEntity::class])
data class StandingDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [TableEntity::class])
data class TableDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [TeamEntity::class])
data class TeamDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [CompetitionEntity::class])
data class CompetitionDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [AreaEntity::class])
data class AreaDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [CurrentSeasonEntity::class])
data class CurrentSeasonDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [WinnerEntity::class])
data class WinnerDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [CompetitionMatchesEntity::class])
data class CompetitionMatchesDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [MatchEntity::class])
data class MatchDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [AwayTeamEntity::class])
data class AwayTeamDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [HomeTeamEntity::class])
data class HomeTeamDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [RefereeEntity::class])
data class RefereeDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [ScoreEntity::class])
data class ScoreDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [FullTimeEntity::class])
data class FullTimeDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [HalfTimeEntity::class])
data class HalfTimeDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [MatchesByTourEntity::class])
data class MatchesByTourDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [TeamInfoEntity::class])
data class TeamInfoDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [CoachEntity::class])
data class CoachDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [SquadByPositionEntity::class])
data class SquadByPositionDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [SquadEntity::class])
data class SquadDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [ContractEntity::class])
data class ContractDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [TeamMatchesEntity::class])
data class TeamMatchesDbModule(val placeholder: String) {
    constructor() : this("")
}