package no.nav.personbruker.internal.periodic.metrics.reporter.config

import no.nav.personbruker.dittnav.common.util.config.BooleanEnvVar.getEnvVarAsBoolean
import no.nav.personbruker.dittnav.common.util.config.IntEnvVar.getEnvVarAsInt
import no.nav.personbruker.dittnav.common.util.config.LongEnvVar.getEnvVarAsLong
import no.nav.personbruker.dittnav.common.util.config.StringEnvVar.getEnvVar
import java.net.URL

data class Environment(val bootstrapServers: String = getEnvVar("KAFKA_BOOTSTRAP_SERVERS"),
                       val schemaRegistryUrl: String = getEnvVar("KAFKA_SCHEMAREGISTRY_SERVERS"),
                       val username: String = getEnvVar("SERVICEUSER_USERNAME"),
                       val password: String = getEnvVar("SERVICEUSER_PASSWORD"),
                       val clusterName: String = getEnvVar("NAIS_CLUSTER_NAME"),
                       val namespace: String = getEnvVar("NAIS_NAMESPACE"),
                       val sensuHost: String = getEnvVar("SENSU_HOST"),
                       val sensuPort: Int = getEnvVarAsInt("SENSU_PORT"),
                       val deltaCountingEnabled: Boolean = getEnvVarAsBoolean("DELTA_COUNTING_ENABLED", false),
                       val groupIdBase: String = "dn-periodic_metrics_reporter",
                       val countingIntervalMinutes: Long = getEnvVarAsLong("COUNTING_INTERVAL_MINUTES"),
                       val aivenBrokers: String = getEnvVar("KAFKA_BROKERS"),
                       val aivenTruststorePath: String = getEnvVar("KAFKA_TRUSTSTORE_PATH"),
                       val aivenKeystorePath: String = getEnvVar("KAFKA_KEYSTORE_PATH"),
                       val aivenCredstorePassword: String = getEnvVar("KAFKA_CREDSTORE_PASSWORD"),
                       val aivenSchemaRegistry: String = getEnvVar("KAFKA_SCHEMA_REGISTRY"),
                       val aivenSchemaRegistryUser: String = getEnvVar("KAFKA_SCHEMA_REGISTRY_USER"),
                       val aivenSchemaRegistryPassword: String = getEnvVar("KAFKA_SCHEMA_REGISTRY_PASSWORD"),
                       val activityHistoryLength: Int = getEnvVarAsInt("ACTIVITY_HISTORY_LENGTH", 30),
                       val lowActivityStreakThreshold: Int = getEnvVarAsInt("LOW_ACTIVITY_STREAK_THRESHOLD", 60),
                       val moderateActivityStreakThreshold: Int = getEnvVarAsInt("MODERATE_ACTIVITY_STREAK_THRESHOLD", 15),
                       val highActivityStreakThreshold: Int = getEnvVarAsInt("HIGH_ACTIVITY_STREAK_THRESHOLD", 5),
                       val monitorBeskjedActivity: Boolean = getEnvVarAsBoolean("MONITOR_BESKJED_ACTIVITY"),
                       val monitorOppgaveActivity: Boolean = getEnvVarAsBoolean("MONITOR_OPPGAVE_ACTIVITY"),
                       val monitorInnboksActivity: Boolean = getEnvVarAsBoolean("MONITOR_INNBOKS_ACTIVITY"),
                       val monitorDoneActivity: Boolean = getEnvVarAsBoolean("MONITOR_DONE_ACTIVITY"),
                       val monitorStatusoppdateringActivity: Boolean = getEnvVarAsBoolean("MONITOR_STATUSOPPDATERING_ACTIVITY"),
                       val eventHandlerURL: URL = URL(getEnvVar("EVENT_HANDLER_URL").trimEnd('/'))
)

fun isOtherEnvironmentThanProd() = System.getenv("NAIS_CLUSTER_NAME") != "prod-gcp"
