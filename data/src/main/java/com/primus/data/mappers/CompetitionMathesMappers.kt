package com.primus.data.mappers

import io.realm.RealmList
import com.primus.data.local.models.AwayTeamEntity
import com.primus.data.local.models.CompetitionMatchesEntity
import com.primus.data.local.models.FullTimeEntity
import com.primus.data.local.models.HalfTimeEntity
import com.primus.data.local.models.HomeTeamEntity
import com.primus.data.local.models.MatchEntity
import com.primus.data.local.models.MatchesByTourEntity
import com.primus.data.local.models.RefereeEntity
import com.primus.data.local.models.ScoreEntity
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
import com.primus.utils.getDescription
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


fun CompetitionMatchesDTO.toCompetitionMatchesEntity(): CompetitionMatchesEntity {
    return CompetitionMatchesEntity(
        id = this.competition?.id.toString(),
        seasonType = this.matches?.firstOrNull()?.competition?.type ?: "",
        season = createYearRange(
            this.matches?.firstOrNull()?.season?.startDate ?: "",
            this.matches?.firstOrNull()?.season?.endDate ?: ""
        ),
        matchesByTourCompleted = groupMatchesByStageAndTourCompletedDTO(matches ?: emptyList()),
        matchesByTourAhead = groupMatchesByStageAndTourAheadDTO(matches ?: emptyList())
    )
}

fun groupMatchesByStageAndTourCompletedDTO(matches: List<MatchDTO>): RealmList<MatchesByTourEntity> {
    val filteredMatches =
        matches.filter { it.status == "FINISHED" && it.homeTeam?.id != null && it.awayTeam?.id != null }

    val groupedMatches = filteredMatches.groupBy { it.stage }

    val result = mutableListOf<MatchesByTourEntity>()

    groupedMatches.forEach { (stage, matchesByStage) ->
        val matchesByTourEntities = matchesByStage.groupBy { it.matchday }

        matchesByTourEntities.forEach { (matchday, matchesByMatchday) ->
            val matchesByTourEntity = MatchesByTourEntity(
                stage = getDescription(stage),
                matchday = matchday,
                matches = RealmList()
            )

            matchesByMatchday.forEach { matchDto ->
                matchesByTourEntity.matches?.add(matchDto.toMatchEntity())
            }

            result.add(matchesByTourEntity)
        }
    }

    return RealmList<MatchesByTourEntity>().apply {
        addAll(result.reversed())
    }
}


fun groupMatchesByStageAndTourAheadDTO(matches: List<MatchDTO>): RealmList<MatchesByTourEntity> {
    val groupedMatches =
        matches.filter { it.status != "FINISHED" && it.homeTeam?.id != null && it.awayTeam?.id != null }
            .groupBy { it.stage }
    val result = RealmList<MatchesByTourEntity>()
    groupedMatches.forEach { (stage, matchesByStage) ->
        val matchesByTourEntities = matchesByStage.groupBy { it.matchday }
        matchesByTourEntities.entries.sortedBy { it.key }.forEach { (matchday, matchesByMatchday) ->
            val matchesByTourEntity =
                MatchesByTourEntity(
                    stage = getDescription(stage),
                    matchday = matchday,
                    matches = RealmList()
                )
            matchesByMatchday.forEach { matchDto ->
                matchesByTourEntity.matches?.add(matchDto.toMatchEntity())
            }
            result.add(matchesByTourEntity)
        }
    }
    return result
}


fun MatchDTO.toMatchEntity(): MatchEntity {
    val referees: RealmList<RefereeEntity> = RealmList()
    this@toMatchEntity.referees?.map { it.toRefereeEntity() }
        ?.let { referees.addAll(it) }
    return MatchEntity(
        area = area.toAreaEntity(),
        competition = competition?.toCompetitionEntity(),
        awayTeam = awayTeam?.toAwayTeamEntity(),
        group = group,
        homeTeam = homeTeam?.toHomeTeamEntity(),
        id = id,
        lastUpdated = lastUpdated,
        matchday = matchday,
        referees = referees,
        score = score?.toScoreEntity(),
        stage = stage,
        status = status,
        utcDate = utcDate
    )
}

