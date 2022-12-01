
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
    id("com.netflix.dgs.codegen")
    java
    id("com.bmuschko.docker-spring-boot-application") version "6.7.0"
}

group = "com.pranetr"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_18
val javaversion = "18"
val module = "catalog"
val port = 8083

repositories {
    mavenCentral()
}

configurations {
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
}
dependencies {
    implementation(project(":foundation"))
    implementation(project(":schema-registry"))
    implementation("com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "$javaversion"
    }
}
tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.generateJava{
    enabled= false
}
docker {
    springBootApplication {
        baseImage.set("openjdk:$javaversion")
        ports.set(listOf(port))
        images.set(setOf("pranetr-$module:$version", "pranetr-$module:latest"))
        jvmArgs.set(listOf("-Dspring.profiles.active=production", "-Xmx2048m"))
    }
}