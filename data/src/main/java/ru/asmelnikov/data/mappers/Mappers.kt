package ru.asmelnikov.data.mappers

import ru.asmelnikov.data.models.AreaDTO
import ru.asmelnikov.data.models.CompetitionDTO
import ru.asmelnikov.data.models.CompetitionModelDTO
import ru.asmelnikov.data.models.CurrentSeasonDTO
import ru.asmelnikov.data.models.FiltersDTO
import ru.asmelnikov.data.models.WinnerDTO
import ru.asmelnikov.domain.models.Area
import ru.asmelnikov.domain.models.Competition
import ru.asmelnikov.domain.models.CompetitionModel
import ru.asmelnikov.domain.models.CurrentSeason
import ru.asmelnikov.domain.models.Filters
import ru.asmelnikov.domain.models.Winner

fun CompetitionModelDTO.toCompetitionModel(): CompetitionModel {
    return CompetitionModel(
        count = count ?: 0,
        filters = filters.toFilters(),
        competitions = this.competitions?.map { it.toCompetition() } ?: emptyList()
    )
}

fun CompetitionDTO.toCompetition(): Competition {
    return Competition(
        area = area.toArea(),
        code = code ?: "",
        currentSeason = currentSeason.toCurrentSeason(),
        emblem = emblem ?: "",
        id = id ?: -1,
        lastUpdated = lastUpdated ?: "",
        name = name ?: "",
        numberOfAvailableSeasons = numberOfAvailableSeasons ?: -1,
        plan = plan ?: "",
        type = type ?: ""
    )
}

fun FiltersDTO?.toFilters(): Filters {
    return Filters(
        client = this?.client ?: ""
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

fun CurrentSeasonDTO?.toCurrentSeason(): CurrentSeason {
    return CurrentSeason(
        currentMatchDay = this?.currentMatchDay ?: -1,
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
        website = this?.website ?: ""
    )
}