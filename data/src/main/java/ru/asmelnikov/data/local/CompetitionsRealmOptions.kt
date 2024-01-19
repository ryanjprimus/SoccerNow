package ru.asmelnikov.data.local

import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmConfiguration
import io.realm.RealmResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import ru.asmelnikov.data.local.models.CompetitionEntity

interface CompetitionsRealmOptions {

    suspend fun upsertCompetitionsDataFromRemoteToLocal(competitions: List<CompetitionEntity>)

    suspend fun getCompetitionsFlowFromLocal(): Flow<List<CompetitionEntity>>

    class RealmOptionsImpl(private val realmConfig: RealmConfiguration) : CompetitionsRealmOptions {

        override suspend fun upsertCompetitionsDataFromRemoteToLocal(competitions: List<CompetitionEntity>) {
            withContext(Dispatchers.IO) {
                val realm = Realm.getInstance(realmConfig)
                realm.executeTransaction { transition ->
                    transition.insertOrUpdate(competitions)
                }
                realm.close()
            }
        }

        override suspend fun getCompetitionsFlowFromLocal(): Flow<List<CompetitionEntity>> {
            return callbackFlow {
                val realm = Realm.getInstance(realmConfig)
                val competitionsList = realm.where(CompetitionEntity::class.java).findAll()
                val copiedCompetitions = realm.copyFromRealm(competitionsList)

                send(copiedCompetitions)

                val listener =
                    RealmChangeListener<RealmResults<CompetitionEntity>> { results ->
                        val updatedMessages = realm.copyFromRealm(results)
                        trySend(updatedMessages)
                    }

                competitionsList.addChangeListener(listener)

                awaitClose {
                    competitionsList.removeChangeListener(listener)
                    realm.close()
                }
            }.flowOn(Dispatchers.Main)
        }
    }
}