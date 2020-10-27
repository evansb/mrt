import org.springframework.boot.gradle.plugin.SpringBootPlugin
import io.spring.gradle.dependencymanagement.DependencyManagementPlugin as SpringDependencyManagementPlugin
import org.gradle.api.tasks.testing.Test
import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin

// Project Metadata
subprojects {
    group = "com.github.evansb"
    version = "0.0.1-SNAPSHOT"
}

// Repositories Configuration
allprojects {
    repositories {
        jcenter()
        mavenCentral()
    }
}

// Common Plugins
plugins {
    java
    id("org.springframework.boot") version("2.3.3.RELEASE") apply(false)
    id("io.spring.dependency-management") version("1.0.8.RELEASE") apply(false)
    id("com.diffplug.spotless") version("5.6.1") apply(false)
}

// Java
configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

// Common Dependencies
dependencies {
    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv")
    // Note: jgrapht >1.5.0 requires Java 11
    implementation("org.jgrapht:jgrapht-core:1.4.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

// Spring
apply<SpringBootPlugin>()
apply<SpringDependencyManagementPlugin>()

// Linting
apply<SpotlessPlugin>()

configure<SpotlessExtension> {
    format("misc") {
        target("**/*.gradle.kts")

        trimTrailingWhitespace()
        indentWithSpaces(4)
        endWithNewline()
    }
    java {
        importOrder()
        removeUnusedImports()
        googleJavaFormat()
    }
}

// Testing
val test: Test by tasks

test.useJUnitPlatform()
test.testLogging {
    events("passed", "skipped", "failed")
}
