plugins {
    application
}

val log4jVersion = "2.11.2"

dependencies {
    implementation("com.github.ajalt:clikt:1.6.0")
    implementation(project(":puzzles-core"))
    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4jVersion")
}

application {
    mainClassName = "com.truecaller.puzzles.cli.MainKt"
}
