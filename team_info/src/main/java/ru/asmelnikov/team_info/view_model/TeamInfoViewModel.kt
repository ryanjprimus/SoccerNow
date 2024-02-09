package ru.asmelnikov.team_info.view_model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import ru.asmelnikov.domain.models.TeamInfo
import ru.asmelnikov.domain.repository.TeamInfoRepository
import ru.asmelnikov.utils.ErrorsTypesHttp
import ru.asmelnikov.utils.Resource
import ru.asmelnikov.utils.StringResourceProvider
import ru.asmelnikov.utils.getErrorMessage

class TeamInfoViewModel(
    private val teamRepository: TeamInfoRepository,
    private val stringResourceProvider: StringResourceProvider,
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
    }

    fun getTeamInfoFromRemoteToLocal() = intent {
        reduce { state.copy(isLoading = true) }
        when (val team =
            teamRepository.getTeamInfoById(
                state.teamId
            )) {
            is Resource.Success -> {
                reduce {
                    state.copy(
                        isLoading = false,
                        teamInfo = team.data ?: TeamInfo()
                    )
                }
            }

            is Resource.Error -> {
                handleError(team.httpErrors ?: ErrorsTypesHttp.UnknownError())
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

    private fun handleError(error: ErrorsTypesHttp) = intent {
        reduce {
            state.copy(
                isLoading = false
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