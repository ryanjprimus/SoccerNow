package ru.asmelnikov.data.local

import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmConfiguration
import io.realm.RealmObjectChangeListener
import io.realm.RealmResults
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

    suspend fun getTeamInfoFlowById(teamId: String): Flow<List<TeamInfoEntity>>

    suspend fun upsertMatchesFromRemoteToLocal(matches: TeamMatchesEntity)

    suspend fun getMatchesFlowById(teamId: String): Flow<List<TeamMatchesEntity>>

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

        override suspend fun getTeamInfoFlowById(teamId: String): Flow<List<TeamInfoEntity>> {
            return callbackFlow {
                val realm = Realm.getInstance(realmConfig)
                val teamInfo =
                    realm.where(TeamInfoEntity::class.java)
                        .equalTo("id", teamId)
                        .findAll()
                val teamEntity =
                    realm.copyFromRealm(teamInfo)
                trySend(teamEntity)
                val listener =
                    RealmChangeListener<RealmResults<TeamInfoEntity>> { updatedEntity ->
                        val updateTeam = realm.copyFromRealm(updatedEntity)
                        trySend(updateTeam)
                    }
                teamInfo.addChangeListener(listener)
                awaitClose {
                    teamInfo.removeChangeListener(listener)
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

        override suspend fun getMatchesFlowById(teamId: String): Flow<List<TeamMatchesEntity>> {
            return callbackFlow {
                val realm = Realm.getInstance(realmConfig)
                val matches =
                    realm.where(TeamMatchesEntity::class.java)
                        .equalTo("id", teamId)
                        .findAll()
                val matchEntity =
                    realm.copyFromRealm(matches)
                trySend(matchEntity)
                val listener =
                    RealmChangeListener<RealmResults<TeamMatchesEntity>> { updatedEntity ->
                        val updateMatch = realm.copyFromRealm(updatedEntity)
                        trySend(updateMatch)
                    }
                matches.addChangeListener(listener)
                awaitClose {
                    matches.removeChangeListener(listener)
                    realm.close()
                }
            }.flowOn(Dispatchers.Main)
        }
    }
}
