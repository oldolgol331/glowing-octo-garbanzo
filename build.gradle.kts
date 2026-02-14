plugins {
    idea
    jacoco
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.kotlin.plugin.jpa)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sonarqube)
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(libs.versions.java.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // --- [SPRING BOOT BOM] ---
    implementation(platform(libs.spring.boot.dependencies))

    // --- [SPRING ECOSYSTEM - CORE & DX] ---
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.aop)
    implementation(libs.spring.boot.starter.cache)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.kotlin.reflect)
    implementation(libs.jackson.module.kotlin)
    developmentOnly(libs.spring.boot.devtools)
    developmentOnly(libs.spring.boot.docker.compose)
    ksp(libs.spring.boot.configuration.processor)

    // --- [DATABASE & PERSISTENCE] ---
    // Starters & Frameworks
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.data.redis)
    // Querydsl
    implementation(libs.querydsl.jpa)
    ksp(libs.querydsl.apt) { artifact { classifier = "jakarta" } }
    ksp(libs.querydsl.ksp.codegen)
    // Logging & Proxy
    implementation(libs.kotlin.logging.jvm)
    implementation(libs.p6spy.spring.boot.starter)
    // Drivers
    runtimeOnly(libs.mysql.connector.j)
    testRuntimeOnly(libs.h2)
    // Redis Client
    implementation(libs.bundles.redisson)

    // --- [UTILITIES & API DOCS] ---
    implementation(libs.jackson.datatype.jsr310)
    implementation(libs.jackson.databind)
    implementation(libs.springdoc.openapi.starter.webmvc.ui)

    // --- [TESTING UTILS & FRAMEWORKS] ---
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.mockk)
    // Random Data & Property Testing
    testImplementation(libs.bundles.autoparams)
    testImplementation(libs.bundles.fixture.monkey)
    testImplementation(libs.datafaker)
    testImplementation(libs.bundles.jqwik)
    // Others
    testImplementation(libs.awaitility)
    testImplementation(libs.embedded.redis)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.jar {
    enabled = false
}

tasks.bootJar {
    archiveFileName = "app.jar"
}

jacoco {
    toolVersion = libs.versions.jacoco.get()
}

val coverageExclusions = listOf(
    ""
)

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        html.required.set(true)
        xml.required.set(true)
        csv.required.set(false)
    }

    classDirectories.setFrom(
        sourceSets.main.get().output.asFileTree.matching {
            exclude(coverageExclusions)
        }
    )
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.jacocoTestReport)

    violationRules {
        rule {
            enabled = true
            element = "CLASS"

            includes = listOf("*")
            excludes = coverageExclusions

            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "0.00".toBigDecimal()
            }

            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.00".toBigDecimal()
            }
        }
    }
}

sonar {
    properties {
        property("sonar.projectKey", "demo")
        property("sonar.host.url", "http://localhost:9000")
        property("sonar.token", "")
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/test/jacocoTestReport.xml")
        property("sonar.coverage.exclusions", coverageExclusions.joinToString(","))
    }
}
