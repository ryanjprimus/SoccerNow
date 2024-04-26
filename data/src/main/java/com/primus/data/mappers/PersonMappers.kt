package com.primus.data.mappers

import com.primus.data.models.ContractDTO
import com.primus.data.models.CurrentTeamDTO
import com.primus.data.models.PersonDTO
import com.primus.data.models.RunningCompetitionDTO
import com.primus.domain.models.Contract
import com.primus.domain.models.CurrentTeam
import com.primus.domain.models.Person
import com.primus.domain.models.RunningCompetition

fun PersonDTO.toPerson(): Person {
    return Person(
        currentTeam = currentTeam.toCurrentTeam(),
        age = dateOfBirth?.calculateAge() ?: "",
        firstName = firstName ?: "",
        id = id ?: -1,
        lastName = lastName ?: "",
        lastUpdated = lastUpdated ?: "",
        name = name ?: "",
        nationality = nationality ?: "",
        position = position ?: "",
        section = section ?: "",
        shirtNumber = shirtNumber ?: -1
    )
}

fun CurrentTeamDTO?.toCurrentTeam(): CurrentTeam {
    return CurrentTeam(
        address = this?.address ?: "",
        area = this?.area.toArea(),
        clubColors = this?.clubColors ?: "",
        contract = this?.contract.toContract(),
        crest = this?.crest ?: "",
        founded = this?.founded ?: -1,
        id = this?.id ?: -1,
        name = this?.name ?: "",
        runningCompetitions = this?.runningCompetitions?.map { it.toRunningCompetition() }
            ?: emptyList(),
        shortName = this?.shortName ?: "",
        tla = this?.tla ?: "",
        venue = this?.venue ?: "",
        website = this?.website ?: ""
    )
}

fun ContractDTO?.toContract(): Contract {
    return Contract(
        start = this?.start ?: "",
        until = this?.until ?: ""
    )
}

fun RunningCompetitionDTO?.toRunningCompetition(): RunningCompetition {
    return RunningCompetition(
        code = this?.code ?: "",
        emblem = this?.emblem ?: "",
        id = this?.id ?: -1,
        name = this?.name ?: "",
        type = this?.type ?: ""
    )
}