package com.primus.data.mappers

import io.realm.RealmList
import com.primus.data.local.models.CompetitionScorersEntity
import com.primus.data.local.models.PlayerEntity
import com.primus.data.local.models.ScorerEntity
import com.primus.data.models.CompetitionScorersModelDTO
import com.primus.data.models.PlayerDTO
import com.primus.data.models.ScorerDTO
import com.primus.domain.models.CompetitionScorers
import com.primus.domain.models.Player
import com.primus.domain.models.Scorer
import kotlin.random.Random

fun CompetitionScorersModelDTO.toCompetitionScorersEntity(): CompetitionScorersEntity {
    val scorers: RealmList<ScorerEntity> = RealmList()
    this@toCompetitionScorersEntity.scorers?.map { it.toScorerEntity() }
        ?.let { scorers.addAll(it) }
    return CompetitionScorersEntity(
        id = (this.competition?.id ?: -1).toString(),
        scorers = scorers,
        season = this.season?.toSeasonEntity()
    )
}

fun CompetitionScorersEntity.toCompetitionScorers(): CompetitionScorers {
    return CompetitionScorers(
        id = id,
        scorers = this.scorers?.map { it.toScorer() } ?: emptyList(),
        season = this.season.toSeason(),
    )
}

fun ScorerDTO.toScorerEntity(): ScorerEntity {
    return ScorerEntity(
        assists = assists,
        goals = goals,
        penalties = penalties,
        playedMatches = playedMatches,
        player = player?.toPlayerEntity(),
        team = team?.toTeamEntity()
    )
}

fun PlayerDTO.toPlayerEntity(): PlayerEntity {
    return PlayerEntity(
        dateOfBirth = dateOfBirth,
        firstName = firstName,
        id = id ?: Random.nextInt(100, 1000),
        lastName = lastName,
        lastUpdated = lastUpdated,
        name = name,
        nationality = nationality,
        position = position,
        section = section,
        shirtNumber = shirtNumber
    )
}

fun ScorerEntity.toScorer(): Scorer {
    return Scorer(
        assists = assists ?: 0,
        goals = goals ?: 0,
        penalties = penalties ?: 0,
        playedMatches = playedMatches ?: 0,
        player = this.player.toPlayer(),
        team = this.team.toTeam(),
    )
}

fun PlayerEntity?.toPlayer(): Player {
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
        shirtNumber = this?.shirtNumber ?: 0
    )
}