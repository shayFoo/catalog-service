import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    java
    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.polarbookshop"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

tasks.getByName<BootRun>("bootRun") {
    systemProperty("spring.profiles.active", "testdata")
    doFirst {
        println("Running with 'testdata' profile")
    }
}

extra["springCloudVersion"] = "2025.0.0"
extra["testcontainersVersion"] = "1.21.3"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.retry:spring-retry")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")

    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// cloud native buildpack settings
tasks.named<BootBuildImage>("bootBuildImage") {
    environment = mapOf(
        "BP_JVM_VERSION" to "25",
        "BP_JVM_TIMEZONE" to "Asia/Tokyo",
        "LANG" to "ja_JP.UTF-8",
        "LANGUAGE" to "ja_JP:ja",
        "LC_ALL" to "ja_JP.UTF-8"
    )
    imageName = project.name
    docker {
        publishRegistry {
            username = project.findProperty("registryUsername")?.toString()
            password = project.findProperty("registryToken")?.toString()
            url = project.findProject("registryUrl")?.toString()
        }
    }
}
