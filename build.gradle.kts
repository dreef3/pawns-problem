import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    kotlin("jvm") version("1.3.10") apply false
}

allprojects {
    group = "com.truecaller.puzzles"
    version = "1.0"
    repositories {
        jcenter()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        "implementation"("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        "testImplementation"("org.junit.jupiter:junit-jupiter-api:5.3.2")
        "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:5.3.2")
        "testImplementation"("org.assertj:assertj-core:3.12.2")
        "testImplementation"("net.jqwik:jqwik:1.1.1")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform {
            includeEngines = setOf("jqwik")
        }
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}
