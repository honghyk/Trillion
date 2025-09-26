plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("kotlinMultiplatform") {
            id = "trillion.wms.kotlinMultiplatform"
            implementationClass = "KotlinMultiplatformConventionPlugin"
        }
        register("androidApplication") {
            id = "trillion.wms.androidApplication"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "trillion.wms.androidLibrary"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("koin") {
            id = "trillion.wms.koin"
            implementationClass = "KoinConventionPlugin"
        }
        register("composeMultiplatform") {
            id = "trillion.wms.composeMultiplatform"
            implementationClass = "ComposeMultiplatformConventionPlugin"
        }
        register("feature") {
            id = "trillion.wms.feature"
            implementationClass = "FeatureConventionPlugin"
        }
    }
}
