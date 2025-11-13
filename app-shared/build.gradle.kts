import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.trillion.wms.androidLibrary)
    alias(libs.plugins.trillion.wms.kotlinMultiplatform)
    alias(libs.plugins.trillion.wms.composeMultiplatform)
    alias(libs.plugins.trillion.wms.koin)
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
            implementation(projects.feature.zone.list)
            implementation(projects.feature.zone.detail)
            implementation(projects.feature.zone.form)
            implementation(projects.feature.inventory)
            implementation(projects.feature.fabricroll.detail)
            implementation(projects.feature.fabricroll.form)
            implementation(projects.feature.outbound)

            implementation(libs.navgation.compose)
            implementation(libs.kotlinx.serialization)
        }
    }

    targets.withType<KotlinNativeTarget>().configureEach {
        binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }
}

dependencies {
    debugImplementation(libs.androidx.compose.ui.tooling)
}
