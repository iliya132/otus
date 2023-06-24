rootProject.name = "otusJava"
include ("hw01-gradle")
include("hw03-reflection")
include("hw-08-gc")
include("hw-10-aop")
include("hw-12-atm")
include("hw-15-structuralPatterns")
include("hw-16-io")
include("hw-19-jdbc")
include("hw-21-jpql")
include("hw-25-di")

pluginManagement {
    val jgitver: String by settings
    val dependencyManagement: String by settings
    val springframeworkBoot: String by settings
    val johnrengelmanShadow: String by settings
    val jib: String by settings

    plugins {
        id("fr.brouillard.oss.gradle.jgitver") version jgitver
        id("io.spring.dependency-management") version dependencyManagement
        id("org.springframework.boot") version springframeworkBoot
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
        id("com.google.cloud.tools.jib") version jib
    }
}
