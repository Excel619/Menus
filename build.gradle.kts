import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
plugins {
    kotlin("jvm") version "1.7.20"
    `java-library`
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "com.gmail.excel8392.menus"
version = "1.0"

repositories {
    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")

    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")

    mavenLocal()
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.19.3-R0.1-SNAPSHOT") // The Spigot API with no shadowing. Requires the OSS repo.
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}

kotlin {
    jvmToolchain(11)
}