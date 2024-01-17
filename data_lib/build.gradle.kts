import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("kotlin-kapt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

val koinVersion: String by project.extra
val retrofitVersion: String by project.extra
val okhttpVersion: String by project.extra
val moshiVersion: String by project.extra


dependencies {

    // module
    implementation(project(":domain"))
    implementation(project(":utils"))

    // koin
    implementation("io.insert-koin:koin-core:$koinVersion")

    // network
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofitVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")

    // Moshi
    api("com.squareup.moshi:moshi:$moshiVersion")
    api("com.squareup.moshi:moshi-adapters:$moshiVersion")
    api("com.squareup.moshi:moshi-kotlin:$moshiVersion")

}