fun AwayTeamDTO.toAwayTeamEntity(): AwayTeamEntity {
    return AwayTeamEntity(
        crest = crest,
        id = id,
        name = name,
        shortName = shortName,
        tla = tla
    )
}

fun HomeTeamDTO.toHomeTeamEntity(): HomeTeamEntity {
    return HomeTeamEntity(
        crest = crest,
        id = id,
        name = name,
        shortName = shortName,
        tla = tla
    )
}

fun RefereeDTO.toRefereeEntity(): RefereeEntity {
    return RefereeEntity(
        id = id,
        name = name,
        nationality = nationality,
        type = type
    )
}

fun ScoreDTO.toScoreEntity(): ScoreEntity {
    return ScoreEntity(
        duration = duration,
        winner = winner,
        fullTime = fullTime?.toFullTimeEntity(),
        halfTime = halfTime?.toHalfTimeEntity(),
    )
}

fun FullTimeDTO.toFullTimeEntity(): FullTimeEntity {
    return FullTimeEntity(
        away = away,
        home = home
    )
}

fun HalfTimeDTO.toHalfTimeEntity(): HalfTimeEntity {
    return HalfTimeEntity(
        away = away,
        home = home
    )
}


fun CompetitionMatchesEntity.toCompetitionMatches(): CompetitionMatches {
    return CompetitionMatches(
        id = id,
        season = season,
        matchesByTourCompleted = matchesByTourCompleted?.map { it.toMatchByTour(seasonType) }
            ?: emptyList(),
        matchesByTourAhead = matchesByTourAhead?.map { it.toMatchByTour(seasonType) } ?: emptyList()
    )
}

fun MatchesByTourEntity.toMatchByTour(seasonType: String): MatchesByTour {
    return MatchesByTour(
        matchday = matchday ?: -1,
        seasonType = seasonType,
        stage = stage,
        matches = matches?.map { it.toMatches() } ?: emptyList()
    )
}

fun MatchEntity.toMatches(): Match {
    return Match(
        area = area.toArea(),
        competition = competition.toCompetition(),
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
        utcDate = utcDate?.toDate()?.formatTo("dd MMMM yyyy, HH:mm") ?: "",
        bigDate = utcDate?.toDate()?.formatTo("dd.MM") ?: "",
    )
}

fun AwayTeamEntity?.toAwayTeam(): AwayTeam {
    return AwayTeam(
        crest = this?.crest ?: "",
        id = this?.id ?: -1,
        name = this?.name ?: "",
        shortName = this?.shortName ?: "",
        tla = this?.tla ?: ""
    )
}

fun HomeTeamEntity?.toHomeTeam(): HomeTeam {
    return HomeTeam(
        crest = this?.crest ?: "",
        id = this?.id ?: -1,
        name = this?.name ?: "",
        shortName = this?.shortName ?: "",
        tla = this?.tla ?: ""
    )
}

fun RefereeEntity.toReferee(): Referee {
    return Referee(
        id = id ?: -1,
        name = name ?: "",
        nationality = nationality ?: "",
        type = type ?: ""
    )
}

fun ScoreEntity?.toScore(): Score {
    return Score(
        duration = this?.duration ?: "",
        winner = this?.winner ?: "",
        fullTime = this?.fullTime.toFullTime(),
        halfTime = this?.halfTime.toHalfTime(),
    )
}

fun FullTimeEntity?.toFullTime(): FullTime {
    return FullTime(
        away = this?.away ?: -1,
        home = this?.home ?: -1
    )
}

fun HalfTimeEntity?.toHalfTime(): HalfTime {
    return HalfTime(
        away = this?.away ?: -1,
        home = this?.home ?: -1
    )
}

fun String.toDate(
    dateFormat: String = "yyyy-MM-dd'T'HH:mm:ssX",
    timeZone: TimeZone = TimeZone.getTimeZone("UTC")
): Date? {
    val parser = SimpleDateFormat(dateFormat, Locale.US)
    parser.timeZone = timeZone
    return parser.parse(this)
}

fun Date.formatTo(
    dateFormat: String,
    timeZone: TimeZone = TimeZone.getTimeZone("Asia/Karachi")
): String { //todo correct time zone
    val formatter = SimpleDateFormat(dateFormat, Locale.US) // todo locale
    formatter.timeZone = timeZone
    return formatter.format(this)
}