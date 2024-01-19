package ru.asmelnikov.competitions_main.view_model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import ru.asmelnikov.domain.repository.FootballRepository
import ru.asmelnikov.utils.ErrorsTypesHttp
import ru.asmelnikov.utils.Resource
import ru.asmelnikov.utils.StringResourceProvider
import ru.asmelnikov.utils.R

class CompetitionsScreenViewModel(
    private val footballRepository: FootballRepository,
    private val stringResourceProvider: StringResourceProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    ContainerHost<CompetitionsScreenState, CompetitionsScreenSideEffects> {

    override val container = container<CompetitionsScreenState, CompetitionsScreenSideEffects>(
        initialState = CompetitionsScreenState(),
        savedStateHandle = savedStateHandle
    ) {
        collectCompetitionsFlowFromLocal()
        updateCompetitionsFromRemoteToLocal()
    }

    private fun collectCompetitionsFlowFromLocal() = intent(registerIdling = false) {
        reduce { state.copy(isLoadingLocal = true) }
        repeatOnSubscription {
            footballRepository.getAllCompetitionsFlowFromLocal().collect { comps ->
                if (comps.isNotEmpty())
                    reduce { state.copy(comps = comps, isLoadingLocal = false) }
            }
        }
    }

    fun updateCompetitionsFromRemoteToLocal() = intent {
        reduce { state.copy(isLoadingRemote = true) }
        when (val compsFromRemote = footballRepository.getAllCompetitionsFromRemoteToLocal()) {
            is Resource.Success -> {
                reduce {
                    state.copy(
                        isLoadingRemote = false,
                        comps = compsFromRemote.data ?: emptyList()
                    )
                }
            }

            is Resource.Error -> {
                handleError(compsFromRemote.httpErrors ?: ErrorsTypesHttp.UnknownError())
            }
        }
    }

    private fun handleError(error: ErrorsTypesHttp) = intent {
        reduce { state.copy(isLoadingRemote = false) }

        when (error) {
            is ErrorsTypesHttp.Https400Errors ->
                postSideEffect(
                    CompetitionsScreenSideEffects.Snackbar(
                        stringResourceProvider.getString(
                            resourceId = when (error.errorCode) {
                                429 -> R.string.http_429_errors
                                else -> R.string.http_400_errors
                            },
                            arguments = arrayOf(error.code ?: "")
                        )
                    )
                )

            is ErrorsTypesHttp.Https500Errors -> postSideEffect(
                CompetitionsScreenSideEffects.Snackbar(
                    stringResourceProvider.getString(
                        resourceId = R.string.http_500_errors,
                        arguments = arrayOf(error.errorMessage ?: "")
                    )
                )
            )

            is ErrorsTypesHttp.TimeoutException -> postSideEffect(
                CompetitionsScreenSideEffects.Snackbar(
                    stringResourceProvider.getString(
                        resourceId = R.string.http_timeout_error
                    )
                )
            )

            is ErrorsTypesHttp.MissingConnection -> postSideEffect(
                CompetitionsScreenSideEffects.Snackbar(
                    stringResourceProvider.getString(
                        resourceId = R.string.http_missing_error
                    )
                )
            )

            is ErrorsTypesHttp.NetworkError -> postSideEffect(
                CompetitionsScreenSideEffects.Snackbar(
                    stringResourceProvider.getString(
                        resourceId = R.string.http_network_error,
                        arguments = arrayOf(error.errorMessage ?: "")
                    )
                )
            )

            else -> postSideEffect(
                CompetitionsScreenSideEffects.Snackbar(
                    stringResourceProvider.getString(
                        resourceId = R.string.http_unknown_error,
                        arguments = arrayOf(error.errorMessage ?: "")
                    )
                )
            )

        }
    }
}