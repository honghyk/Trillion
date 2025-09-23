import trillion.wms.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets {
                    targets.findByName("android")?.let {
                        androidMain {
                            dependencies {
                                implementation(libs.findLibrary("koin.android").get())
                            }
                        }
                    }

                    commonMain {
                        dependencies {
                            val bom = libs.findLibrary("koin.bom").get()
                            implementation(project.dependencies.platform(bom))
                            implementation(libs.findLibrary("koin.core").get())
                        }
                    }
                }
            }
        }
    }
}
