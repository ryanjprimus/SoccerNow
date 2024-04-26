plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("realm-android")
}

android {
    namespace = "com.primus.soccernow"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.primus.soccernow"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

val koinVersion: String by project.extra

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class:1.1.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("androidx.core:core-splashscreen:1.0.1")

    // Shared Elements Transition
    implementation("com.mxalbert.sharedelements:shared-elements:0.1.0-SNAPSHOT")

    // Koin for Android
    implementation("io.insert-koin:koin-androidx-compose:$koinVersion")
    implementation("io.insert-koin:koin-android:$koinVersion")
    implementation("io.insert-koin:koin-core:$koinVersion")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // Module
    implementation(project(":competitions_main"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":utils"))
    implementation(project(":competition_standings"))
    implementation(project(":team_info"))
    implementation(project(":person_info"))
}