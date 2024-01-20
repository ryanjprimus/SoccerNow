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
import ru.asmelnikov.data.local.models.CompetitionStandingsEntity


interface StandingsRealmOptions {

    suspend fun upsertStandingsFromRemoteToLocal(standings: CompetitionStandingsEntity)

    suspend fun getStandingsFlowById(compId: String): Flow<CompetitionStandingsEntity>

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

        override suspend fun getStandingsFlowById(compId: String): Flow<CompetitionStandingsEntity> {
            return callbackFlow {
                val realm = Realm.getInstance(realmConfig)
                val competition: CompetitionStandingsEntity? =
                    realm.where(CompetitionStandingsEntity::class.java)
                        .equalTo("id", compId)
                        .findFirst()

                if (competition != null) {
                    val compEntity: CompetitionStandingsEntity =
                        realm.copyFromRealm(competition)

                    trySend(compEntity)

                    val listener =
                        RealmObjectChangeListener<CompetitionStandingsEntity> { updatedEntity, _ ->
                            val updateComp = realm.copyFromRealm(updatedEntity)
                            trySend(updateComp)
                        }

                    competition.addChangeListener(listener)

                    awaitClose {
                        competition.removeChangeListener(listener)
                        realm.close()
                    }
                } else {
                    send(CompetitionStandingsEntity())
                    close()
                    realm.close()
                }
            }.flowOn(Dispatchers.Main)
        }
    }
}