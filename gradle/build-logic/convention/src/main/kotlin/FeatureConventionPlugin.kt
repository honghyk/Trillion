import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import trillion.wms.convention.libs

class FeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("trillion.wms.androidLibrary")
            apply("trillion.wms.kotlinMultiplatform")
            apply("trillion.wms.composeMultiplatform")
            apply("trillion.wms.koin")
        }

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets {
                commonMain.dependencies {
                    implementation(project(":core:model"))
                    implementation(project(":core:domain"))
                    implementation(project(":core:designsystem"))
                    implementation(project(":core:ui"))

                    implementation(libs.findLibrary("androidx.lifecycle.viewmodelCompose").get())
                    implementation(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                    implementation(libs.findLibrary("koin.compose.viewmodel").get())
                }

                commonTest.dependencies {
                    implementation(libs.findLibrary("kotlin.test").get())
                    implementation(libs.findLibrary("kotlin.coroutines.test").get())
                    implementation(libs.findLibrary("turbine").get())
                }
            }
        }
    }
}
