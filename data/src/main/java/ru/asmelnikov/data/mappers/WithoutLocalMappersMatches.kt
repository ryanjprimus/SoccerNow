package ru.asmelnikov.data.mappers

import ru.asmelnikov.data.models.AwayTeamDTO
import ru.asmelnikov.data.models.CompetitionMatchesDTO
import ru.asmelnikov.data.models.FullTimeDTO
import ru.asmelnikov.data.models.HalfTimeDTO
import ru.asmelnikov.data.models.HomeTeamDTO
import ru.asmelnikov.data.models.MatchDTO
import ru.asmelnikov.data.models.RefereeDTO
import ru.asmelnikov.data.models.ScoreDTO
import ru.asmelnikov.domain.models.AwayTeam
import ru.asmelnikov.domain.models.CompetitionMatches
import ru.asmelnikov.domain.models.FullTime
import ru.asmelnikov.domain.models.HalfTime
import ru.asmelnikov.domain.models.HomeTeam
import ru.asmelnikov.domain.models.Match
import ru.asmelnikov.domain.models.MatchesByTour
import ru.asmelnikov.domain.models.Referee
import ru.asmelnikov.domain.models.Score
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun CompetitionMatchesDTO.toCompetitionMatches(): CompetitionMatches {
    return CompetitionMatches(
        id = this.competition?.id.toString(),
        season = createYearRange(
            this.matches?.firstOrNull()?.season?.startDate ?: "",
            this.matches?.firstOrNull()?.season?.endDate ?: ""
        ),
        matchesByTourCompleted = groupMatchesByTourCompleted(matches ?: emptyList()),
        matchesByTourAhead = groupMatchesByTourAhead(matches ?: emptyList())
    )
}

fun groupMatchesByTourCompleted(matches: List<MatchDTO>): List<MatchesByTour> {
    val groupedMatches = matches.groupBy { it.stage }
    val result = mutableListOf<MatchesByTour>()

    groupedMatches.forEach { (stage, matchesByStage) ->
        val matchesByTour = matchesByStage.groupBy { it.matchday }
        matchesByTour.forEach { (matchday, matchesByMatchday) ->
            val completedMatches =
                matchesByMatchday.filter { it.status == "FINISHED" }.map { it.toMatch() }
            if (completedMatches.isNotEmpty()) {
                result.add(
                    MatchesByTour(
                        matchday ?: -1,
                        matches.firstOrNull()?.stage ?: "",
                        stage,
                        completedMatches
                    )
                )
            }
        }
    }
    return result.reversed()
}

fun groupMatchesByTourAhead(matches: List<MatchDTO>): List<MatchesByTour> {
    val groupedMatches = matches.groupBy { it.stage }
    val result = mutableListOf<MatchesByTour>()

    groupedMatches.forEach { (stage, matchesByStage) ->
        val matchesByTour = matchesByStage.groupBy { it.matchday }
        matchesByTour.forEach { (matchday, matchesByMatchday) ->
            val aheadMatches =
                matchesByMatchday.filter { it.status != "FINISHED" }.map { it.toMatch() }
            if (aheadMatches.isNotEmpty()) {
                result.add(
                    MatchesByTour(
                        matchday ?: -1,
                        matches.firstOrNull()?.stage ?: "",
                        stage,
                        aheadMatches
                    )
                )
            }
        }
    }

    return result
}

fun MatchDTO.toMatch(): Match {
    val dateTime = LocalDateTime.parse(utcDate, DateTimeFormatter.ISO_DATE_TIME)
    val zoneId = ZoneId.ofOffset("GMT", ZoneOffset.ofHours(5))
    val zonedDateTime = dateTime.atZone(zoneId)
    val outputDateTime = zonedDateTime.format(DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm"))
    val bigOutputDateTime = zonedDateTime.format(DateTimeFormatter.ofPattern("dd.MM"))
    return Match(
        awayTeam = awayTeam.toAwayTeam(),
        group = group ?: "",
        homeTeam = homeTeam.toHomeTeam(),
        id = id ?: -1,
        lastUpdated = lastUpdated ?: "",
        matchday = matchday ?: -1,
        referees = referees?.map { it.toReferee() } ?: emptyList(),
        score = score.toScore(),
        stage = stage ?: "",
        status = status ?: "",
        utcDate = outputDateTime ?: "",
        bigDate = bigOutputDateTime ?: "",
        area = area.toArea(),
        competition = competition.toCompetition()
    )
}

fun AwayTeamDTO?.toAwayTeam(): AwayTeam {
    return AwayTeam(
        crest = this?.crest ?: "",
        id = this?.id ?: -1,
        name = this?.name ?: "",
        shortName = this?.shortName ?: "",
        tla = this?.tla ?: ""
    )
}

fun HomeTeamDTO?.toHomeTeam(): HomeTeam {
    return HomeTeam(
        crest = this?.crest ?: "",
        id = this?.id ?: -1,
        name = this?.name ?: "",
        shortName = this?.shortName ?: "",
        tla = this?.tla ?: ""
    )
}

fun RefereeDTO.toReferee(): Referee {
    return Referee(
        id = id ?: -1,
        name = name ?: "",
        nationality = nationality ?: "",
        type = type ?: ""
    )
}

fun ScoreDTO?.toScore(): Score {
    return Score(
        duration = this?.duration ?: "",
        winner = this?.winner ?: "",
        fullTime = this?.fullTime.toFullTime(),
        halfTime = this?.halfTime.toHalfTime(),
    )
}

fun FullTimeDTO?.toFullTime(): FullTime {
    return FullTime(
        away = this?.away ?: -1,
        home = this?.away ?: -1
    )
}

fun HalfTimeDTO?.toHalfTime(): HalfTime {
    return HalfTime(
        away = this?.away ?: -1,
        home = this?.away ?: -1
    )
}