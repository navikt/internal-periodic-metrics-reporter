package no.nav.personbruker.internal.periodic.metrics.reporter.common.kafka.util

import no.nav.common.JAASCredential
import no.nav.common.KafkaEnvironment
import no.nav.personbruker.internal.periodic.metrics.reporter.config.Environment
import org.apache.avro.generic.GenericRecord
import java.net.URL

object KafkaTestUtil {

    val username = "srvkafkaclient"
    val password = "kafkaclient"

    fun createDefaultKafkaEmbeddedInstance(withSecurity: Boolean, topics: List<String>): KafkaEnvironment {
        return KafkaEnvironment(
                topicNames = topics,
                withSecurity = withSecurity,
                withSchemaRegistry = true,
                users = listOf(JAASCredential(username, password))
        )
    }

    fun createEnvironmentForEmbeddedKafka(embeddedEnv: KafkaEnvironment): Environment {
        return Environment(
                bootstrapServers = embeddedEnv.brokersURL.substringAfterLast("/"),
                schemaRegistryUrl = embeddedEnv.schemaRegistry!!.url,
                username = username,
                password = password,
                clusterName = "clusterNameIkkeIBrukHer",
                namespace = "namespaceIkkeIBrukHer",
                sensuHost = "sensuHostIkkeIBrukHer",
                sensuPort = 0,
                countingIntervalMinutes = 1,
                aivenBrokers = embeddedEnv.brokersURL.substringAfterLast("/"),
                aivenTruststorePath = "aivenTruststorePathIkkeIBrukHer",
                aivenKeystorePath = "aivenKeystorePathIkkeIBrukHer",
                aivenCredstorePassword = "aivenCredstorePasswordIkkeIBrukHer",
                aivenSchemaRegistry = embeddedEnv.schemaRegistry!!.url,
                aivenSchemaRegistryUser = username,
                aivenSchemaRegistryPassword = password,
                lowActivityStreakThreshold = 60,
                moderateActivityStreakThreshold = 15,
                highActivityStreakThreshold = 5,
                monitorBeskjedActivity = false,
                monitorOppgaveActivity = false,
                monitorInnboksActivity = false,
                monitorDoneActivity = false,
                monitorStatusoppdateringActivity = false,
                eventHandlerURL = URL("https://event_handler_url")

        )
    }

    suspend fun <K> produceEvents(env: Environment, topicName: String, enableSecurity: Boolean, events: Map<K, GenericRecord>): Boolean {
        return KafkaProducerUtil.kafkaAvroProduce(
                env.bootstrapServers,
                env.schemaRegistryUrl,
                topicName,
                env.username,
                env.password,
                enableSecurity,
                events)
    }
}
