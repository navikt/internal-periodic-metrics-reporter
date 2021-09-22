package no.nav.personbruker.internal.periodic.metrics.reporter.config

import no.nav.personbruker.dittnav.common.util.config.BooleanEnvVar.getEnvVarAsBoolean
import no.nav.personbruker.dittnav.common.util.config.IntEnvVar.getEnvVarAsInt
import no.nav.personbruker.dittnav.common.util.config.LongEnvVar.getEnvVarAsLong
import no.nav.personbruker.dittnav.common.util.config.StringEnvVar.getEnvVar
import java.net.URL

data class Environment(val username: String = getEnvVar("SERVICEUSER_USERNAME"),
                       val password: String = getEnvVar("SERVICEUSER_PASSWORD"),
                       val clusterName: String = getEnvVar("NAIS_CLUSTER_NAME"),
                       val namespace: String = getEnvVar("NAIS_NAMESPACE"),
                       val influxdbHost: String = getEnvVar("INFLUXDB_HOST"),
                       val influxdbPort: Int = getEnvVarAsInt("INFLUXDB_PORT"),
                       val influxdbName: String = getEnvVar("INFLUXDB_DATABASE_NAME"),
                       val influxdbUser: String = getEnvVar("INFLUXDB_USER"),
                       val influxdbPassword: String = getEnvVar("INFLUXDB_PASSWORD"),
                       val influxdbRetentionPolicy: String = getEnvVar("INFLUXDB_RETENTION_POLICY"),
                       val deltaCountingEnabled: Boolean = getEnvVarAsBoolean("DELTA_COUNTING_ENABLED", false),
                       val groupIdBase: String = "internal-periodic_metrics_reporter",
                       val countingIntervalMinutes: Long = getEnvVarAsLong("COUNTING_INTERVAL_MINUTES"),
                       val aivenBrokers: String = getEnvVar("KAFKA_BROKERS"),
                       val aivenSchemaRegistry: String = getEnvVar("KAFKA_SCHEMA_REGISTRY"),
                       val securityConfig: SecurityConfig = SecurityConfig(isCurrentlyRunningOnNais()),
                       val activityHistoryLength: Int = getEnvVarAsInt("ACTIVITY_HISTORY_LENGTH", 30),
                       val lowActivityStreakThreshold: Int = getEnvVarAsInt("LOW_ACTIVITY_STREAK_THRESHOLD", 60),
                       val moderateActivityStreakThreshold: Int = getEnvVarAsInt("MODERATE_ACTIVITY_STREAK_THRESHOLD", 15),
                       val highActivityStreakThreshold: Int = getEnvVarAsInt("HIGH_ACTIVITY_STREAK_THRESHOLD", 5),
                       val monitorBeskjedActivity: Boolean = getEnvVarAsBoolean("MONITOR_BESKJED_ACTIVITY"),
                       val monitorOppgaveActivity: Boolean = getEnvVarAsBoolean("MONITOR_OPPGAVE_ACTIVITY"),
                       val monitorInnboksActivity: Boolean = getEnvVarAsBoolean("MONITOR_INNBOKS_ACTIVITY"),
                       val monitorDoneActivity: Boolean = getEnvVarAsBoolean("MONITOR_DONE_ACTIVITY"),
                       val monitorStatusoppdateringActivity: Boolean = getEnvVarAsBoolean("MONITOR_STATUSOPPDATERING_ACTIVITY"),
                       val eventHandlerURL: URL = URL(getEnvVar("EVENT_HANDLER_URL").trimEnd('/')),
                       val beskjedInternTopicName: String = getEnvVar("INTERN_BESKJED_TOPIC"),
                       val oppgaveInternTopicName: String = getEnvVar("INTERN_OPPGAVE_TOPIC"),
                       val innboksInternTopicName: String = getEnvVar("INTERN_INNBOKS_TOPIC"),
                       val statusoppdateringInternTopicName: String = getEnvVar("INTERN_STATUSOPPDATERING_TOPIC"),
                       val doneInternTopicName: String = getEnvVar("INTERN_DONE_TOPIC")
)

data class SecurityConfig(
        val enabled: Boolean,

        val variables: SecurityVars? = if (enabled) {
            SecurityVars()
        } else {
            null
        }
)

data class SecurityVars(
        val aivenTruststorePath: String = getEnvVar("KAFKA_TRUSTSTORE_PATH"),
        val aivenKeystorePath: String = getEnvVar("KAFKA_KEYSTORE_PATH"),
        val aivenCredstorePassword: String = getEnvVar("KAFKA_CREDSTORE_PASSWORD"),
        val aivenSchemaRegistryUser: String = getEnvVar("KAFKA_SCHEMA_REGISTRY_USER"),
        val aivenSchemaRegistryPassword: String = getEnvVar("KAFKA_SCHEMA_REGISTRY_PASSWORD")
)

fun isOtherEnvironmentThanProd() = System.getenv("NAIS_CLUSTER_NAME") != "prod-gcp"

fun isCurrentlyRunningOnNais(): Boolean {
    return System.getenv("NAIS_APP_NAME") != null
}