package ru.asmelnikov.data.di

import io.realm.RealmConfiguration
import io.realm.annotations.RealmModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.asmelnikov.data.api.FootballApi
import ru.asmelnikov.data.local.RealmOptions
import ru.asmelnikov.data.local.models.AreaEntity
import ru.asmelnikov.data.local.models.CompetitionEntity
import ru.asmelnikov.data.local.models.CurrentSeasonEntity
import ru.asmelnikov.data.local.models.WinnerEntity
import ru.asmelnikov.data.repository.FootballRepositoryImpl
import ru.asmelnikov.domain.repository.FootballRepository

@RealmModule(library = false, classes = [CompetitionEntity::class])
data class CompetitionDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [AreaEntity::class])
data class AreaDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [CurrentSeasonEntity::class])
data class CurrentSeasonDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [WinnerEntity::class])
data class WinnerDbModule(val placeholder: String) {
    constructor() : this("")
}

private const val FOOTBALL_API_URL = "https://api.football-data.org/v4/"


val dataModule = module {

    single<RealmConfiguration> {
        RealmConfiguration.Builder()
            .name("goal_pulse.realm")
            .schemaVersion(1L)
            .modules(
                CompetitionDbModule(),
                AreaDbModule(),
                CurrentSeasonDbModule(),
                WinnerDbModule()
            )
            .build()
    }

    single<RealmOptions> { RealmOptions.RealmOptionsImpl(realmConfig = get()) }

    single<OkHttpClient> { okHttp() }

    single<MoshiConverterFactory> { moshiConverterFactory() }

    single<Retrofit> { retrofit(get(), get()) }

    single<FootballApi> { get<Retrofit>().create(FootballApi::class.java) }

    single<ru.asmelnikov.domain.repository.FootballRepository> { FootballRepositoryImpl(footballApi = get(), realmOptions = get()) }

}

private fun moshiConverterFactory(): MoshiConverterFactory =
    MoshiConverterFactory.create()

private fun okHttp(): OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor())
        .build()

private fun retrofit(
    moshiConverterFactory: MoshiConverterFactory,
    okHttpClient: OkHttpClient,
) = Retrofit.Builder()
    .baseUrl(FOOTBALL_API_URL)
    .addConverterFactory(moshiConverterFactory)
    .client(okHttpClient)
    .build()

private fun loggingInterceptor() =
    HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BASIC)
