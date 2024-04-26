package com.primus.data.mappers

import com.primus.data.models.AwayTeamDTO
import com.primus.data.models.CompetitionMatchesDTO
import com.primus.data.models.FullTimeDTO
import com.primus.data.models.HalfTimeDTO
import com.primus.data.models.HomeTeamDTO
import com.primus.data.models.MatchDTO
import com.primus.data.models.RefereeDTO
import com.primus.data.models.ScoreDTO
import com.primus.domain.models.AwayTeam
import com.primus.domain.models.CompetitionMatches
import com.primus.domain.models.FullTime
import com.primus.domain.models.HalfTime
import com.primus.domain.models.HomeTeam
import com.primus.domain.models.Match
import com.primus.domain.models.MatchesByTour
import com.primus.domain.models.Referee
import com.primus.domain.models.Score
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