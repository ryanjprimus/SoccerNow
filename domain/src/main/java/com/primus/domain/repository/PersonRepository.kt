package com.primus.domain.repository

import com.primus.domain.models.Person
import com.primus.utils.Resource

interface PersonRepository {

    suspend fun getPersonInfo(personId: String): Resource<Person>

}