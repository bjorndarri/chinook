plugins {
    `java-library`
    id("chinook.jasperreports.modules")
    id("chinook.spotless.plugin")
    id("io.github.f-cramer.jasperreports") version "0.0.3"
}

dependencies {
    api(project(":chinook-domain-api"))

    implementation(libs.codion.common.rmi)
    implementation(libs.codion.framework.i18n)
    implementation(libs.codion.framework.db.local)

    compileOnly(libs.jasperreports.jdt) {
        exclude(group = "net.sf.jasperreports")
    }

    testImplementation(libs.codion.framework.domain.test)
    testRuntimeOnly(libs.codion.dbms.h2)
    testRuntimeOnly(libs.h2)
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
}

extraJavaModuleInfo {
    automaticModule("org.eclipse.jdt:ecj", "org.eclipse.jdt.ecj")
}

tasks.test {
    useJUnitPlatform()
    systemProperty("codion.test.user", "scott:tiger")
    systemProperty("codion.db.url", "jdbc:h2:mem:h2db")
    systemProperty("codion.db.initScripts", "classpath:create_schema.sql")
}

jasperreports {
    classpath.from(project.sourceSets.main.get().compileClasspath)
}

sourceSets.main {
    resources.srcDir(tasks.compileAllReports)
}