
plugins {
    java

    kotlin("jvm") version "1.7.10"
    kotlin("plugin.spring") version "1.5.0"
    id("com.netflix.dgs.codegen") version "5.6.0"
    id("org.springframework.boot") version "2.6.3"
//
//    id("org.springframework.boot") version "3.0.0"
//    id("io.spring.dependency-management") version "1.1.0"
//    kotlin("jvm") version "1.7.10"
//    kotlin("plugin.spring") version "1.5.0"

   // id("com.netflix.dgs.codegen") version "4.4.3"
}
sourceSets {
    main {
        java {
            srcDir(file("src/main/kotlin"))
        }
    }
    test {
        java {
            srcDir(file("src/test/kotlin"))
        }
    }
}
java.sourceCompatibility = JavaVersion.VERSION_18
repositories {
    mavenCentral()
}

tasks.generateJava{
    enabled = false
}

tasks.bootJar{
    enabled = false
}
tasks.jar{
    enabled = false
}