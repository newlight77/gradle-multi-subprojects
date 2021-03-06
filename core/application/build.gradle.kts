
plugins {
	idea
	`java-library`
	id("org.springframework.boot") version "2.3.0.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("jvm")
	kotlin("plugin.spring") version "1.3.72"
	kotlin("plugin.jpa") version "1.3.70"
}

buildscript {
	configurations.classpath
			.resolutionStrategy.force("com.github.pinterest:ktlint:0.36.0")
}

group = "io.github.newlight77"
version = "0.0.1-SNAPSHOT"


repositories {
	mavenCentral()
}

dependencies {
	implementation(project(":core:domain"))
	implementation(project(":core:infrastructure:account"))
	implementation(project(":core:infrastructure:encryption"))
	implementation(project(":core:infrastructure:login"))
	implementation(project(":core:infrastructure:notification"))
	implementation(project(":core:infrastructure:note"))
	implementation(project(":core:infrastructure:okta-client"))
	implementation(project(":core:infrastructure:signup"))
	implementation(project(":core:infrastructure:storage"))

	implementation("org.springframework.boot:spring-boot-devtools")

	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")

//	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
//	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

//	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server:2.3.0.RELEASE")
//	implementation("org.springframework.security.oauth.boot:spring-security-oauth2-autoconfigure:2.3.0.RELEASE")
//	implementation("org.springframework.security.oauth:spring-security-oauth2:2.3.0.RELEASE")
//	implementation("org.springframework.security:spring-security-oauth2-jose:5.3.1.RELEASE")
	implementation("org.springframework.security:spring-security-oauth2-client")
	implementation("org.springframework.security:spring-security-oauth2-jose")
//	implementation("org.springframework.boot:spring-security-oauth2:2.3.0.RELEASE")
//	implementation("org.springframework.security:spring-security-jwt:1.1.0.RELEASE")

	implementation("org.keycloak:keycloak-spring-security-adapter:11.0.0")

	implementation("io.jsonwebtoken:jjwt:0.9.1")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("com.fasterxml.jackson.module:jackson-module-jaxb-annotations")
	implementation("com.github.ben-manes.caffeine:caffeine:2.8.1")

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.3.5")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
		exclude("junit", "junit")
	}

	testImplementation("io.cucumber:cucumber-java:5.6.0")
	testImplementation("io.cucumber:cucumber-java8:5.6.0")
	testImplementation("io.cucumber:cucumber-junit-platform-engine:5.6.0")
	testImplementation("io.cucumber:cucumber-spring:5.6.0")
	testImplementation("com.github.cukedoctor:cukedoctor-converter:1.2.1")

    testImplementation("io.rest-assured:spring-mock-mvc:4.3.0") {
        exclude("com.sun.xml.bind:jaxb-osgi")
    }

	testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
	testImplementation("org.mockito:mockito-junit-jupiter:3.3.0")
	testImplementation("org.assertj:assertj-core:3.11.1")
	testImplementation("com.icegreen:greenmail:1.5.13")


	implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.3.0.RELEASE")
	testImplementation("com.h2database:h2")
	implementation("org.postgresql:postgresql")

	implementation("org.liquibase:liquibase-core:3.8.9")

}

tasks.withType<Jar>() {
    baseName = "core-application"
}

testlogger {
	theme = com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA_PARALLEL
	showExceptions = true
	showStackTraces = true
	showFullStackTraces = true
	showCauses = true
	showSummary = true
	showStandardStreams = false
	showPassedStandardStreams = true
	showSkippedStandardStreams = true
	showFailedStandardStreams = true
}
