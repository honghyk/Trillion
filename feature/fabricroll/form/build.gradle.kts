plugins {
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.trillion.wms.feature)
}

android {
    namespace = "trillion.wms.feature.fabricroll.form"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization)
        }
    }
}

dependencies {
    debugImplementation(libs.androidx.compose.ui.tooling)
}
