import org.flywaydb.gradle.task.FlywayCleanTask
import org.flywaydb.gradle.task.FlywayMigrateTask

plugins {
    java
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.flywaydb.flyway") version "7.7.3"
}

group = "com.kbhealthcare.ocare"
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
    /**
     * spring
     */
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

    /**
     * flyway
     */
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")

    /**
     * gson
     */
    implementation("com.google.code.gson:gson:2.10.1")

    /**
     * querydsl
     */
    implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta")
    implementation("com.querydsl:querydsl-core:5.1.0")

    /**
     * etc
     */
    implementation("io.springfox:springfox-swagger-ui:3.0.0")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.4.2")

    /**
     * Swagger
     */
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

    runtimeOnly("com.mysql:mysql-connector-j")

    /**
     * test
     */
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.batch:spring-batch-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("io.kotest:kotest-runner-junit5:4.6.3")
    testImplementation("io.kotest:kotest-assertions-core:4.6.3")
    testImplementation("com.ninja-squad:springmockk:4.0.2")

    /**
     * mockk
     */
    testImplementation("io.mockk:mockk:1.13.3")

    /**
     * Util
     */
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

flyway {
    url = "jdbc:mysql://localhost:3306/health_sync?serverTimezone=UTC&characterEncoding=UTF-8"
    user = "health_sync"
    password = "health_sync"
    baselineVersion = "1"
    baselineOnMigrate = true
    schemas = listOf("health_sync").toTypedArray()
    locations = listOf("filesystem:src/main/resources/db/migration", "filesystem:src/main/resources/db/seed").toTypedArray()
}

tasks.register<FlywayMigrateTask>("flywayMigrateTestDB", fun FlywayMigrateTask.() {
    description = "migrate health_sync_test"
    url =
        "jdbc:mysql://127.0.0.1:3306/health_sync_test?useUnicode=yes&characterEncoding=UTF-8&serverTimezone=UTC&characterEncoding=UTF-8"
    user = "health_sync"
    password = "health_sync"
    schemas = listOf("health_sync_test").toTypedArray()
})

tasks.register<FlywayCleanTask>("flywayCleanTestDB", fun FlywayCleanTask.() {
    description = "clean health_sync_test"
    url =
        "jdbc:mysql://127.0.0.1:3306/health_sync_test?useUnicode=yes&characterEncoding=UTF-8&serverTimezone=UTC&characterEncoding=UTF-8"
    user = "health_sync"
    password = "health_sync"
    schemas = listOf("health_sync_test").toTypedArray()
})

tasks.withType<Test> {
    useJUnitPlatform()
}
