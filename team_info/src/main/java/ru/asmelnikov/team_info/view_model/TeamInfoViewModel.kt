package ru.asmelnikov.team_info.view_model

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.TeamInfo
import ru.asmelnikov.domain.repository.CompetitionStandingsRepository
import ru.asmelnikov.domain.repository.TeamInfoRepository
import ru.asmelnikov.utils.ErrorsTypesHttp
import ru.asmelnikov.utils.Resource
import ru.asmelnikov.utils.StringResourceProvider
import ru.asmelnikov.utils.getErrorMessage

class TeamInfoViewModel(
    private val teamRepository: TeamInfoRepository,
    private val stringResourceProvider: StringResourceProvider,
    private val standingsRepository: CompetitionStandingsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    ContainerHost<TeamInfoState, TeamInfoSideEffects> {

    override val container = container<TeamInfoState, TeamInfoSideEffects>(
        initialState = TeamInfoState(),
        savedStateHandle = savedStateHandle
    ) {
        val teamId = savedStateHandle.get<String>("teamId")
        reduce { state.copy(teamId = teamId ?: "") }
        collectTeamInfoFlowFromLocal()
        getTeamInfoFromRemoteToLocal()
        collectTeamMatchesFlowFromLocal()
        getTeamMatchesFromRemoteToLocal()
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

    fun setColorPalette(colors: Map<String, String>) = intent {
        reduce { state.copy(colorPalette = colors) }
    }

    fun getTeamInfoFromRemoteToLocal() = intent {
        reduce { state.copy(isInfoLoading = true) }
        when (val team =
            teamRepository.getTeamInfoById(
                state.teamId
            )) {
            is Resource.Success -> {
                reduce {
                    state.copy(
                        isInfoLoading = false,
                        teamInfo = team.data ?: TeamInfo()
                    )
                }
            }

            is Resource.Error -> {
                handleError(team.httpErrors ?: ErrorsTypesHttp.UnknownError())
            }
        }
    }

    fun getTeamMatchesFromRemoteToLocal() = intent {
        reduce { state.copy(isMatchesLoading = true) }
        when (val matches =
            teamRepository.getTeamMatchesFromRemoteToLocal(
                teamId = state.teamId
            )) {
            is Resource.Success -> {
                reduce {
                    state.copy(
                        isMatchesLoading = false,
                        matchesComplete = matches.data?.matchesCompleted ?: emptyList(),
                        matchesAhead = matches.data?.matchesAhead ?: emptyList()
                    )
                }
            }

            is Resource.Error -> {
                handleError(matches.httpErrors ?: ErrorsTypesHttp.UnknownError())
            }
        }
    }

    fun onBackClick() = intent {
        postSideEffect(TeamInfoSideEffects.BackClick)
    }

    private fun collectTeamInfoFlowFromLocal() = intent(registerIdling = false) {
        repeatOnSubscription {
            teamRepository.getTeamInfoByIdFlowFromLocal(state.teamId).collect { teamInfo ->
                reduce {
                    state.copy(
                        teamInfo = teamInfo
                    )
                }
            }
        }
    }

    private fun collectTeamMatchesFlowFromLocal() = intent(registerIdling = false) {
        repeatOnSubscription {
            teamRepository.getTeamMatchesFlowFromLocal(state.teamId).collect { matches ->
                reduce {
                    state.copy(
                        matchesComplete = matches.matchesCompleted,
                        matchesAhead = matches.matchesAhead
                    )
                }
            }
        }
    }

    private fun handleError(error: ErrorsTypesHttp) = intent {
        reduce {
            state.copy(
                isInfoLoading = false,
                isMatchesLoading = false,
                isHead2headLoading = false
            )
        }

        postSideEffect(
            TeamInfoSideEffects.Snackbar(
                error.getErrorMessage(
                    stringResourceProvider
                )
            )
        )
    }

}