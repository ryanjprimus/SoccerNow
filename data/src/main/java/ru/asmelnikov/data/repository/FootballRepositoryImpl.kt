package ru.asmelnikov.data.repository

import retrofit2.Response
import ru.asmelnikov.data.api.FootballApi
import ru.asmelnikov.data.mappers.toCompetition
import ru.asmelnikov.data.models.CompetitionModelDTO
import ru.asmelnikov.domain.repository.FootballRepository
import ru.asmelnikov.domain.models.Competition
import ru.asmelnikov.utils.ErrorsTypesHttp
import ru.asmelnikov.utils.Resource
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException

class FootballRepositoryImpl(
    private val footballApi: FootballApi
) : FootballRepository {

    override suspend fun getAllCompetitions(): Resource<List<Competition>> {
        return executeSafely {
            val response: Response<CompetitionModelDTO> = footballApi.getAllFootballCompetitions()
            if (response.isSuccessful) {
                val competitions: List<Competition> = response.body()?.competitions?.map {
                    it.toCompetition()
                } ?: emptyList()
                Resource.Success(competitions)
            } else {
                responseFailureHandler(response)
            }
        }
    }


    private suspend fun <T> executeSafely(block: suspend () -> Resource<T>): Resource<T> {
        return try {
            block()
        } catch (e: Exception) {
            when (e) {
                is ConnectException, is SocketException -> {
                    Resource.Error(httpErrors = ErrorsTypesHttp.MissingConnection)
                }

                is SocketTimeoutException -> {
                    Resource.Error(httpErrors = ErrorsTypesHttp.TimeoutException)
                }

                else -> {
                    Resource.Error(httpErrors = ErrorsTypesHttp.UnknownError(e.message))
                }
            }
        }
    }

    private fun <T, R> responseFailureHandler(response: Response<T>): Resource.Error<R> {
        return when (response.code()) {
            in 400..499 -> {
                Resource.Error(httpErrors = ErrorsTypesHttp.Https400Errors)
            }

            in 500..599 -> {
                Resource.Error(httpErrors = ErrorsTypesHttp.Https500Errors)
            }

            else -> {
                Resource.Error(httpErrors = ErrorsTypesHttp.UnknownError(response.message()))
            }
        }
    }
}