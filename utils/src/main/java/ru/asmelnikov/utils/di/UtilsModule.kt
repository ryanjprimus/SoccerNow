package ru.asmelnikov.utils.di

import org.koin.dsl.module
import ru.asmelnikov.utils.StringResourceProvider

val utilsModule = module {

    single<StringResourceProvider> { StringResourceProvider.StringResourceProviderImpl(context = get()) }

}