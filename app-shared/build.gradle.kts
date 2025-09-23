plugins {
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.trillion.wms.androidLibrary)
    alias(libs.plugins.trillion.wms.kotlinMultiplatform)
    alias(libs.plugins.trillion.wms.composeMultiplatform)
}

android {
    namespace = "trillion.wms.app.shared"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.designsystem)
            implementation(projects.core.ui)
            implementation(projects.core.model)
            implementation(projects.feature)

            implementation(libs.navgation.compose)
            implementation(libs.kotlinx.serialization)
        }
    }
}

dependencies {
    debugImplementation(libs.androidx.compose.ui.tooling)
}
