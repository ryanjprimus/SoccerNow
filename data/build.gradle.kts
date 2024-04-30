plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("realm-android")
}

android {
    namespace = "com.primus.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

val koinVersion: String by project.extra
val retrofitVersion: String by project.extra
val okhttpVersion: String by project.extra
val moshiVersion: String by project.extra


dependencies {

    // Koin
    implementation("io.insert-koin:koin-core:$koinVersion")

    // Network
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofitVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")
    implementation("androidx.test:monitor:1.6.1")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("junit:junit:4.12")

    // Moshi
    //noinspection KaptUsageInsteadOfKsp
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.15.0")
    api("com.squareup.moshi:moshi:$moshiVersion")
    api("com.squareup.moshi:moshi-adapters:$moshiVersion")
    api("com.squareup.moshi:moshi-kotlin:$moshiVersion")

    // module
    implementation(project(":domain"))
    implementation(project(":utils"))
}