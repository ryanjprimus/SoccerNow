package com.primus.data.mappers

import com.primus.data.models.AreaDTO
import com.primus.data.models.CompetitionDTO
import com.primus.data.models.CompetitionStandingsModelDTO
import com.primus.data.models.CurrentSeasonDTO
import com.primus.data.models.FiltersDTO
import com.primus.data.models.PlayerDTO
import com.primus.data.models.ScorerDTO
import com.primus.data.models.SeasonDTO
import com.primus.data.models.StandingDTO
import com.primus.data.models.TableDTO
import com.primus.data.models.TeamDTO
import com.primus.data.models.WinnerDTO
import com.primus.domain.models.Area
import com.primus.domain.models.Competition
import com.primus.domain.models.CompetitionStandings
import com.primus.domain.models.CurrentSeason
import com.primus.domain.models.Filters
import com.primus.domain.models.Player
import com.primus.domain.models.Scorer
import com.primus.domain.models.Season
import com.primus.domain.models.Standing
import com.primus.domain.models.Table
import com.primus.domain.models.Team
import com.primus.domain.models.Winner

fun CompetitionStandingsModelDTO.toCompetitionStandings(): CompetitionStandings {
    return CompetitionStandings(
        id = (competition?.id ?: -1).toString(),
        area = area.toArea(),
        competition = this.competition.toCompetition(),
        filters = filters.toFilters(),
        season = season.toSeason(),
        standings = standings?.map { it.toStanding() }?.filter { it.type == "TOTAL" }
            ?: emptyList()
    )
}

fun ScorerDTO.toScorer(): Scorer {
    return Scorer(
        assists = assists ?: -1,
        goals = goals ?: -1,
        penalties = penalties ?: -1,
        playedMatches = playedMatches ?: -1,
        player = this.player.toPlayer(),
        team = this.team.toTeam(),
    )
}

fun PlayerDTO?.toPlayer(): Player {
    return Player(
        dateOfBirth = this?.dateOfBirth ?: "",
        firstName = this?.firstName ?: "",
        id = this?.id ?: -1,
        lastName = this?.lastName ?: "",
        lastUpdated = this?.lastUpdated ?: "",
        name = this?.name ?: "",
        nationality = this?.nationality ?: "",
        position = this?.position ?: "",
        section = this?.section ?: "",
        shirtNumber = this?.shirtNumber ?: -1
    )
}

fun StandingDTO?.toStanding(): Standing {
    return Standing(
        group = this?.group ?: "",
        stage = this?.stage ?: "",
        type = this?.type ?: "",
        table = this?.table?.map { it.toTable() } ?: emptyList()
    )
}

fun TableDTO?.toTable(): Table {
    return Table(
        draw = this?.draw ?: -1,
        form = this?.form ?: "",
        goalDifference = this?.goalDifference ?: -1,
        goalsAgainst = this?.goalsAgainst ?: -1,
        goalsFor = this?.goalsFor ?: -1,
        lost = this?.lost ?: -1,
        playedGames = this?.playedGames ?: -1,
        points = this?.points ?: -1,
        position = this?.position ?: -1,
        team = this?.team.toTeam(),
        won = this?.won ?: -1
    )
}

fun TeamDTO?.toTeam(): Team {
    return Team(
        crest = this?.crest ?: "",
        id = this?.id ?: -1,
        name = this?.name ?: "",
        shortName = this?.shortName ?: "",
        tla = this?.tla ?: ""
    )
}

fun AreaDTO?.toArea(): Area {
    return Area(
        code = this?.code ?: "",
        flag = this?.flag ?: "",
        id = this?.id ?: -1,
        name = this?.name ?: ""
    )
}

fun CompetitionDTO?.toCompetition(): Competition {
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
        seasons = this?.seasons?.map { it.toSeason() }?.take(4) ?: emptyList()
    )
}

fun SeasonDTO?.toSeason(): Season {
    return Season(
        currentMatchday = this?.currentMatchday ?: -1,
        startDateEndDate = createYearRange(this?.startDate ?: "", this?.endDate ?: ""),
        endDate = this?.endDate ?: "",
        id = this?.id ?: -1,
        startDate = this?.startDate ?: "",
        winner = this?.winner.toWinner()
    )
}

fun FiltersDTO?.toFilters(): Filters {
    return Filters(
        season = this?.season ?: ""
    )
}

fun CurrentSeasonDTO?.toCurrentSeason(): CurrentSeason {
    return CurrentSeason(
        currentMatchDay = this?.currentMatchDay ?: -1,
        startDateEndDate = createYearRange(this?.startDate ?: "", this?.endDate ?: ""),
        endDate = this?.endDate ?: "",
        id = this?.id ?: -1,
        startDate = this?.startDate ?: "",
        winner = this?.winner.toWinner()
    )
}

fun WinnerDTO?.toWinner(): Winner {
    return Winner(
        address = this?.address ?: "",
        clubColors = this?.clubColors ?: "",
        crest = this?.crest ?: "",
        founded = this?.founded ?: -1,
        id = this?.id ?: -1,
        lastUpdated = this?.lastUpdated ?: "",
        name = this?.name ?: "",
        shortName = this?.shortName ?: "",
        tla = this?.tla ?: "",
        website = this?.website ?: "",
        venue = this?.venue ?: ""
    )
}