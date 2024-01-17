package ru.asmelnikov.data.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.asmelnikov.data.api.FootballApi
import ru.asmelnikov.data.repository.FootballRepositoryImpl
import ru.asmelnikov.domain.FootballRepository

private const val FOOTBALL_API_URL = "https://api.football-data.org/v4/"


val dataModule = module {

    single { okHttp() }

    single { moshiConverterFactory() }

    single { retrofit(get(), get()) }

    single { get<Retrofit>().create(FootballApi::class.java) }

    single<FootballRepository> { FootballRepositoryImpl(footballApi = get()) }

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
