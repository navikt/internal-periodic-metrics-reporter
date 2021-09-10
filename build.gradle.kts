import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin on the JVM.
    kotlin("jvm").version(Kotlin.version)
    kotlin("plugin.allopen").version(Kotlin.version)

    id(Shadow.pluginId) version (Shadow.version)

    // Apply the application plugin to add support for building a CLI application.
    application
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "12"
}

repositories {
    jcenter()
    mavenCentral()
    maven("https://packages.confluent.io/maven")
    mavenLocal()
    maven("https://jitpack.io")
}

sourceSets {
    create("intTest") {
        compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output
        runtimeClasspath += sourceSets.main.get().output + sourceSets.test.get().output
    }
}

val intTestImplementation by configurations.getting {
    extendsFrom(configurations.testImplementation.get())
}
configurations["intTestRuntimeOnly"].extendsFrom(configurations.testRuntimeOnly.get())

dependencies {
    implementation(Brukernotifikasjon.schemas)
    implementation(Brukernotifikasjon.schemas_internal)
    implementation(DittNAV.Common.utils)
    implementation(DittNAV.Common.influx)
    implementation(Hikari.cp)
    implementation(Influxdb.java)
    implementation(Kafka.Apache.clients)
    implementation(Kafka.Confluent.avroSerializer)
    implementation(Ktor.serverNetty)
    implementation(Ktor.htmlBuilder)
    implementation(Ktor.clientApache)
    implementation(Ktor.clientJson)
    implementation(Ktor.clientJackson)
    implementation(Jackson.dataTypeJsr310)
    implementation(Logback.classic)
    implementation(Logstash.logbackEncoder)
    implementation(NAV.vaultJdbc)
    implementation(Postgresql.postgresql)
    implementation(Prometheus.common)
    implementation(Prometheus.hotspot)
    implementation(Prometheus.logback)

    testImplementation(H2Database.h2)
    testImplementation(Junit.api)
    testImplementation(Junit.engine)
    testImplementation(Kafka.Apache.kafka_2_12)
    testImplementation(Kafka.Apache.streams)
    testImplementation(Kluent.kluent)
    testImplementation(Kafka.Confluent.schemaRegistry)
    testImplementation(Ktor.clientMock)
    testImplementation(Ktor.clientMockJvm)
    testImplementation(Mockk.mockk)
    testImplementation(NAV.kafkaEmbedded)
    testImplementation("io.kotest:kotest-runner-junit5:4.3.1")
    testImplementation("io.kotest:kotest-assertions-core:4.3.1")
    testImplementation("io.kotest:kotest-extensions:4.3.1")
    testImplementation(ULID.sulkyUlid)
    intTestImplementation(Junit.engine)
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

tasks {
    withType<Test> {
        useJUnitPlatform()
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
            events("passed", "skipped", "failed")
        }
    }

    register("runServer", JavaExec::class) {
        println("Setting default environment variables for running with DittNAV docker-compose")
        DockerComposeDefaults.environomentVariables.forEach { (name, value) ->
            println("Setting the environment variable $name")
            environment(name, value)
        }

        environment("COUNTING_INTERVAL_MINUTES", "1")

        environment("KAFKA_BROKERS", "localhost:29092")
        environment("KAFKA_TRUSTSTORE_PATH", "/path/to/trust/store")
        environment("KAFKA_KEYSTORE_PATH", "/path/to/key/store")
        environment("KAFKA_KEYSTORE_PATH", "superhemmelig")
        environment("KAFKA_CREDSTORE_PASSWORD", "superhemmelig")
        environment("KAFKA_SCHEMA_REGISTRY", "http://localhost:8081")
        environment("KAFKA_SCHEMA_REGISTRY_USER", "schema-user")
        environment("KAFKA_SCHEMA_REGISTRY_PASSWORD", "schema-pwd")

        environment("DELTA_COUNTING_ENABLED", "true")
        environment("REQUIRE_EVENTS_IN_FIRST_BATCH", "true")

        main = application.mainClass.get()
        classpath = sourceSets["main"].runtimeClasspath
    }
}

val integrationTest = task<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"

    testClassesDirs = sourceSets["intTest"].output.classesDirs
    classpath = sourceSets["intTest"].runtimeClasspath
    shouldRunAfter("test")
}

tasks.check { dependsOn(integrationTest) }

// TODO: Fjern følgende work around i ny versjon av Shadow-pluginet:
// Skal være løst i denne: https://github.com/johnrengelman/shadow/pull/612
project.setProperty("mainClassName", application.mainClass.get())
apply(plugin = Shadow.pluginId)
