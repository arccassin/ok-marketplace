plugins {
    kotlin("jvm") apply false
}

group = "ru.otus.kotlin.circus"
version = "0.0.1"



allprojects{
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}