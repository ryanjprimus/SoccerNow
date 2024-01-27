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
import ru.asmelnikov.utils.getErrorMessage

class CompetitionStandingsViewModel(
    private val standingsRepository: CompetitionStandingsRepository,
    private val stringResourceProvider: StringResourceProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    ContainerHost<CompetitionStandingsState, CompetitionStandingSideEffects> {

    override val container = container<CompetitionStandingsState, CompetitionStandingSideEffects>(
        initialState = CompetitionStandingsState(),
        savedStateHandle = savedStateHandle
    ) {
        val compId = savedStateHandle.get<String>("compId")
        reduce { state.copy(compId = compId ?: "") }
        collectStandingsFlowFromLocal()
        collectScorersFlowFromLocal()
        updateStandingsFromRemoteToLocal()
        updateScorersFromRemoteToLocal()
        getSeasons()
    }

    fun onBackClick() = intent {
        postSideEffect(CompetitionStandingSideEffects.BackClick)
    }

    fun updateScorersFromRemoteToLocal(season: String? = null) = intent {
        reduce { state.copy(isLoadingScorers = true) }
        when (val compsFromRemote =
            standingsRepository.getCompetitionTopScorersBySeason(
                state.compId,
                season
            )) {
            is Resource.Success -> {
                reduce {
                    state.copy(
                        isLoadingScorers = false,
                    )
                }
                reduce {
                    state.copy(
                        scorers = compsFromRemote.data?.scorers ?: emptyList()
                    )
                }
                reduce {
                    state.copy(
                        currentSeasonScorers = compsFromRemote.data?.season?.startDateEndDate
                            ?: ""
                    )
                }
            }

            is Resource.Error -> {
                handleError(compsFromRemote.httpErrors ?: ErrorsTypesHttp.UnknownError())
            }
        }
    }

    fun updateStandingsFromRemoteToLocal(season: String? = null) = intent {
        reduce { state.copy(isLoadingStandings = true) }
        when (val compsFromRemote =
            standingsRepository.getCompetitionStandingsFromRemoteToLocalById(
                state.compId,
                season
            )) {
            is Resource.Success -> {
                reduce {
                    state.copy(
                        isLoadingStandings = false,
                    )
                }
                reduce {
                    state.copy(
                        competitionStandings = compsFromRemote.data
                    )
                }
                reduce {
                    state.copy(
                        currentSeasonStandings = compsFromRemote.data?.season?.startDateEndDate
                            ?: ""
                    )
                }
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
                        currentSeasonStandings = standings.season.startDateEndDate
                    )
                }
            }
        }
    }

    private fun collectScorersFlowFromLocal() = intent(registerIdling = false) {
        repeatOnSubscription {
            standingsRepository.getScorersFlowFromLocal(state.compId).collect { scorers ->
                reduce {
                    state.copy(
                        scorers = scorers.scorers,
                        currentSeasonScorers = scorers.season.startDateEndDate
                    )
                }
            }
        }
    }

    private fun handleError(error: ErrorsTypesHttp) = intent {
        reduce { state.copy(isLoadingStandings = false, isLoadingScorers = false) }

        postSideEffect(
            CompetitionStandingSideEffects.Snackbar(
                error.getErrorMessage(
                    stringResourceProvider
                )
            )
        )
    }
}