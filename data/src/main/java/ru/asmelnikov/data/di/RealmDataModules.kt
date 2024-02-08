package ru.asmelnikov.data.di

import io.realm.annotations.RealmModule
import ru.asmelnikov.data.local.models.AreaEntity
import ru.asmelnikov.data.local.models.AwayTeamEntity
import ru.asmelnikov.data.local.models.CompetitionEmbeddedEntity
import ru.asmelnikov.data.local.models.CompetitionEntity
import ru.asmelnikov.data.local.models.CompetitionMatchesEntity
import ru.asmelnikov.data.local.models.CompetitionScorersEntity
import ru.asmelnikov.data.local.models.CompetitionStandingsEntity
import ru.asmelnikov.data.local.models.CurrentSeasonEntity
import ru.asmelnikov.data.local.models.FiltersEntity
import ru.asmelnikov.data.local.models.FullTimeEntity
import ru.asmelnikov.data.local.models.HalfTimeEntity
import ru.asmelnikov.data.local.models.HomeTeamEntity
import ru.asmelnikov.data.local.models.MatchEntity
import ru.asmelnikov.data.local.models.MatchesByTourEntity
import ru.asmelnikov.data.local.models.PlayerEntity
import ru.asmelnikov.data.local.models.RefereeEntity
import ru.asmelnikov.data.local.models.ScoreEntity
import ru.asmelnikov.data.local.models.ScorerEntity
import ru.asmelnikov.data.local.models.SeasonEntity
import ru.asmelnikov.data.local.models.StandingEntity
import ru.asmelnikov.data.local.models.TableEntity
import ru.asmelnikov.data.local.models.TeamEntity
import ru.asmelnikov.data.local.models.WinnerEntity

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