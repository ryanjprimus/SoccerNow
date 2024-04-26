package com.primus.soccernow

import android.app.Application
import io.realm.Realm
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import com.primus.competition_standings.di.competitionStandingsModule
import com.primus.competitions_main.di.competitionsScreenModule
import com.primus.data.di.dataModule
import com.primus.person_info.di.personModule
import com.primus.team_info.di.teamInfoModule
import com.primus.utils.di.utilsModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(
                listOf(
                    dataModule,
                    competitionsScreenModule,
                    competitionStandingsModule,
                    teamInfoModule,
                    personModule,
                    utilsModule
                )
            )
        }
    }
}