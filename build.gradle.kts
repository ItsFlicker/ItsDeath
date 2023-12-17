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
        }
    }
    install("common")
    install("common-5")
    install("module-configuration")
    install("module-chat")
    install("module-kether")
    install("module-lang")
    install("platform-bukkit")
    install("expansion-command-helper")
    install("expansion-ioc")
    classifier = null
    version = "6.0.12-41"
}

repositories {
    mavenCentral()
}

dependencies {
//    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("ink.ptms.core:v12002:12002:mapped")
    compileOnly("ink.ptms.core:v12002:12002:universal")

    compileOnly("ink.ptms.adyeshach:common:$adyeshachVersion")
    compileOnly("ink.ptms.adyeshach:common-impl:$adyeshachVersion")

    compileOnly("com.google.code.gson:gson:2.8.5")
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