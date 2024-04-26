package com.primus.data.mappers

import io.realm.RealmList
import com.primus.data.local.models.CoachEntity
import com.primus.data.local.models.ContractEntity
import com.primus.data.local.models.SquadByPositionEntity
import com.primus.data.local.models.SquadEntity
import com.primus.data.local.models.TeamInfoEntity
import com.primus.data.models.CoachDTO
import com.primus.data.models.ContractDTO
import com.primus.data.models.SquadDTO
import com.primus.data.models.TeamInfoDTO
import com.primus.domain.models.Coach
import com.primus.domain.models.Contract
import com.primus.domain.models.Squad
import com.primus.domain.models.SquadByPosition
import com.primus.domain.models.TeamInfo
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

fun TeamInfoDTO.toTeamInfoEntity(): TeamInfoEntity {
    return TeamInfoEntity(
        address = address ?: "",
        area = area.toAreaEntity(),
        clubColors = clubColors ?: "",
        coach = coach.toCoachEntity(),
        crest = crest ?: "",
        founded = founded ?: -1,
        id = id.toString(),
        lastUpdated = lastUpdated ?: "",
        name = name ?: "",
        shortName = shortName ?: "",
        squadByPosition = convertToRealmList(squad),
        tla = tla ?: "",
        venue = venue ?: "",
        website = website ?: ""
    )
}

fun CoachDTO?.toCoachEntity(): CoachEntity {
    return CoachEntity(
        contract = this?.contract.toContractEntity(),
        dateOfBirth = this?.dateOfBirth ?: "",
        firstName = this?.firstName ?: "",
        id = this?.id ?: -1,
        lastName = this?.lastName ?: "",
        name = this?.name ?: "",
        nationality = this?.nationality ?: ""
    )
}

fun ContractDTO?.toContractEntity(): ContractEntity {
    return ContractEntity(
        start = this?.start ?: "",
        until = this?.until ?: ""
    )
}

fun TeamInfoEntity.toTeamInfo(): TeamInfo {
    return TeamInfo(
        address = address,
        area = area.toArea(),
        clubColors = clubColors,
        coach = coach.toCoach(),
        crest = crest,
        founded = founded,
        id = id,
        lastUpdated = lastUpdated,
        name = name,
        shortName = shortName,
        squadByPosition = squadByPosition?.map { it.toSquadByPosition() } ?: emptyList(),
        tla = tla,
        venue = venue,
        website = website
    )
}

fun CoachEntity?.toCoach(): Coach {
    return Coach(
        contract = this?.contract.toContract(),
        dateOfBirth = this?.dateOfBirth ?: "",
        firstName = this?.firstName ?: "",
        id = this?.id ?: -1,
        lastName = this?.lastName ?: "",
        name = this?.name ?: "",
        nationality = this?.nationality ?: ""
    )
}

fun ContractEntity?.toContract(): Contract {
    return Contract(
        start = this?.start ?: "",
        until = this?.until ?: ""
    )
}

fun SquadByPositionEntity.toSquadByPosition(): SquadByPosition {
    return SquadByPosition(
        position = position,
        squad = squad?.map { it.toSquad() } ?: emptyList()
    )
}

fun SquadEntity.toSquad(): Squad {
    return Squad(
        age = dateOfBirth.calculateAge(),
        id = id,
        name = name,
        nationality = nationality
    )
}

fun convertToRealmList(squadDTOList: List<SquadDTO>?): RealmList<SquadByPositionEntity> {
    val squadByPositionList = RealmList<SquadByPositionEntity>()
    squadDTOList?.groupBy { it.position }?.forEach { (position, squadDTOs) ->
        val squadEntityList = RealmList<SquadEntity>()
        squadDTOs.forEach { squadDTO ->
            val squadEntity = SquadEntity(
                squadDTO.dateOfBirth ?: "",
                squadDTO.id ?: -1,
                squadDTO.name ?: "",
                squadDTO.nationality ?: ""
            )
            squadEntityList.add(squadEntity)
        }
        val squadByPositionEntity = SquadByPositionEntity(position ?: "", squadEntityList)
        squadByPositionList.add(squadByPositionEntity)
    }
    return squadByPositionList
}

fun String.calculateAge(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return try {
        val dob = LocalDate.parse(this, formatter)
        val currentDate = LocalDate.now()
        val age = Period.between(dob, currentDate).years
        age.toString()
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}
