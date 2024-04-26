package com.primus.data.repository

import com.primus.data.api.FootballApi
import com.primus.data.mappers.toPerson
import com.primus.data.retrofit_errors_handler.RetrofitErrorsHandler
import com.primus.domain.models.Person
import com.primus.domain.repository.PersonRepository
import com.primus.utils.Resource

class PersonRepositoryImpl(
    private val footballApi: FootballApi,
    private val retrofitErrorsHandler: RetrofitErrorsHandler
) : PersonRepository {
    override suspend fun getPersonInfo(personId: String): Resource<Person> {
        return retrofitErrorsHandler.executeSafely {
            val response = footballApi.getPersonInfo(personId)
            if (response.isSuccessful && response.code() == 200) {
                Resource.Success(response.body()?.toPerson() ?: Person())
            } else {
                retrofitErrorsHandler.responseFailureHandler(response)
            }
        }
    }
}