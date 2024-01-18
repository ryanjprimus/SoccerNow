package ru.asmelnikov.utils

import org.koin.dsl.module

val utilsModule = module {

    single<StringResourceProvider> { StringResourceProvider.StringResourceProviderImpl(context = get()) }

}