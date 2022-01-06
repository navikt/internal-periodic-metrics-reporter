package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import no.nav.brukernotifikasjon.schemas.internal.NokkelIntern
import no.nav.common.KafkaEnvironment
import no.nav.personbruker.internal.periodic.metrics.reporter.beskjed.AvroBeskjedInternObjectMother
import no.nav.personbruker.internal.periodic.metrics.reporter.common.kafka.KafkaTestTopics
import no.nav.personbruker.internal.periodic.metrics.reporter.common.kafka.KafkaTestUtil
import no.nav.personbruker.internal.periodic.metrics.reporter.config.Environment
import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.config.Kafka
import no.nav.personbruker.internal.periodic.metrics.reporter.config.KafkaConsumerSetup
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.activity.TopicActivityService
import no.nav.personbruker.internal.periodic.metrics.reporter.nokkel.AvroNokkelInternObjectMother
import org.amshove.kluent.`should be equal to`
import org.apache.avro.generic.GenericRecord
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TopicEventCounterAivenServiceIT {

    private lateinit var embeddedEnv: KafkaEnvironment
    private lateinit var testEnvironment: Environment
    private val topicActivityService = TopicActivityService(30)

    private val events = (1..5).map { AvroNokkelInternObjectMother.createNokkel(it) to AvroBeskjedInternObjectMother.createBeskjed(it) }.toMap()

    @AfterEach
    fun `tear down`() {
        embeddedEnv.adminClient?.close()
        embeddedEnv.tearDown()
    }

    @BeforeEach
    fun setup() {
        embeddedEnv = KafkaTestUtil.createDefaultKafkaEmbeddedInstance(listOf(KafkaTestTopics.beskjedInternTopicName))
        testEnvironment = KafkaTestUtil.createEnvironmentForEmbeddedKafka(embeddedEnv)
        embeddedEnv.start()
    }

    @Test
    fun `Skal telle korrekt total antall av eventer`() {
        `Produser eventer`(KafkaTestTopics.beskjedInternTopicName)

        val kafkaProps = Kafka.counterConsumerAivenProps(testEnvironment, EventType.BESKJED_INTERN)
        val beskjedInternCountConsumer = KafkaConsumerSetup.setupCountConsumer<NokkelIntern, GenericRecord>(kafkaProps, KafkaTestTopics.beskjedInternTopicName)
        beskjedInternCountConsumer.startSubscription()

        val topicEventTypeCounter = TopicEventTypeCounter(
                consumer = beskjedInternCountConsumer,
                topicActivityService = topicActivityService,
                eventType = EventType.BESKJED_INTERN,
                deltaCountingEnabled = false
        )

        val metricsSession = runBlocking {
            topicEventTypeCounter.countEventsAsync().await()
        }

        metricsSession.getNumberOfEvents() `should be equal to` events.size

        runBlocking { beskjedInternCountConsumer.stop() }
    }

    @Test
    fun `Ved deltatelling skal metrikkene akkumuleres fra forrige telling`() {
        val kafkaProps = Kafka.counterConsumerAivenProps(testEnvironment, EventType.BESKJED_INTERN)
        val beskjedInternCountConsumer = KafkaConsumerSetup.setupCountConsumer<NokkelIntern, GenericRecord>(kafkaProps, KafkaTestTopics.beskjedInternTopicName)
        beskjedInternCountConsumer.startSubscription()

        val deltaTopicEventTypeCounter = TopicEventTypeCounter(
                consumer = beskjedInternCountConsumer,
                topicActivityService = topicActivityService,
                eventType = EventType.BESKJED_INTERN,
                deltaCountingEnabled = true
        )
        `Produser eventer`(KafkaTestTopics.beskjedInternTopicName)
        runBlocking {
            deltaTopicEventTypeCounter.countEventsAsync().await()
        }

        `Produser eventer`(KafkaTestTopics.beskjedInternTopicName)
        val metricsSession = runBlocking {
            deltaTopicEventTypeCounter.countEventsAsync().await()
        }

        metricsSession.getNumberOfEvents() `should be equal to` events.size * 2

        runBlocking { beskjedInternCountConsumer.stop() }
    }

    @Test
    fun `Ved telling etter reset av offset blir resultatet det samme som etter deltatelling `() {
        val deltaCountingEnv = testEnvironment.copy(groupIdBase = "delta")
        val fromScratchCountingEnv = testEnvironment.copy(groupIdBase = "fromScratch")

        val kafkaPropsDeltaCounting = Kafka.counterConsumerAivenProps(deltaCountingEnv, EventType.BESKJED_INTERN)
        val deltaCountingConsumer =
                KafkaConsumerSetup.setupCountConsumer<NokkelIntern, GenericRecord>(kafkaPropsDeltaCounting, KafkaTestTopics.beskjedInternTopicName)
        deltaCountingConsumer.startSubscription()

        val kafkaPropsFromScratchCounting = Kafka.counterConsumerAivenProps(fromScratchCountingEnv, EventType.BESKJED_INTERN)
        val fromScratchCountingConsumer =
                KafkaConsumerSetup.setupCountConsumer<NokkelIntern, GenericRecord>(kafkaPropsFromScratchCounting, KafkaTestTopics.beskjedInternTopicName)
        fromScratchCountingConsumer.startSubscription()

        val deltaTopicEventTypeCounter = TopicEventTypeCounter(
                consumer = deltaCountingConsumer,
                topicActivityService = topicActivityService,
                eventType = EventType.BESKJED_INTERN,
                deltaCountingEnabled = true
        )
        val fromScratchTopicEventTypeCounter = TopicEventTypeCounter(
                consumer = fromScratchCountingConsumer,
                topicActivityService = topicActivityService,
                eventType = EventType.BESKJED_INTERN,
                deltaCountingEnabled = false
        )

        `Produser eventer`(KafkaTestTopics.beskjedInternTopicName)
        runBlocking {
            deltaTopicEventTypeCounter.countEventsAsync().await()
        }
        `Produser eventer`(KafkaTestTopics.beskjedInternTopicName)
        val deltaMetricsSession = runBlocking {
            deltaTopicEventTypeCounter.countEventsAsync().await()
        }

        val fromScratchMetricsSession = runBlocking {
            fromScratchTopicEventTypeCounter.countEventsAsync().await()
        }

        deltaMetricsSession.getNumberOfEvents() `should be equal to` fromScratchMetricsSession.getNumberOfEvents()

        runBlocking {
            deltaCountingConsumer.stop()
            fromScratchCountingConsumer.stop()
        }
    }

    @Test
    fun `Skal telle riktig antall eventer flere ganger paa rad ved bruk av samme kafka-klient`() {
        `Produser eventer`(KafkaTestTopics.beskjedInternTopicName)
        val kafkaProps = Kafka.counterConsumerAivenProps(testEnvironment, EventType.BESKJED_INTERN)
        val beskjedCountConsumer = KafkaConsumerSetup.setupCountConsumer<NokkelIntern, GenericRecord>(kafkaProps, KafkaTestTopics.beskjedInternTopicName)
        beskjedCountConsumer.startSubscription()

        val topicEventTypeCounter = TopicEventTypeCounter(
                consumer = beskjedCountConsumer,
                topicActivityService = topicActivityService,
                eventType = EventType.BESKJED_INTERN,
                deltaCountingEnabled = false
        )

        `tell og verifiser korrekte antall eventer flere ganger paa rad`(topicEventTypeCounter)

        runBlocking { beskjedCountConsumer.stop() }
    }

    private fun `tell og verifiser korrekte antall eventer flere ganger paa rad`(
            topicEventTypeCounter: TopicEventTypeCounter<NokkelIntern>
    ) {
        `tell og verifiser korrekt antall eventer`(topicEventTypeCounter)
        `tell og verifiser korrekt antall eventer`(topicEventTypeCounter)
        `tell og verifiser korrekt antall eventer`(topicEventTypeCounter)
    }

    private fun `tell og verifiser korrekt antall eventer`(
            topicEventTypeCounter: TopicEventTypeCounter<NokkelIntern>
    ) {
        val metricsSession = runBlocking {
            delay(500)
            topicEventTypeCounter.countEventsAsync().await()
        }

        metricsSession.getNumberOfEvents() `should be equal to` events.size
    }

    private fun `Produser eventer`(topic: String) {
        runBlocking {
            val fikkProduserBatch1 = KafkaTestUtil.produceEvents(testEnvironment, topic, events)
            fikkProduserBatch1
        } `should be equal to` true
    }

}
