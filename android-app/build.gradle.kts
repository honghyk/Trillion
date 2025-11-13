plugins {
    alias(libs.plugins.trillion.wms.androidApplication)
    alias(libs.plugins.trillion.wms.kotlinMultiplatform)
    alias(libs.plugins.trillion.wms.composeMultiplatform)
    alias(libs.plugins.trillion.wms.koin)
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }

        commonMain.dependencies {
            implementation(projects.appShared)
            implementation(projects.core.designsystem)
            implementation(projects.core.model)

            implementation(libs.navgation.compose)
        }
    }
}

android {
    namespace = "trillion.wms"

    defaultConfig {
        applicationId = "trillion.wms"
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    debugImplementation(libs.androidx.compose.ui.tooling)
}
