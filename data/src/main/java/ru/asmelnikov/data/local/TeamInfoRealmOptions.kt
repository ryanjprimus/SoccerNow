package ru.asmelnikov.data.local

import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmObjectChangeListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import ru.asmelnikov.data.local.models.CompetitionMatchesEntity
import ru.asmelnikov.data.local.models.TeamInfoEntity
import ru.asmelnikov.data.local.models.TeamMatchesEntity

interface TeamInfoRealmOptions {

    suspend fun upsertTeamInfoFromRemoteToLocal(teamInfo: TeamInfoEntity)

    suspend fun getTeamInfoFlowById(teamId: String): Flow<TeamInfoEntity>

    suspend fun upsertMatchesFromRemoteToLocal(matches: TeamMatchesEntity)

    suspend fun getMatchesFlowById(teamId: String): Flow<TeamMatchesEntity>

    class RealmOptionsImpl(private val realmConfig: RealmConfiguration) : TeamInfoRealmOptions {
        override suspend fun upsertTeamInfoFromRemoteToLocal(teamInfo: TeamInfoEntity) {
            withContext(Dispatchers.IO) {
                val realm = Realm.getInstance(realmConfig)
                realm.executeTransaction { transition ->
                    transition.insertOrUpdate(teamInfo)
                }
                realm.close()
            }
        }

        override suspend fun getTeamInfoFlowById(teamId: String): Flow<TeamInfoEntity> {
            return callbackFlow {
                val realm = Realm.getInstance(realmConfig)
                val teamInfo: TeamInfoEntity? =
                    realm.where(TeamInfoEntity::class.java)
                        .equalTo("id", teamId)
                        .findFirst()

                if (teamInfo != null) {
                    val teamEntity: TeamInfoEntity =
                        realm.copyFromRealm(teamInfo)

                    trySend(teamEntity)

                    val listener =
                        RealmObjectChangeListener<TeamInfoEntity> { updatedEntity, _ ->
                            val updateTeam = realm.copyFromRealm(updatedEntity)
                            trySend(updateTeam)
                        }

                    teamInfo.addChangeListener(listener)

                    awaitClose {
                        teamInfo.removeChangeListener(listener)
                        realm.close()
                    }
                } else {
                    send(TeamInfoEntity())
                    close()
                    realm.close()
                }
            }.flowOn(Dispatchers.Main)
        }

        override suspend fun upsertMatchesFromRemoteToLocal(matches: TeamMatchesEntity) {
            withContext(Dispatchers.IO) {
                val realm = Realm.getInstance(realmConfig)
                realm.executeTransaction { transition ->
                    transition.insertOrUpdate(matches)
                }
                realm.close()
            }
        }

        override suspend fun getMatchesFlowById(teamId: String): Flow<TeamMatchesEntity> {
            return callbackFlow {
                val realm = Realm.getInstance(realmConfig)
                val matches: TeamMatchesEntity? =
                    realm.where(TeamMatchesEntity::class.java)
                        .equalTo("id", teamId)
                        .findFirst()

                if (matches != null) {
                    val matchEntity: TeamMatchesEntity =
                        realm.copyFromRealm(matches)

                    trySend(matchEntity)

                    val listener =
                        RealmObjectChangeListener<TeamMatchesEntity> { updatedEntity, _ ->
                            val updateMatch = realm.copyFromRealm(updatedEntity)
                            trySend(updateMatch)
                        }

                    matches.addChangeListener(listener)

                    awaitClose {
                        matches.removeChangeListener(listener)
                        realm.close()
                    }
                } else {
                    send(TeamMatchesEntity())
                    close()
                    realm.close()
                }
            }.flowOn(Dispatchers.Main)
        }
    }
}