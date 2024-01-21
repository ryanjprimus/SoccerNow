package ru.asmelnikov.competition_standings.view_model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import ru.asmelnikov.domain.repository.CompetitionStandingsRepository
import ru.asmelnikov.utils.ErrorsTypesHttp
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.Resource
import ru.asmelnikov.utils.StringResourceProvider

class CompetitionStandingsViewModel(
    private val standingsRepository: CompetitionStandingsRepository,
    private val stringResourceProvider: StringResourceProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    ContainerHost<CompetitionStandingsState, CompetitionStandingSideEffects> {

    override val container = container<CompetitionStandingsState, CompetitionStandingSideEffects>(
        initialState = CompetitionStandingsState(),
        savedStateHandle = savedStateHandle
    )

    fun onBackClick() = intent {
        postSideEffect(CompetitionStandingSideEffects.BackClick)
    }

    fun compIdToState(compId: String) = intent {
        reduce { state.copy(compId = compId) }
        collectStandingsFlowFromLocal()
        updateStandingsFromRemoteToLocal()
    }

    fun getStandingsBySeason(season: String) = intent {
        reduce { state.copy(isLoading = true) }
        when (val standings = standingsRepository.getStandingsBySeason(state.compId, season)) {
            is Resource.Success -> {
                reduce {
                    state.copy(
                        isLoading = false,
                        competitionStandings = standings.data,
                        currentSeason = season
                    )
                }
            }

            is Resource.Error -> {
                handleError(standings.httpErrors ?: ErrorsTypesHttp.UnknownError())
            }
        }
    }

    private fun updateStandingsFromRemoteToLocal() = intent {
        reduce { state.copy(isLoading = true) }
        when (val compsFromRemote =
            standingsRepository.getCompetitionStandingsFromRemoteToLocalById(
                state.compId
            )) {
            is Resource.Success -> {
                reduce {
                    state.copy(
                        isLoading = false,
                    )
                }
                if (state.competitionStandings?.standings.isNullOrEmpty()) {
                    reduce {
                        state.copy(
                            competitionStandings = compsFromRemote.data
                        )
                    }
                }
                if (state.currentSeason.isEmpty()) {
                    reduce {
                        state.copy(
                            currentSeason = compsFromRemote.data?.season?.startDateEndDate
                                ?: ""
                        )
                    }
                }
                getSeasons()
            }

            is Resource.Error -> {
                handleError(compsFromRemote.httpErrors ?: ErrorsTypesHttp.UnknownError())
            }
        }

    }

    private fun getSeasons() = intent {
        when (val seasons = standingsRepository.getCompetitionSeasonsById(state.compId)) {
            is Resource.Success -> {
                reduce {
                    state.copy(
                        seasons = seasons.data ?: emptyList()
                    )
                }
            }

            is Resource.Error -> {
                handleError(seasons.httpErrors ?: ErrorsTypesHttp.UnknownError())
            }
        }
    }

    private fun collectStandingsFlowFromLocal() = intent(registerIdling = false) {
        repeatOnSubscription {
            standingsRepository.getStandingsFlowFromLocalById(state.compId).collect { standings ->
                reduce {
                    state.copy(
                        competitionStandings = standings,
                        currentSeason = standings.season.startDateEndDate
                    )
                }
            }
        }
    }


    private fun handleError(error: ErrorsTypesHttp) = intent {
        reduce { state.copy(isLoading = false) }

        when (error) {
            is ErrorsTypesHttp.Https400Errors ->
                postSideEffect(
                    CompetitionStandingSideEffects.Snackbar(
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
                CompetitionStandingSideEffects.Snackbar(
                    stringResourceProvider.getString(
                        resourceId = R.string.http_500_errors,
                        arguments = arrayOf(error.errorMessage ?: "")
                    )
                )
            )

            is ErrorsTypesHttp.TimeoutException -> postSideEffect(
                CompetitionStandingSideEffects.Snackbar(
                    stringResourceProvider.getString(
                        resourceId = R.string.http_timeout_error
                    )
                )
            )

            is ErrorsTypesHttp.MissingConnection -> postSideEffect(
                CompetitionStandingSideEffects.Snackbar(
                    stringResourceProvider.getString(
                        resourceId = R.string.http_missing_error
                    )
                )
            )

            is ErrorsTypesHttp.NetworkError -> postSideEffect(
                CompetitionStandingSideEffects.Snackbar(
                    stringResourceProvider.getString(
                        resourceId = R.string.http_network_error,
                        arguments = arrayOf(error.errorMessage ?: "")
                    )
                )
            )

            else -> postSideEffect(
                CompetitionStandingSideEffects.Snackbar(
                    stringResourceProvider.getString(
                        resourceId = R.string.http_unknown_error,
                        arguments = arrayOf(error.errorMessage ?: "")
                    )
                )
            )

        }
    }
}