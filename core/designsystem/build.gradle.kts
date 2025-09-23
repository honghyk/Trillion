import org.gradle.kotlin.dsl.debugImplementation

plugins {
    alias(libs.plugins.trillion.wms.androidLibrary)
    alias(libs.plugins.trillion.wms.kotlinMultiplatform)
    alias(libs.plugins.trillion.wms.composeMultiplatform)
}

android {
    namespace = "trillion.wms.core.designsystem"
}

dependencies {
    debugImplementation(libs.androidx.compose.ui.tooling)
}
