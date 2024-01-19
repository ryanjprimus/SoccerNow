package ru.asmelnikov.data.di

import io.realm.RealmConfiguration
import io.realm.annotations.RealmModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.asmelnikov.data.api.FootballApi
import ru.asmelnikov.data.local.CompetitionsRealmOptions
import ru.asmelnikov.data.local.StandingsRealmOptions
import ru.asmelnikov.data.local.models.AreaEntity
import ru.asmelnikov.data.local.models.CompetitionEntity
import ru.asmelnikov.data.local.models.CurrentSeasonEntity
import ru.asmelnikov.data.local.models.*
import ru.asmelnikov.data.local.models.WinnerEntity
import ru.asmelnikov.data.repository.CompetitionStandingsRepositoryImpl
import ru.asmelnikov.data.repository.CompetitionsRepositoryImpl
import ru.asmelnikov.data.retrofit_errors_handler.RetrofitErrorsHandler
import ru.asmelnikov.domain.repository.CompetitionStandingsRepository
import ru.asmelnikov.domain.repository.CompetitionsRepository

@RealmModule(library = false, classes = [CompetitionStandingsEntity::class])
data class CompetitionStandingsDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [CompetitionEmbeddedEntity::class])
data class CompetitionEmbeddedDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [FiltersEntity::class])
data class FiltersDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [SeasonEntity::class])
data class SeasonDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [StandingEntity::class])
data class StandingDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [TableEntity::class])
data class TableDbModule(val placeholder: String) {
    constructor() : this("")
}

@RealmModule(library = false, classes = [TeamEntity::class])
data class TeamDbModule(val placeholder: String) {
    constructor() : this("")
}

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
                CompetitionStandingsDbModule(),
                CompetitionEmbeddedDbModule(),
                FiltersDbModule(),
                SeasonDbModule(),
                StandingDbModule(),
                TableDbModule(),
                TeamDbModule(),
                CompetitionDbModule(),
                AreaDbModule(),
                CurrentSeasonDbModule(),
                WinnerDbModule()
            )
            .build()
    }

    factory<RetrofitErrorsHandler> { RetrofitErrorsHandler.RetrofitErrorsHandlerImpl() }

    single<CompetitionsRealmOptions> { CompetitionsRealmOptions.RealmOptionsImpl(realmConfig = get()) }

    single<StandingsRealmOptions> { StandingsRealmOptions.RealmOptionsImpl(realmConfig = get()) }

    single<OkHttpClient> { okHttp() }

    single<MoshiConverterFactory> { moshiConverterFactory() }

    single<Retrofit> { retrofit(get(), get()) }

    single<FootballApi> { get<Retrofit>().create(FootballApi::class.java) }

    single<CompetitionsRepository> {
        CompetitionsRepositoryImpl(
            footballApi = get(),
            realmOptions = get(),
            retrofitErrorsHandler = get()
        )
    }

    single<CompetitionStandingsRepository> {
        CompetitionStandingsRepositoryImpl(
            footballApi = get(),
            realmOptions = get(),
            retrofitErrorsHandler = get()
        )
    }

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
        .setLevel(HttpLoggingInterceptor.Level.BODY)
