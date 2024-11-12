plugins {
	java
	war
	id("org.springframework.boot") version "3.3.3"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.novelvox"


java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}
extra["springCloudVersion"] = "2023.0.3"
repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	implementation("org.springframework.boot:spring-boot-starter-log4j2")
	implementation("io.github.openfeign:feign-jackson:13.3")

//	implementation("jakarta.servlet:jakarta.servlet-api:5.0.0")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}
configurations {
	all {
//		exclude(group = "org.springframework.cloud", module = "spring-cloud-starter")
		exclude(group = "org.springframework.cloud", module = "spring-cloud-starter-netflix-ribbon")
		exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
//		exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
