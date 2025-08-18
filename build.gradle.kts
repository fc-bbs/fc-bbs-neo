plugins {
    java
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.bbs"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("com.qcloud:cos_api:5.6.8") {
        exclude(group = "org.slf4j", module = "slf4j-log4j12")
    }
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    runtimeOnly("com.mysql:mysql-connector-j")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    // Sa-Token
    implementation("cn.dev33:sa-token-spring-boot3-starter:1.37.0")
    implementation("cn.dev33:sa-token-redis-jackson:1.37.0")

    // Redisson
    implementation("com.zengtengpeng:redisson-spring-boot-starter:1.0.10")

    // Hibernate Validator
    implementation("org.hibernate.validator:hibernate-validator")

    // Hutool
    implementation("cn.hutool:hutool-all:5.7.22")

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // FastJSON
    implementation("com.alibaba:fastjson:1.2.51")

    // QR Code
    implementation("com.google.zxing:javase:3.3.0")

    // Testing
    testImplementation("junit:junit")
    testImplementation("org.springframework:spring-test")
    testImplementation("org.springframework.boot:spring-boot-test")

    // Okhttp3
    implementation("com.squareup.okhttp3:okhttp:3.4.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
