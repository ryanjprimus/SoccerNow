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
import ru.asmelnikov.data.local.models.CompetitionScorersEntity
import ru.asmelnikov.data.local.models.CompetitionStandingsEntity

interface StandingsRealmOptions {

    suspend fun upsertStandingsFromRemoteToLocal(standings: CompetitionStandingsEntity)

    suspend fun getStandingsFlowById(compId: String): Flow<List<CompetitionStandingsEntity>>

    suspend fun upsertScorersFromRemoteToLocal(comp: CompetitionScorersEntity)

    suspend fun getScorersFlowById(compId: String): Flow<List<CompetitionScorersEntity>>

    suspend fun upsertMatchesFromRemoteToLocal(comp: CompetitionMatchesEntity)

    suspend fun getMatchesFlowById(compId: String): Flow<List<CompetitionMatchesEntity>>

    class RealmOptionsImpl(private val realmConfig: RealmConfiguration) : StandingsRealmOptions {
        override suspend fun upsertStandingsFromRemoteToLocal(standings: CompetitionStandingsEntity) {
            withContext(Dispatchers.IO) {
                val realm = Realm.getInstance(realmConfig)
                realm.executeTransaction { transition ->
                    transition.insertOrUpdate(standings)
                }
                realm.close()
            }
        }

        override suspend fun getStandingsFlowById(compId: String): Flow<List<CompetitionStandingsEntity>> {
            return callbackFlow {
                val realm = Realm.getInstance(realmConfig)
                val competition =
                    realm.where(CompetitionStandingsEntity::class.java)
                        .equalTo("id", compId)
                        .findAll()

                val compEntity =
                    realm.copyFromRealm(competition)

                send(compEntity)

                val listener =
                    RealmChangeListener<RealmResults<CompetitionStandingsEntity>> { updatedEntity ->
                        val updateComp = realm.copyFromRealm(updatedEntity)
                        trySend(updateComp)
                    }

                competition.addChangeListener(listener)

                awaitClose {
                    competition.removeChangeListener(listener)
                    realm.close()
                }
            }.flowOn(Dispatchers.Main)
        }

        override suspend fun upsertScorersFromRemoteToLocal(comp: CompetitionScorersEntity) {
            withContext(Dispatchers.IO) {
                val realm = Realm.getInstance(realmConfig)
                realm.executeTransaction { transition ->
                    transition.insertOrUpdate(comp)
                }
                realm.close()
            }
        }

        override suspend fun getScorersFlowById(compId: String): Flow<List<CompetitionScorersEntity>> {
            return callbackFlow {
                val realm = Realm.getInstance(realmConfig)
                val scorers = realm.where(CompetitionScorersEntity::class.java)
                    .equalTo("id", compId).findAll()

                val scorersEntity = realm.copyFromRealm(scorers)
                trySend(scorersEntity)
                val listener =
                    RealmChangeListener<RealmResults<CompetitionScorersEntity>> { updatedEntity ->
                        val updateScorers = realm.copyFromRealm(updatedEntity)
                        trySend(updateScorers)
                    }
                scorers.addChangeListener(listener)
                awaitClose {
                    scorers.removeChangeListener(listener)
                    realm.close()
                }
            }.flowOn(Dispatchers.Main)
        }


        override suspend fun upsertMatchesFromRemoteToLocal(comp: CompetitionMatchesEntity) {
            withContext(Dispatchers.IO) {
                val realm = Realm.getInstance(realmConfig)
                realm.executeTransaction { transition ->
                    transition.insertOrUpdate(comp)
                }
                realm.close()
            }
        }

        override suspend fun getMatchesFlowById(compId: String): Flow<List<CompetitionMatchesEntity>> {
            return callbackFlow {
                val realm = Realm.getInstance(realmConfig)
                val matches = realm.where(CompetitionMatchesEntity::class.java)
                    .equalTo("id", compId)
                    .findAll()
                val matchEntity =
                    realm.copyFromRealm(matches)
                trySend(matchEntity)
                val listener =
                    RealmChangeListener<RealmResults<CompetitionMatchesEntity>> { updatedEntity ->
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