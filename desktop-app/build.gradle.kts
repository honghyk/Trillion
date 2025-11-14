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

        buildTypes.release.proguard {
            obfuscate.set(false)
            isEnabled.set(false)
        }

        nativeDistributions {
            targetFormats(
                // MacOS
                TargetFormat.Dmg,
                // Windows
                TargetFormat.Msi,
                TargetFormat.Exe,
            )
            packageName = "Trillion"
            packageVersion = "1.0.0"

            windows {
                shortcut = true
                iconFile.set(project.file("src/jvmMain/resources/AppIcon.ico"))
            }

            macOS {
                dockName = "Trillion"
                iconFile.set(project.file("src/jvmMain/resources/AppIcon.icns"))
            }
        }
    }
}
