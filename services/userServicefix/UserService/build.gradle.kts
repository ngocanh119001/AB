plugins {
    id("java")
    id("org.springframework.boot") version("2.7.8") // Đảm bảo phiên bản Spring Boot là đúng
    id("io.spring.dependency-management") version("1.0.15.RELEASE")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // JUnit
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    // MongoDB Java Driver (nếu dùng sau này)

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-logging")

    // Spring Boot DevTools (dành cho phát triển ứng dụng nhanh chóng)
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Spring Boot Actuator (giám sát ứng dụng)
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Spring Boot Security (nếu cần bảo mật)
    // implementation("org.springframework.boot:spring-boot-starter-security")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17)) // Hoặc đổi thành 11 nếu bạn dùng JDK 11
    }
}
