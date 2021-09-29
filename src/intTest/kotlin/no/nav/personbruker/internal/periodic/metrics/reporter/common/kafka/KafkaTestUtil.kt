package no.nav.personbruker.internal.periodic.metrics.reporter.common.kafka

import no.nav.common.JAASCredential
import no.nav.common.KafkaEnvironment
import no.nav.personbruker.internal.periodic.metrics.reporter.config.Environment
import no.nav.personbruker.internal.periodic.metrics.reporter.config.SecurityConfig
import org.apache.avro.generic.GenericRecord
import java.net.URL

object KafkaTestUtil {

    val username = "srvkafkaclient"
    val password = "kafkaclient"

    fun createDefaultKafkaEmbeddedInstance(topics: List<String>): KafkaEnvironment {
        return KafkaEnvironment(
                topicNames = topics,
                withSecurity = false,
                withSchemaRegistry = true,
                users = listOf(JAASCredential(username, password))
        )
    }

    fun createEnvironmentForEmbeddedKafka(embeddedEnv: KafkaEnvironment): Environment {
        return Environment(
                username = username,
                password = password,
                clusterName = "clusterNameIkkeIBrukHer",
                namespace = "namespaceIkkeIBrukHer",
                countingIntervalMinutes = 1,
                aivenBrokers = embeddedEnv.brokersURL.substringAfterLast("/"),
                aivenSchemaRegistry = embeddedEnv.schemaRegistry!!.url,
                influxdbHost = "",
                influxdbPort = 0,
                influxdbName = "",
                influxdbUser = "",
                influxdbPassword = "",
                influxdbRetentionPolicy = "",
                lowActivityStreakThreshold = 60,
                moderateActivityStreakThreshold = 15,
                highActivityStreakThreshold = 5,
                monitorBeskjedActivity = false,
                monitorOppgaveActivity = false,
                monitorInnboksActivity = false,
                monitorDoneActivity = false,
                monitorStatusoppdateringActivity = false,
                eventHandlerURL = URL("https://event_handler_url"),
                beskjedInternTopicName = KafkaTestTopics.beskjedInternTopicName,
                oppgaveInternTopicName = KafkaTestTopics.oppgaveInternTopicName,
                innboksInternTopicName = KafkaTestTopics.innboksInternTopicName,
                statusoppdateringInternTopicName = KafkaTestTopics.statusoppdateringInternTopicName,
                doneInternTopicName = KafkaTestTopics.doneInternTopicName,
                securityConfig = SecurityConfig(enabled = false),
                eventHandlerAppEnvironmentDetails = "cluster_namespace_appname"
        )
    }

    suspend fun <K> produceEvents(env: Environment, topicName: String, events: Map<K, GenericRecord>): Boolean {
        return KafkaProducerUtil.kafkaAvroProduce(
                env.aivenBrokers,
                env.aivenSchemaRegistry,
                topicName,
                events)
    }
}
