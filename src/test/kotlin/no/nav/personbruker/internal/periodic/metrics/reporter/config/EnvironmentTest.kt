package no.nav.personbruker.internal.periodic.metrics.reporter.config

import io.kotest.extensions.system.withEnvironment
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

internal class EnvironmentTest {

    private val envVars = mapOf(
            "SERVICEUSER_USERNAME" to "username",
            "SERVICEUSER_PASSWORD" to "password",
            "NAIS_CLUSTER_NAME" to "cluster_name",
            "NAIS_NAMESPACE" to "namespace",
            "SENSU_HOST" to "sensu_host",
            "SENSU_PORT" to "1",
            "COUNTING_INTERVAL_MINUTES" to "1",
            "KAFKA_BROKERS" to "kafka_brokers",
            "KAFKA_SCHEMA_REGISTRY" to "kafka_schema_registry",
            "MONITOR_BESKJED_ACTIVITY" to "true",
            "MONITOR_OPPGAVE_ACTIVITY" to "true",
            "MONITOR_INNBOKS_ACTIVITY" to "true",
            "MONITOR_DONE_ACTIVITY" to "true",
            "MONITOR_STATUSOPPDATERING_ACTIVITY" to "true",
            "EVENT_HANDLER_URL" to "https://event_handler_url",
            "INTERN_BESKJED_TOPIC" to "internBeskjedTopic",
            "INTERN_OPPGAVE_TOPIC" to "internOppgaveTopic",
            "INTERN_INNBOKS_TOPIC" to "internInnboksTopic",
            "INTERN_STATUSOPPDATERING_TOPIC" to "internStatusoppdateringTopic",
            "INTERN_DONE_TOPIC" to "internDoneTopic",
            "INFLUXDB_HOST" to "influx_host",
            "INFLUXDB_PORT" to "1",
            "INFLUXDB_DATABASE_NAME" to "influx_db",
            "INFLUXDB_USER" to "influx_user",
            "INFLUXDB_PASSWORD" to "influx_pw",
            "INFLUXDB_RETENTION_POLICY" to "influx_retention",
            "HANDLER_ENVIRONMENT_DETAILS" to "cluster_namespace_appname"
    )

    @Test
    fun `Om DELTA_COUNTING_MODE ikke er satt som env_var evalueres den til default false`() {
        withEnvironment(envVars) {
            Environment().deltaCountingEnabled `should be equal to` false
        }
    }

    @Test
    fun `Om DELTA_COUNTING_MODE  er satt som "FALSE" env_var evalueres den til false`() {
        withEnvironment(envVars + ("DELTA_COUNTING_ENABLED" to "false")) {
            Environment().deltaCountingEnabled `should be equal to` false
        }
    }

    @Test
    fun `Om DELTA_COUNTING_MODE er satt som "TRUE" env_var evalueres den til true`() {
        withEnvironment(envVars + ("DELTA_COUNTING_ENABLED" to "true")) {
            Environment().deltaCountingEnabled `should be equal to` true
        }
    }

}
