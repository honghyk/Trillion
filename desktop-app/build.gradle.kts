import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.trillion.wms.kotlinMultiplatform)
    alias(libs.plugins.trillion.wms.composeMultiplatform)
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
