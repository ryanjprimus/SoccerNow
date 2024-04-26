package com.primus.competition_standings.view_model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import com.primus.domain.models.Head2head
import com.primus.domain.repository.CompetitionStandingsRepository
import com.primus.utils.ErrorsTypesHttp
import com.primus.utils.Resource
import com.primus.utils.StringResourceProvider
import com.primus.utils.getErrorMessage

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
        collectMatchesFlowFromLocal()
        updateStandingsFromRemoteToLocal()
        updateScorersFromRemoteToLocal()
        updateMatchesFromRemoteToLocal()
        getSeasons()
    }

    fun matchItemClick(itemId: Int) = intent {
        if (state.expandedItem == itemId) {
            reduce { state.copy(expandedItem = -1) }
            return@intent
        }
        if (state.head2head.id == itemId) {
            reduce { state.copy(expandedItem = itemId) }
            return@intent
        }
        reduce { state.copy(expandedItem = itemId, isHead2headLoading = true) }
        when (val head2head =
            standingsRepository.getHead2headById(
                itemId
            )) {
            is Resource.Success -> {
                reduce {
                    state.copy(
                        head2head = head2head.data ?: Head2head(),
                        isHead2headLoading = false,
                    )
                }
            }

            is Resource.Error -> {
                handleError(head2head.httpErrors ?: ErrorsTypesHttp.UnknownError())
            }
        }
    }

    fun onPersonClick(personId: Int) = intent {
        postSideEffect(CompetitionStandingSideEffects.OnPersonInfoNavigate(personId = personId.toString()))
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
                        isLoadingScorers = false
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
                        isLoadingStandings = false
                    )
                }
            }

            is Resource.Error -> {
                handleError(compsFromRemote.httpErrors ?: ErrorsTypesHttp.UnknownError())
            }
        }
    }

    fun updateMatchesFromRemoteToLocal(season: String? = null) = intent {
        reduce { state.copy(isLoadingMatches = true) }
        when (val matchesFromRemote =
            standingsRepository.getAllMatchesFromRemoteToLocal(
                state.compId,
                season
            )) {
            is Resource.Success -> {
                reduce {
                    state.copy(
                        isLoadingMatches = false
                    )
                }
            }

            is Resource.Error -> {
                handleError(matchesFromRemote.httpErrors ?: ErrorsTypesHttp.UnknownError())
            }
        }
    }

    fun onTeamClick(teamId: Int) = intent {
        postSideEffect(CompetitionStandingSideEffects.OnTeamInfoNavigate(teamId = teamId.toString()))
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
                        currentSeasonStandings = standings?.season?.startDateEndDate ?: ""
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
                        scorers = scorers?.scorers ?: emptyList(),
                        currentSeasonScorers = scorers?.season?.startDateEndDate ?: ""
                    )
                }
            }
        }
    }

    private fun collectMatchesFlowFromLocal() = intent(registerIdling = false) {
        repeatOnSubscription {
            standingsRepository.getAllMatchesFlowFromLocal(state.compId).collect { matches ->
                reduce {
                    state.copy(
                        matchesCompleted = matches?.matchesByTourCompleted ?: emptyList(),
                        matchesAhead = matches?.matchesByTourAhead ?: emptyList(),
                        currentSeasonMatches = matches?.season ?: ""
                    )
                }
            }
        }
    }

    private fun handleError(error: ErrorsTypesHttp) = intent {
        reduce {
            state.copy(
                isLoadingStandings = false,
                isLoadingScorers = false,
                isLoadingMatches = false,
                isHead2headLoading = false
            )
        }

        postSideEffect(
            CompetitionStandingSideEffects.Snackbar(
                error.getErrorMessage(
                    stringResourceProvider
                )
            )
        )
    }
}