plugins {
    java
}

version = libs.versions.codion.get().replace("-SNAPSHOT", "")

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(23))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.isDeprecation = true
}