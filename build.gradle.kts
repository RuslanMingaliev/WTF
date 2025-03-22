import io.gitlab.arturbosch.detekt.Detekt

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false

    alias(libs.plugins.detekt)
    alias(libs.plugins.composeHotReload) apply false
}

dependencies {
    add("detektPlugins", libs.detektFormatting)
}

subprojects {
    tasks.register("detekt", Detekt::class.java) {
        description = "Run detekt on subproject"
        source(
            files(
                project.layout.projectDirectory.dir("src"),
                project.layout.projectDirectory.dir("build.gradle.kts")
            )
        )
    }
}

tasks.register<Detekt>("detektAll") {
    description = "Runs over whole code base without the starting overhead for each module."
    parallel = true
    autoCorrect = false
    setSource(files(projectDir))

    config.setFrom(files(project.rootDir.resolve("config/detekt/detekt.yml")))
    buildUponDefaultConfig = false

    include("**/*.kt")
    include("**/*.kts")
    exclude("**/resources/**")
    exclude("**/build/**")
    reports {
        xml.required.set(false)
        html.required.set(false)
    }
}
