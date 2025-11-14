import trillion.wms.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("org.jetbrains.kotlin.multiplatform")
        }

        extensions.configure<KotlinMultiplatformExtension> {
            jvmToolchain(17)

            if (hasAndroidPlugin()) {
                androidTarget()
            }

            jvm()

            iosArm64()
            iosSimulatorArm64()

            applyDefaultHierarchyTemplate()

            compilerOptions {
                optIn.addAll(
                    "kotlin.RequiresOptIn",
                    "kotlin.time.ExperimentalTime",
                    "kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "kotlinx.coroutines.FlowPreview",
                )
                freeCompilerArgs.addAll(
                    "-Xexpect-actual-classes",
                )
            }

            sourceSets {
                commonMain.dependencies {
                    implementation(libs.findLibrary("kermit").get())
                }
            }
        }
    }
}

private fun Project.hasAndroidPlugin(): Boolean =
    pluginManager.hasPlugin("com.android.library") || pluginManager.hasPlugin("com.android.application")
