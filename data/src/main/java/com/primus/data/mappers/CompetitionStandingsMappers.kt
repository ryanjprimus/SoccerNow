package com.primus.data.mappers

import io.realm.RealmList
import com.primus.data.local.models.CompetitionEmbeddedEntity
import com.primus.data.local.models.CompetitionStandingsEntity
import com.primus.data.local.models.FiltersEntity
import com.primus.data.local.models.SeasonEntity
import com.primus.data.local.models.StandingEntity
import com.primus.data.local.models.TableEntity
import com.primus.data.local.models.TeamEntity
import com.primus.data.models.CompetitionDTO
import com.primus.data.models.CompetitionStandingsModelDTO
import com.primus.data.models.FiltersDTO
import com.primus.data.models.SeasonDTO
import com.primus.data.models.StandingDTO
import com.primus.data.models.TableDTO
import com.primus.data.models.TeamDTO
import com.primus.domain.models.Competition
import com.primus.domain.models.CompetitionStandings
import com.primus.domain.models.Filters
import com.primus.domain.models.Season
import com.primus.domain.models.Standing
import com.primus.domain.models.Table
import com.primus.domain.models.Team
import kotlin.random.Random

fun CompetitionStandingsModelDTO.toCompetitionStandingsEntity(): CompetitionStandingsEntity {
    val standings: RealmList<StandingEntity> = RealmList()
    this@toCompetitionStandingsEntity.standings?.map { it.toStandingEntity() }
        ?.filter { it.type == "TOTAL" }
        ?.let { standings.addAll(it) }
    return CompetitionStandingsEntity(
        id = this.competition?.id.toString(),
        area = this.area.toAreaEntity(),
        competition = this.competition?.toCompetitionEmbeddedEntity(),
        filters = this.filters?.toFiltersEntity(),
        season = this.season?.toSeasonEntity(),
        standings = standings
    )
}

fun CompetitionDTO.toCompetitionEmbeddedEntity(): CompetitionEmbeddedEntity {
    val seasons: RealmList<SeasonEntity> = RealmList()
    this@toCompetitionEmbeddedEntity.seasons?.map {
        it.toSeasonEntity()
    }?.take(4)?.let { seasons.addAll(it) }
    return CompetitionEmbeddedEntity(
        area = area.toAreaEntity(),
        code = code ?: "",
        currentSeason = currentSeason.toCurrentSeasonEntity(),
        emblem = emblem ?: "",
        id = id ?: -1,
        lastUpdated = lastUpdated ?: "",
        name = name ?: "",
        numberOfAvailableSeasons = numberOfAvailableSeasons ?: -1,
        plan = plan ?: "",
        type = type ?: "",
        seasons = seasons
    )
}

fun FiltersDTO.toFiltersEntity(): FiltersEntity {
    return FiltersEntity(
        season = season ?: ""
    )
}

fun SeasonDTO.toSeasonEntity(): SeasonEntity {
    return SeasonEntity(
        currentMatchday = currentMatchday ?: -1,
        endDate = endDate ?: "",
        id = id ?: -1,
        startDate = startDate ?: "",
        winner = winner.toWinnerEntity()
    )
}

fun StandingDTO.toStandingEntity(): StandingEntity {
    val table: RealmList<TableEntity> = RealmList()
    this@toStandingEntity.table?.map { it.toTableEntity() }?.let { table.addAll(it) }
    return StandingEntity(
        group = group ?: "",
        stage = stage ?: "",
        type = type ?: "",
        table = table
    )
}

fun TableDTO?.toTableEntity(): TableEntity {
    return TableEntity(
        draw = this?.draw ?: -1,
        form = this?.form ?: "",
        goalDifference = this?.goalDifference ?: -1,
        goalsAgainst = this?.goalsAgainst ?: -1,
        goalsFor = this?.goalsFor ?: -1,
        lost = this?.lost ?: -1,
        playedGames = this?.playedGames ?: -1,
        points = this?.points ?: -1,
        position = this?.position ?: -1,
        team = this?.team?.toTeamEntity(),
        won = this?.won ?: -1
    )
}

fun TeamDTO.toTeamEntity(): TeamEntity {
    return TeamEntity(
        crest = crest ?: "",
        id = id ?: Random.nextInt(100, 1000),
        name = name ?: "",
        shortName = shortName ?: "",
        tla = tla ?: ""
    )
}

fun CompetitionStandingsEntity?.toCompetitionStandings(): CompetitionStandings {
    return CompetitionStandings(
        id = this?.competition?.id.toString(),
        area = this?.area.toArea(),
        competition = this?.competition.toCompetition(),
        filters = this?.filters.toFilters(),
        season = this?.season.toSeason(),
        standings = this?.standings?.map { it.toStanding() } ?: emptyList()
    )
}


fun CompetitionEmbeddedEntity?.toCompetition(): Competition {
    return Competition(
        area = this?.area.toArea(),
        code = this?.code ?: "",
        currentSeason = this?.currentSeason.toCurrentSeason(),
        emblem = this?.emblem ?: "",
        id = this?.id ?: -1,
        lastUpdated = this?.lastUpdated ?: "",
        name = this?.name ?: "",
        numberOfAvailableSeasons = this?.numberOfAvailableSeasons ?: -1,
        plan = this?.plan ?: "",
        type = this?.type ?: "",
        seasons = this?.seasons?.map { it.toSeason() } ?: emptyList()
    )
}

fun FiltersEntity?.toFilters(): Filters {
    return Filters(
        season = this?.season ?: ""
    )
}

fun SeasonEntity?.toSeason(): Season {
    return Season(
        currentMatchday = this?.currentMatchday ?: -1,
        startDateEndDate = createYearRange(this?.startDate ?: "", this?.endDate ?: ""),
        endDate = this?.endDate ?: "",
        id = this?.id ?: -1,
        startDate = this?.startDate ?: "",
        winner = this?.winner.toWinner()
    )
}

fun StandingEntity.toStanding(): Standing {
    return Standing(
        group = group,
        stage = stage,
        type = type,
        table = this.table?.map { it.toTable() } ?: emptyList()
    )
}

fun TableEntity.toTable(): Table {
    return Table(
        draw = draw,
        form = form,
        goalDifference = goalDifference,
        goalsAgainst = goalsAgainst,
        goalsFor = goalsFor,
        lost = lost,
        playedGames = playedGames,
        points = points,
        position = position,
        team = this.team.toTeam(),
        won = won
    )
}

fun TeamEntity?.toTeam(): Team {
    return Team(
        crest = this?.crest ?: "",
        id = this?.id ?: -1,
        name = this?.name ?: "",
        shortName = this?.shortName ?: "",
        tla = this?.tla ?: ""
    )
}