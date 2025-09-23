plugins {
    alias(libs.plugins.trillion.wms.kotlinMultiplatform)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.datetime)
            }
        }
    }
}
