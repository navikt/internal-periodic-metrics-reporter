package no.nav.personbruker.internal.periodic.metrics.reporter.config

import no.nav.personbruker.internal.periodic.metrics.reporter.common.kafka.Consumer
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

object KafkaConsumerSetup {

    private val log: Logger = LoggerFactory.getLogger(KafkaConsumerSetup::class.java)

    fun <K, V> setupCountConsumer(kafkaProps: Properties, topic: String): Consumer<K, V> {
        val kafkaConsumer = KafkaConsumer<K, V>(kafkaProps)
        return Consumer(topic, kafkaConsumer)
    }

    fun startSubscriptionOnAllKafkaConsumersAiven(appContext: ApplicationContext) {
        if (isOtherEnvironmentThanProd()) {
            appContext.beskjedCountAivenConsumer.startSubscription()
            appContext.oppgaveCountAivenConsumer.startSubscription()
            appContext.doneCountAivenConsumer.startSubscription()
        } else {
            log.info("Er i produksjonsmiljø, unnlater å starte consumere på Aiven.")
        }
    }

    suspend fun stopAllKafkaConsumersAiven(appContext: ApplicationContext) {
        log.info("Begynner å stoppe kafka-pollerne på Aiven...")
        if (isOtherEnvironmentThanProd()) {
            appContext.beskjedCountAivenConsumer.stop()
            appContext.oppgaveCountAivenConsumer.stop()
            appContext.doneCountAivenConsumer.stop()
        }
        log.info("...ferdig med å stoppe kafka-pollerne på Aiven.")
    }

    suspend fun restartConsumersAiven(appContext: ApplicationContext) {
        stopAllKafkaConsumersAiven(appContext)
        appContext.reinitializeConsumersAiven()
        startSubscriptionOnAllKafkaConsumersAiven(appContext)
    }
}
