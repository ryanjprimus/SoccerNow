pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
    }
}

rootProject.name = "GoalPulse"
include(":app")
include(":data")
include(":domain")
include(":utils")
include(":competitions_main")
include(":competition_standings")
