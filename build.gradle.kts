buildscript {

    dependencies {
        classpath("io.realm:realm-gradle-plugin:10.17.0")
    }
}

allprojects {

    extra.apply {
        // Koin
        set("koinVersion", "3.5.3")
        // Retrofit
        set("retrofitVersion", "2.9.0")
        // Okhttp
        set("okhttpVersion", "4.12.0")
        // Moshi
        set("moshiVersion", "1.14.0")
        // Orbit
        set("orbitVersion", "6.1.0")
        // Coil
        set("coilVersion",  "2.5.0")
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("org.jetbrains.kotlin.jvm") version "1.8.10" apply false
    id("com.android.library") version "8.1.2" apply false
}