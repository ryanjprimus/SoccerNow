package ru.asmelnikov.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CompetitionMatches(
    val id: String = "",
    val season: String = "",
    val matchesByTourCompleted: List<MatchesByTour> = emptyList(),
    val matchesByTourAhead: List<MatchesByTour> = emptyList()
) : Parcelable

@Parcelize
data class MatchesByTour(
    val matchday: Int,
    val seasonType: String,
    val stage: String?,
    val matches: List<Match>
) : Parcelable

@Parcelize
data class Match(
    val awayTeam: AwayTeam,
    val group: String,
    val homeTeam: HomeTeam,
    val id: Int,
    val lastUpdated: String,
    val matchday: Int,
    val referees: List<Referee>,
    val score: Score,
    val stage: String,
    val status: String,
    val utcDate: String,
    val bigDate: String
) : Parcelable

@Parcelize
data class AwayTeam(
    val crest: String,
    val id: Int,
    val name: String,
    val shortName: String,
    val tla: String
) : Parcelable

@Parcelize
data class HomeTeam(
    val crest: String,
    val id: Int,
    val name: String,
    val shortName: String,
    val tla: String
) : Parcelable

@Parcelize
data class Referee(
    val id: Int,
    val name: String,
    val nationality: String,
    val type: String
) : Parcelable

@Parcelize
data class Score(
    val duration: String,
    val fullTime: FullTime,
    val halfTime: HalfTime,
    val winner: String
) : Parcelable

@Parcelize
data class FullTime(
    val away: Int,
    val home: Int
) : Parcelable

@Parcelize
data class HalfTime(
    val away: Int,
    val home: Int
) : Parcelable