package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic

import io.mockk.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import no.nav.brukernotifikasjon.schemas.internal.NokkelIntern
import no.nav.personbruker.internal.periodic.metrics.reporter.common.kafka.Consumer
import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.KafkaEventIdentifier
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.activity.TopicActivityService
import org.amshove.kluent.`should be greater than`
import org.amshove.kluent.shouldNotBeNull
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration

internal class TopicEventTypeCounterTest {

    private val polledEvents: ConsumerRecords<NokkelIntern, GenericRecord> = mockk()
    private val polledNoEvents: ConsumerRecords<NokkelIntern, GenericRecord> = mockk()

    private val activityService: TopicActivityService = mockk()


    @BeforeEach
    fun resetMocks() {
        clearMocks(polledEvents)
        clearMocks(polledNoEvents)
        every { polledEvents.isEmpty } returns false
        every { polledNoEvents.isEmpty } returns true
    }

    @Test
    internal fun `Should calculate processing time`() {

        mockkObject(KafkaKeyIdentifierTransformer)
        mockkObject(TopicEventTypeCounter)

        val consumer: Consumer<NokkelIntern, GenericRecord> = mockk()

        val deltaCountingEnabled = true
        val counter = TopicEventTypeCounter(consumer, activityService, EventType.BESKJED_INTERN, deltaCountingEnabled)

        every { consumer.kafkaConsumer.poll(any<Duration>()) } returns polledEvents andThen polledNoEvents
        every { activityService.reportEventsFound() } returns Unit
        every { activityService.reportNoEventsFound() } returns Unit

        val sessionSlot = slot<TopicMetricsSession>()

        val minimumProcessingTimeInMs: Long = 500

        every { TopicEventTypeCounter.countBatch(polledEvents, capture(sessionSlot)) } coAnswers {
            sessionSlot.captured.countEvent(KafkaEventIdentifier("1", "test", "test"))
            sessionSlot.captured.countEvent(KafkaEventIdentifier("2", "test", "test"))
            sessionSlot.captured.countEvent(KafkaEventIdentifier("3", "test", "test"))
            delay(minimumProcessingTimeInMs)
        }


        every { TopicEventTypeCounter.countBatch(polledNoEvents, capture(sessionSlot)) } coAnswers {
            // Det skal ikke utføres noe når ingen eventer telles
        }

        val minimumProcessingTimeInNs: Long = minimumProcessingTimeInMs * 1000000
        val session = runBlocking {
            counter.countEventsAsync().await()
        }

        session.getProcessingTime() `should be greater than` minimumProcessingTimeInNs
        counter.getPreviousSession().shouldNotBeNull()
    }

}
