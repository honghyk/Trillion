import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.trillion.wms.androidApplication)
    alias(libs.plugins.trillion.wms.kotlinMultiplatform)
    alias(libs.plugins.trillion.wms.composeMultiplatform)
    alias(libs.plugins.trillion.wms.koin)
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.appShared)
            implementation(projects.core.designsystem)
            implementation(projects.core.model)

            implementation(libs.navgation.compose)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }

    targets.withType<KotlinNativeTarget>().configureEach {
        binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
}

android {
    namespace = "trillion.wms.app.shared"
}

compose.desktop {
    application {
        mainClass = "trillion.wms.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "trillion.wms"
            packageVersion = "1.0.0"

            windows {
                shortcut = true
            }

            macOS {
                dockName = "Trillion"
            }
        }
    }
}

dependencies {
    debugImplementation(libs.androidx.compose.ui.tooling)
}
