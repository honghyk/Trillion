plugins {
    alias(libs.plugins.trillion.wms.feature)
}

android {
    namespace = "trillion.wms.feature.inventory"
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
