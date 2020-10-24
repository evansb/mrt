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
    id("org.springframework.boot") version("2.3.3.RELEASE") apply(false)
    id("io.spring.dependency-management") version("1.0.8.RELEASE") apply(false)
    id("com.diffplug.spotless") version("5.6.1") apply(false)
}

// Java
subprojects {
    apply<JavaPlugin>()
    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

// Common Dependencies
subprojects {
    dependencies {
        "implementation"("org.projectlombok:lombok")
        "annotationProcessor"("org.projectlombok:lombok")

        "testImplementation"("org.projectlombok:lombok")
        "testAnnotationProcessor"("org.projectlombok:lombok")
        "annotationProcessor"("org.springframework.boot:spring-boot-configuration-processor")

        "testImplementation"("org.junit.platform:junit-platform-runner")
        "testImplementation"("org.junit.jupiter:junit-jupiter-api")
        "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
        "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine")
    }
}

// Spring
subprojects {
    apply<SpringBootPlugin>()
    apply<SpringDependencyManagementPlugin>()
}

// Linting
project.apply {
    apply<SpotlessPlugin>()

    configure<SpotlessExtension> {
        format("misc") {
            target("**/*.gradle.kts")

            trimTrailingWhitespace()
            indentWithSpaces(4)
            endWithNewline()
        }
    }
}
subprojects {
    apply<SpotlessPlugin>()

    configure<SpotlessExtension> {
        java {
            importOrder()
            removeUnusedImports()
            googleJavaFormat()
        }
    }
}

// Testing
subprojects {
    val test: Test by tasks

    test.useJUnitPlatform()
    test.testLogging {
        events("passed", "skipped", "failed")
    }
}
