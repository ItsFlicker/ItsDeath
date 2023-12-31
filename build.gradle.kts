plugins {
    `java-library`
    `maven-publish`
    id("io.izzel.taboolib") version "1.56"
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
}

val adyeshachVersion = "2.0.0-snapshot-25"

taboolib {
    description {
        contributors {
            name("ItsFlicker")
        }
        dependencies {
            name("Adyeshach")
            name("ProtocolLib")
        }
    }
    install("common")
    install("common-5")
    install("module-configuration")
    install("module-chat")
    install("module-kether")
    install("module-lang")
    install("module-nms")
    install("module-nms-util")
    install("platform-bukkit")
    install("expansion-command-helper")
    install("expansion-geek-tool")
    install("expansion-ioc")
    classifier = null
    version = "6.0.12-56"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
//    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("ink.ptms.core:v12004:12004:mapped")
    compileOnly("ink.ptms.core:v12004:12004:universal")
    compileOnly("com.velocitypowered:velocity-brigadier:1.0.0-SNAPSHOT")
    compileOnly("net.kyori:adventure-api:4.15.0")
    compileOnly("net.kyori:adventure-platform-bukkit:4.3.2")

    compileOnly("ink.ptms.adyeshach:common:$adyeshachVersion")
    compileOnly("ink.ptms.adyeshach:common-impl:$adyeshachVersion")

    compileOnly("com.google.code.gson:gson:2.8.7")
    compileOnly("net.md-5:bungeecord-chat:1.20")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

publishing {
    repositories {
        maven {
            url = uri("https://repo.tabooproject.org/repository/releases")
            credentials {
                username = project.findProperty("taboolibUsername").toString()
                password = project.findProperty("taboolibPassword").toString()
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            groupId = project.group.toString()
        }
    }
}