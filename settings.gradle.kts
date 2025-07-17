pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        gradlePluginPortal()
        google()
        jcenter()
        maven(url = "https://jitpack.io")
        mavenCentral()
        maven ( url = "https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea" )
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        gradlePluginPortal()
        google()
        jcenter()
        maven(url = "https://jitpack.io")
        mavenCentral()
        maven ( url = "https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea" )
    }
}

rootProject.name = "PregnancyTrackerIgnite"
include(":app")
include(":sonictimer")
