package com.primus.utils.di

import org.koin.dsl.module
import com.primus.utils.StringResourceProvider
import com.primus.utils.color_gen.ColorGenerator

val utilsModule = module {

    single<StringResourceProvider> { StringResourceProvider.StringResourceProviderImpl(context = get()) }

    single<ColorGenerator> { ColorGenerator.ColorGeneratorImpl(context = get()) }

}