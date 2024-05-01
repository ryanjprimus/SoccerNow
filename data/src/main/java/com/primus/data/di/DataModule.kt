package com.primus.data.di

import io.realm.RealmConfiguration
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.primus.data.api.FootballApi
import com.primus.data.api.NewsApi
import com.primus.data.local.CompetitionsRealmOptions
import com.primus.data.local.StandingsRealmOptions
import com.primus.data.local.TeamInfoRealmOptions
import com.primus.data.repository.CompetitionStandingsRepositoryImpl
import com.primus.data.repository.CompetitionsRepositoryImpl
import com.primus.data.repository.NewsRepositoryImpl
import com.primus.data.repository.PersonRepositoryImpl
import com.primus.data.repository.TeamInfoRepositoryImpl
import com.primus.data.retrofit_errors_handler.RetrofitErrorsHandler
import com.primus.domain.repository.CompetitionStandingsRepository
import com.primus.domain.repository.CompetitionsRepository
import com.primus.domain.repository.NewsRepository
import com.primus.domain.repository.PersonRepository
import com.primus.domain.repository.TeamInfoRepository

private const val FOOTBALL_API_URL = "https://api.football-data.org/v4/"

private const val NEWS_API_URL = "https://newsapi.org/v2/"

val dataModule = module {

    single<RealmConfiguration> {
        RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .name("soccer_now.realm")
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

    single<Retrofit>(named(FOOTBALL_API_URL)) { retrofit(get(), get(), FOOTBALL_API_URL) }

    single<Retrofit>(named(NEWS_API_URL)) { retrofit(get(), get(), NEWS_API_URL) }

    single<FootballApi> { get<Retrofit>(named(FOOTBALL_API_URL)).create(FootballApi::class.java) }

    single<NewsApi> { get<Retrofit>(named(NEWS_API_URL)).create(NewsApi::class.java) }

    single<NewsRepository> { NewsRepositoryImpl(newsApi = get(), retrofitErrorsHandler = get()) }

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

    single<PersonRepository> {
        PersonRepositoryImpl(
            footballApi = get(),
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
    url: String
) = Retrofit.Builder()
    .baseUrl(url)
    .addConverterFactory(moshiConverterFactory)
    .client(okHttpClient)
    .build()

private fun loggingInterceptor() =
    HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)
