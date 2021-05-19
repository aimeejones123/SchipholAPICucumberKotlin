import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    jacoco
    kotlin("jvm") version "1.5.0"
    id("com.github.spacialcircumstances.gradle-cucumber-reporting") version "0.1.22"

}

group = "com.example"
version = "0.0.1-SNAPSHOT"

configurations {}

val cucumberRuntime by configurations.creating {
    extendsFrom(configurations["testImplementation"])
}
val restAssuredVersion = "4.3.0"
val cucumberVersion = "5.6.0"
val springVersion = "5.2.1.RELEASE"



repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.0")
    implementation("io.rest-assured:rest-assured:${restAssuredVersion}")
    implementation("io.rest-assured:json-path:${restAssuredVersion}")
    implementation("io.rest-assured:json-schema-validator:${restAssuredVersion}")
    implementation("io.rest-assured:kotlin-extensions:${restAssuredVersion}")
    implementation("io.cucumber:cucumber-java:$cucumberVersion")
    implementation("io.cucumber:cucumber-java8:$cucumberVersion")
    implementation("io.cucumber:cucumber-spring:$cucumberVersion")
    implementation("org.springframework:spring-context:${springVersion}")
    implementation("org.springframework:spring-test:${springVersion}")

    implementation("org.hamcrest:hamcrest-junit:2.0.0.0")

    implementation("org.awaitility:awaitility-kotlin:4.0.2")
    implementation("com.google.code.gson:gson:2.8.6")

}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

task("cucumber") {
    dependsOn("assemble", "compileTestJava")
    doLast {
        javaexec {
            main = "io.cucumber.core.cli.Main"
            classpath = cucumberRuntime + sourceSets.main.get().output + sourceSets.test.get().output
            // Change glue for your project package where the step definitions are.
            // And where the feature files are.
            args = listOf("--plugin", "pretty", "--plugin", "json:build/cucumber-report/data/json/report.json", "--glue", "com.schipol.stepdefs", "src/test/resources", "--strict")
            // Configure jacoco agent for the test coverage.
            val jacocoAgent = zipTree(configurations.jacocoAgent.get().singleFile)
                .filter { it.name == "jacocoagent.jar" }
                .singleFile
            jvmArgs = listOf("-javaagent:$jacocoAgent=destfile=$buildDir/results/jacoco/cucumber.exec,append=false")
        }
    }
}

cucumberReports {
    outputDir = file("build/test-report/html")
    buildId = "1.0"
    reports = files("build/cucumber-report/data/json/report.json")
}

tasks.jacocoTestReport {
    // Give jacoco the file generated with the cucumber tests for the coverage.
    executionData(files("$buildDir/jacoco/test.exec", "$buildDir/results/jacoco/cucumber.exec"))
    reports {
        xml.isEnabled = true
    }
}