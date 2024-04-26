package com.primus.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Head2head(
    val id: Int = -1,
    val aggregates: Aggregates = Aggregates()
) : Parcelable

@Parcelize
data class Aggregates(
    val awayTeam: AwayTeamH2H = AwayTeamH2H(),
    val homeTeam: HomeTeamH2H = HomeTeamH2H(),
    val homeWinsPercentage: Float = -1f,
    val awayWinsPercentage: Float = -1f,
    val drawsPercentage: Float = -1f,
    val numberOfMatches: Int = -1,
    val totalGoals: Int = -1
) : Parcelable

@Parcelize
data class HomeTeamH2H(
    val draws: Int = -1,
    val id: Int = -1,
    val losses: Int = -1,
    val name: String = "",
    val wins: Int = -1
) : Parcelable

@Parcelize
data class AwayTeamH2H(
    val draws: Int = -1,
    val id: Int = -1,
    val losses: Int = -1,
    val name: String = "",
    val wins: Int = -1
) : Parcelable