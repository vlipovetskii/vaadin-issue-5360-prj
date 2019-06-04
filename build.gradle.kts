import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {

    // Add assignments like below, to overwrite values,
    // which are configured in "versions.gradle.kts", but only inside current root project and it's subprojects.
    
    rootProject.extra["karibudsl10Version"] = "0.6.3"
    rootProject.extra["springBootStarterVaadinVersion"] = "13.0.6"

    rootProject.extra["spring_bootVersion"] = "2.1.1.RELEASE"

    rootProject.extra["jaxbapiVersion"] = "2.3.1"
    rootProject.extra["jacksonModule_kotlinVersion"] = "2.9.8"
    rootProject.extra["logbackVersion"] = "1.2.3"
    rootProject.extra["junit5Version"] = "5.4.2"
    rootProject.extra["kluent_Version"] = "1.49"

}

plugins {

    kotlin("jvm") version "1.3.31"
    id("org.springframework.boot") version "2.1.1.RELEASE"
}

subprojects {

    apply(plugin = "java")

    java {
        sourceCompatibility = JavaVersion.VERSION_1_10
        targetCompatibility = JavaVersion.VERSION_1_10
    }

    apply(plugin = "kotlin")

    tasks.withType<KotlinCompile>().all {
        kotlinOptions {
            jvmTarget = "10"
            // freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }

    group = "vlfsoft"
    version = 1.0 // -> vlfsoft.module-1.0.jar
    val jar by tasks.getting(Jar::class) {
        manifest {
            attributes["Automatic-Module-Name"] = project.name
        }
    }

    repositories {

        jcenter()
        mavenLocal()
        mavenCentral()

        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }

    }

    dependencies {

        compile(kotlin("stdlib-jdk8"))
        implementation(kotlin("stdlib-jdk7"))
        implementation(kotlin("reflect"))

        compile("com.fasterxml.jackson.module:jackson-module-kotlin:${rootProject.extra["jacksonModule_kotlinVersion"]}")

        testImplementation(kotlin("test"))
        testImplementation(kotlin("test-junit"))

        implementation("javax.xml.bind:jaxb-api:${rootProject.extra["jaxbapiVersion"]}")

        compile("ch.qos.logback:logback-classic:${rootProject.extra["logbackVersion"]}")

        testImplementation("org.junit.jupiter:junit-jupiter-api:${rootProject.extra["junit5Version"]}")

        testImplementation("org.amshove.kluent:kluent:${rootProject.extra["kluent_Version"]}")

    }

}
