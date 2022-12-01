plugins {

    kotlin("jvm")
    kotlin("plugin.spring")

    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.netflix.dgs.codegen")
}

group = "com.pranetr"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_18
val javaversion = "18"

repositories {
    mavenCentral()
}
dependencies {
    api(platform("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:latest.release"))
    api("com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter")
    api("com.netflix.graphql.dgs:graphql-dgs-spring-boot-micrometer")

    //api("org.springframework.cloud:spring-cloud-starter-config")

    api("com.graphql-java:graphql-java-extended-scalars")
    api("com.github.javafaker:javafaker:1.0.2")

    //For security
    api("org.springframework.boot:spring-boot-starter-security")
    api("io.jsonwebtoken:jjwt-api:0.11.5")
    api("io.jsonwebtoken:jjwt-impl:0.11.5")
    api("io.jsonwebtoken:jjwt-jackson:0.11.5")

    //For logging to json format
    api("org.apache.logging.log4j:log4j-api")
    api("org.apache.logging.log4j:log4j-core")
    api("com.fasterxml.jackson.core:jackson-core")
    api("com.fasterxml.jackson.core:jackson-databind")
    api("com.fasterxml.jackson.core:jackson-annotations")
    api("com.fasterxml.jackson.module:jackson-module-kotlin")

//    api("org.projectlombok:lombok:1.18.24")
    api("com.google.code.gson:gson")

    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-actuator")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
tasks.bootJar{
    enabled = false
}
tasks.jar{
    enabled = true
}
tasks.generateJava{
    enabled= false
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "$javaversion"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
tasks.generateJava{
    enabled=false
}