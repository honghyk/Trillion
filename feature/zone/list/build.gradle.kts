plugins {
    alias(libs.plugins.trillion.wms.feature)
}

android {
    namespace = "trillion.wms.feature.zone.list"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.datetime)
        }
    }
}

dependencies {
    debugImplementation(libs.androidx.compose.ui.tooling)
}
