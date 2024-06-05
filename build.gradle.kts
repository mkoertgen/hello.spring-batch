import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	id("org.springframework.boot") version "3.3.0"
	id("io.spring.dependency-management") version "1.1.5"
	kotlin("jvm") version "1.9.24"
	kotlin("plugin.spring") version "1.9.24"
	kotlin("plugin.jpa") version "1.9.24"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
	// for Argo
	maven("https://maven.aksw.org/repository/internal/")
}

extra["springCloudVersion"] = "2023.0.2"

dependencies {
	// https://start.spring.io/#!type=gradle-project-kotlin&language=kotlin&platformVersion=3.3.0&packaging=jar&jvmVersion=17&groupId=com.example&artifactId=demo&name=demo&description=Demo%20project%20for%20Spring%20Boot&packageName=com.example.demo&dependencies=batch,prometheus,data-jpa,h2,web,devtools
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-batch")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.h2database:h2")
	runtimeOnly("io.micrometer:micrometer-registry-prometheus")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.batch:spring-batch-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// https://springdoc.org/#getting-started
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

	// spring batch on k8s: https://spring.io/blog/2021/01/27/spring-batch-on-kubernetes-efficient-batch-processing-at-scale
	// k8s api clients for java (gabri8 vs. official api): https://stackoverflow.com/a/73713709/2592915
	// https://github.com/kubernetes-client/java
	// https://central.sonatype.com/artifact/io.kubernetes/client-java
	implementation("io.kubernetes:client-java:20.0.1")

	// Argo Workflows: https://github.com/argoproj/argo-workflows/tree/main/sdks/java#java-sdk
	// https://mvnrepository.com/artifact/io.argoproj.workflow/argo-client-java
	implementation("io.argoproj.workflow:argo-client-java:v3.4.3")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
		jvmTarget = JvmTarget.JVM_17
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.bootBuildImage {
	docker {
		// enable local build (without push) for testing
		val cr = project.properties["cr"]?.toString() ?: "mycr.azurecr.io"
		val crUser = project.properties["cr_user"]?.toString() ?: ""
		val crPassword = project.properties["cr_pwd"]?.toString() ?: ""

		val crImage = project.properties["cr_image"]?.toString() ?: "${cr}/${project.group}/${project.name}"
		imageName = "${crImage}:${version}"

		publish = crUser.isNotBlank() && crPassword.isNotBlank()
		publishRegistry {
			username = crUser
			password = crPassword
			url = cr
		}
	}
}
