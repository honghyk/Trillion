rootProject.name = "TrillionWMS"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("gradle/build-logic")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(
    ":app-shared",
    ":android-app",
    ":desktop-app",
)

include(
    ":feature:zone:list",
    ":feature:zone:detail",
    ":feature:zone:form",
    ":feature:inventory",
    ":feature:fabricroll:detail",
    ":feature:fabricroll:form",
    ":feature:outbound",
)

include(
    ":core:database",
    ":core:model",
    ":core:data",
    ":core:domain",
    ":core:designsystem",
    ":core:network",
    ":core:ui",
)
