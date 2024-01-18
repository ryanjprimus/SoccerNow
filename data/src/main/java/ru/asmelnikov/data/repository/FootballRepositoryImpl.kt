package ru.asmelnikov.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import retrofit2.Response
import ru.asmelnikov.data.api.FootballApi
import ru.asmelnikov.data.local.RealmOptions
import ru.asmelnikov.data.mappers.toCompetition
import ru.asmelnikov.data.mappers.toCompetitionEntity
import ru.asmelnikov.data.models.CompetitionModelDTO
import ru.asmelnikov.domain.repository.FootballRepository
import ru.asmelnikov.domain.models.Competition
import ru.asmelnikov.utils.ErrorsTypesHttp
import ru.asmelnikov.utils.Resource
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException

class FootballRepositoryImpl(
    private val footballApi: FootballApi,
    private val realmOptions: RealmOptions
) : FootballRepository {

    override suspend fun getAllCompetitionsFromRemoteToLocal(): Resource<List<Competition>> {
        return executeSafely {
            val response: Response<CompetitionModelDTO> = footballApi.getAllFootballCompetitions()
            if (response.isSuccessful && response.code() == 200) {
                val competitions = response.body()?.competitions?.map {
                    it.toCompetitionEntity()
                } ?: emptyList()
                realmOptions.upsertCompetitionsDataFromRemoteToLocal(competitions)
                Resource.Success(competitions.map { it.toCompetition() })
            } else {
                responseFailureHandler(response)
            }
        }
    }

    override suspend fun getAllCompetitionsFlowFromLocal(): Flow<List<Competition>> {
        return realmOptions.getCompetitionsFlowFromLocal()
            .map { comps -> comps.map { comp -> comp.toCompetition() } }
    }


    private suspend fun <T> executeSafely(
        block: suspend () -> Resource<T>
    ): Resource<T> {
        return try {
            block()
        } catch (e: Exception) {
            when (e) {
                is ConnectException, is SocketException -> {
                    Resource.Error(httpErrors = ErrorsTypesHttp.MissingConnection())
                }

                is SocketTimeoutException -> {
                    Resource.Error(httpErrors = ErrorsTypesHttp.TimeoutException())
                }

                is IOException -> {
                    Resource.Error(httpErrors = ErrorsTypesHttp.NetworkError(message = e.message))
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
                Resource.Error(
                    httpErrors = ErrorsTypesHttp.Https400Errors(
                        errorCode = response.code()
                    )
                )
            }

            in 500..599 -> {
                Resource.Error(
                    httpErrors = ErrorsTypesHttp.Https500Errors(
                        errorCode = response.code()
                    )
                )
            }

            else -> {
                Resource.Error(httpErrors = ErrorsTypesHttp.UnknownError(message = response.message()))
            }
        }
    }
}