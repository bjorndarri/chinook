plugins {
    id("groovy-gradle-plugin")
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation(libs.jlink)
    implementation(libs.extra.module.info)
    implementation(libs.spotless)
}