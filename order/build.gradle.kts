
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
    java
    id("com.bmuschko.docker-spring-boot-application") version "6.7.0"
}

group = "com.galaxy"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_18

repositories {
    mavenCentral()
}

configurations {
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
}
dependencies {
    api(project(":foundation"))
    api(project(":schema-registry"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "18"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

docker {
    springBootApplication {
        baseImage.set("openjdk:11-jdk-slim")
        ports.set(listOf(8087))
        images.set(setOf("galaxy-order:1.0", "galaxy-order:latest"))
        jvmArgs.set(listOf("-Dspring.profiles.active=production", "-Xmx2048m"))
    }
}
