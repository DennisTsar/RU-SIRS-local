@Suppress("DSL_SCOPE_VIOLATION") // just to avoid "libs" red underline
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    application
}

group = "dev.letter"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.coroutines)
    implementation(libs.serialization)
    implementation(libs.bundles.ktor.local)
    implementation(project(":common"))
}

application {
    mainClass.set("general.MainKt")
}