plugins {
    kotlin("jvm") version "1.9.21"
    id("com.diffplug.spotless") version "6.23.3"
    application
}

group = "com.sully.patterns"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
}

spotless {
    kotlin {
        target("src/**/*.kt")
        ktlint("1.0.1")
        indentWithSpaces(4)
        endWithNewline()
        trimTrailingWhitespace()
    }

    kotlinGradle {
        target("*.gradle.kts")
        ktlint("1.0.1")
        indentWithSpaces(4)
        endWithNewline()
        trimTrailingWhitespace()
    }
}

tasks.register("preCommitCheck") {
    dependsOn("spotlessCheck")
    doLast {
        println("Pre-commit formatting checks passed!")
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
