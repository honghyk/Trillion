plugins {
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.trillion.wms.androidLibrary)
    alias(libs.plugins.trillion.wms.kotlinMultiplatform)
    alias(libs.plugins.trillion.wms.composeMultiplatform)
    alias(libs.plugins.trillion.wms.koin)
    id("dev.mokkery") version "2.9.0"
}

android {
    namespace = "trillion.wms.feature"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.model)
            implementation(projects.core.domain)
            implementation(projects.core.designsystem)
            implementation(projects.core.ui)


            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.koin.compose.viewmodel)
            implementation(libs.navgation.compose)
            implementation(libs.kotlinx.serialization)
            implementation(libs.kotlinx.datetime)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlin.coroutines.test)
            implementation(libs.turbine)
        }
    }
}

dependencies {
    debugImplementation(libs.androidx.compose.ui.tooling)
}
