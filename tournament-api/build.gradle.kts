plugins {
    kotlin("jvm") version "1.9.23"
    alias(libs.plugins.ktor.plugin)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.betclic"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.contentNegociation)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.cors)
    implementation(libs.logback)
    implementation(libs.dynamodb)
    implementation(libs.koin.core)
    implementation(libs.koin.ktor)
    implementation(libs.koin.slf4j)

    testImplementation(libs.junit)
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.ktor.client.contentNegociation)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit5)
    testImplementation(libs.assertk)
}

tasks.test {
    useJUnitPlatform()
}