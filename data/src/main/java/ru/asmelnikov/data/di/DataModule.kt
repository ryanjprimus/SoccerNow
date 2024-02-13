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
import ru.asmelnikov.data.local.TeamInfoRealmOptions
import ru.asmelnikov.data.local.models.AreaEntity
import ru.asmelnikov.data.local.models.CompetitionEntity
import ru.asmelnikov.data.local.models.CurrentSeasonEntity
import ru.asmelnikov.data.local.models.*
import ru.asmelnikov.data.local.models.WinnerEntity
import ru.asmelnikov.data.repository.CompetitionStandingsRepositoryImpl
import ru.asmelnikov.data.repository.CompetitionsRepositoryImpl
import ru.asmelnikov.data.repository.TeamInfoRepositoryImpl
import ru.asmelnikov.data.retrofit_errors_handler.RetrofitErrorsHandler
import ru.asmelnikov.domain.repository.CompetitionStandingsRepository
import ru.asmelnikov.domain.repository.CompetitionsRepository
import ru.asmelnikov.domain.repository.TeamInfoRepository

private const val FOOTBALL_API_URL = "https://api.football-data.org/v4/"

val dataModule = module {

    single<RealmConfiguration> {
        RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .name("goal_pulse.realm")
            .schemaVersion(1L)
            .modules(
                CompetitionScorersDbModule(),
                ScorerDbModule(),
                PlayerDbModule(),
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
                WinnerDbModule(),
                CompetitionMatchesDbModule(),
                MatchDbModule(),
                AwayTeamDbModule(),
                HomeTeamDbModule(),
                RefereeDbModule(),
                ScoreDbModule(),
                FullTimeDbModule(),
                HalfTimeDbModule(),
                MatchesByTourDbModule(),
                TeamInfoDbModule(),
                CoachDbModule(),
                SquadByPositionDbModule(),
                SquadDbModule(),
                ContractDbModule(),
                TeamMatchesDbModule()
            )
            .build()
    }

    factory<RetrofitErrorsHandler> { RetrofitErrorsHandler.RetrofitErrorsHandlerImpl() }

    single<CompetitionsRealmOptions> { CompetitionsRealmOptions.RealmOptionsImpl(realmConfig = get()) }

    single<StandingsRealmOptions> { StandingsRealmOptions.RealmOptionsImpl(realmConfig = get()) }

    single<TeamInfoRealmOptions> { TeamInfoRealmOptions.RealmOptionsImpl(realmConfig = get()) }

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

    single<TeamInfoRepository> {
        TeamInfoRepositoryImpl(
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
