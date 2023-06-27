plugins {
    id("java")
}

dependencies {
    implementation("ch.qos.logback:logback-classic")
    implementation(project(":hw-19-jdbc"))
    implementation("org.flywaydb:flyway-core")
    implementation("org.postgresql:postgresql")
    implementation("com.zaxxer:HikariCP")
}
