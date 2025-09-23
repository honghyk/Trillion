import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.trillion.wms.androidLibrary)
    alias(libs.plugins.trillion.wms.kotlinMultiplatform)
    alias(libs.plugins.trillion.wms.koin)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.buildConfig)
}

buildConfig {
    packageName("trillion.wms.core.network")

    val apiKey = gradleLocalProperties(rootDir, providers).getProperty("supabase_api_key") ?: ""
    val supabaseUrl = gradleLocalProperties(rootDir, providers).getProperty("supabase_url") ?: ""

    buildConfigField<String>("SUPABASE_API_KEY", apiKey)
    buildConfigField<String>("SUPABASE_URL", supabaseUrl)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.model)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)

            implementation(libs.kotlinx.serialization)
            api(libs.postgrest.kt)
        }

        jvmMain.dependencies {
            implementation(libs.ktor.client.cio)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.cio)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "trillion.wms.core.network"
}
