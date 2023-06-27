dependencies {
    implementation("org.eclipse.jetty:jetty-servlet")
    implementation("org.eclipse.jetty:jetty-server")
    implementation("org.eclipse.jetty:jetty-webapp")
    implementation("org.eclipse.jetty:jetty-security")
    implementation("org.eclipse.jetty:jetty-http")
    implementation("org.eclipse.jetty:jetty-io")
    implementation("org.eclipse.jetty:jetty-util")
    implementation("ch.qos.logback:logback-classic")
    implementation("org.freemarker:freemarker")

    implementation("org.hibernate.orm:hibernate-core")
    implementation("org.flywaydb:flyway-core")

    implementation("org.postgresql:postgresql")
    implementation(project(":hw-21-jpql"))

    testImplementation("com.h2database:h2")
}
