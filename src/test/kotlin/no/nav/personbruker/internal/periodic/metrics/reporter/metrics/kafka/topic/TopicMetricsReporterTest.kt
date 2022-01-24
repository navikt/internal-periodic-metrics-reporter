package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic

import io.mockk.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.common.metrics.MetricsReporter
import no.nav.personbruker.internal.periodic.metrics.reporter.common.`with message containing`
import no.nav.personbruker.internal.periodic.metrics.reporter.common.exceptions.MetricsReportingException
import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.*
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.KafkaEventIdentifier
import org.amshove.kluent.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TopicMetricsReporterTest {

    private val metricsReporter = mockk<MetricsReporter>(relaxed = true)
    private val topicMetricsReporter = TopicMetricsReporter(metricsReporter)

    @BeforeEach
    fun cleanup() {
        clearAllMocks()
    }

    @Test
    fun `Should report correct number of events`() {
        val capturedFieldsForTotalEvents = slot<Map<String, Any>>()
        val capturedFieldsForTotalEventsByProducer = slot<Map<String, Any>>()

        coEvery { metricsReporter.registerDataPoint(KAFKA_TOTAL_EVENTS_ON_TOPIC, capture(capturedFieldsForTotalEvents), any()) } returns Unit
        coEvery { metricsReporter.registerDataPoint(KAFKA_TOTAL_EVENTS_ON_TOPIC_BY_PRODUCER, capture(capturedFieldsForTotalEventsByProducer), any()) } returns Unit

        val session = TopicMetricsSession(EventType.BESKJED_INTERN)
        session.countEvent(KafkaEventIdentifier("1",  "namespace", "producer"))
        session.countEvent(KafkaEventIdentifier("2",  "namespace", "producer"))
        session.countEvent(KafkaEventIdentifier("3",  "namespace", "producer"))
        runBlocking {
            topicMetricsReporter.report(session)
        }

        coVerify(exactly = 3) { metricsReporter.registerDataPoint(any(), any(), any()) }

        capturedFieldsForTotalEvents.captured["counter"] `should be equal to` 3
        capturedFieldsForTotalEventsByProducer.captured["counter"] `should be equal to` 3
    }

    @Test
    fun `Should report the elapsed time to count the events`() {
        val expectedProcessingTimeMs = 100L
        val expectedProcessingTimeNs = expectedProcessingTimeMs * 1000000

        val capturedFieldsForProcessingTime = slot<Map<String, Long>>()

        coEvery { metricsReporter.registerDataPoint(KAFKA_COUNT_PROCESSING_TIME, capture(capturedFieldsForProcessingTime), any()) } returns Unit

        val session = TopicMetricsSession(EventType.BESKJED_INTERN)
        session.countEvent(KafkaEventIdentifier("1", "dummyNamespace", "dummyAppnavn"))
        runBlocking { delay(expectedProcessingTimeMs) }
        session.calculateProcessingTime()
        runBlocking {
            topicMetricsReporter.report(session)
        }

        capturedFieldsForProcessingTime.captured["counter"]!!.shouldBeGreaterOrEqualTo(expectedProcessingTimeNs)
        val fiftyPercentMoreThanExpectedTime = (expectedProcessingTimeNs * 1.5).toLong()
        capturedFieldsForProcessingTime.captured["counter"]!!.shouldBeLessThan(fiftyPercentMoreThanExpectedTime)
    }

    @Test
    fun `Should use the provided appname as producername`() {
        val producerApp = "dummyAppnavn"
        val producerNamespace = "dummyAppnavn"

        val capturedTagsForTotalByProducer = slot<Map<String, String>>()

        coEvery { metricsReporter.registerDataPoint(KAFKA_TOTAL_EVENTS_ON_TOPIC_BY_PRODUCER, any(), capture(capturedTagsForTotalByProducer)) } returns Unit

        val session = TopicMetricsSession(EventType.BESKJED_INTERN)
        session.countEvent(KafkaEventIdentifier("1", producerNamespace, producerApp))
        session.countEvent(KafkaEventIdentifier("2", producerNamespace, producerApp))
        runBlocking {
            topicMetricsReporter.report(session)
        }

        coVerify(exactly = 1) { metricsReporter.registerDataPoint(KAFKA_TOTAL_EVENTS_ON_TOPIC_BY_PRODUCER, any(), any()) }

        capturedTagsForTotalByProducer.captured["producer"] `should be equal to` producerApp
    }

    @Test
    fun `Should not report metrics if current count is zero`() {
        val sessionWithoutAnyReportedEventsSimulatesACountError = TopicMetricsSession(EventType.BESKJED_INTERN)
        runBlocking {
            topicMetricsReporter.report(sessionWithoutAnyReportedEventsSimulatesACountError)
        }

        val numberOfSuccessfulCountingSessions = 0
        coVerify(exactly = numberOfSuccessfulCountingSessions) { metricsReporter.registerDataPoint(any(), any(), any()) }
    }

    @Test
    internal fun `should handle exceptions and rethrow them as internal exceptions`() {
        val simulatedException = Exception("Simulated error in a test")
        coEvery { metricsReporter.registerDataPoint(any(), any(), any()) } throws simulatedException

        invoking {
            runBlocking {
                topicMetricsReporter.report(TopicMetricsSessionObjectMother.giveMeBeskjedSessionWithOneCountedEvent())
            }
        } `should throw` MetricsReportingException::class `with message containing` "beskjed_intern"

        invoking {
            runBlocking {
                topicMetricsReporter.report(TopicMetricsSessionObjectMother.giveMeDoneSessionWithOneCountedEvent())
            }
        } `should throw` MetricsReportingException::class `with message containing` "done_intern"

        invoking {
            runBlocking {
                topicMetricsReporter.report(TopicMetricsSessionObjectMother.giveMeInnboksSessionWithOneCountedEvent())
            }
        } `should throw` MetricsReportingException::class `with message containing` "innboks_intern"

        invoking {
            runBlocking {
                topicMetricsReporter.report(TopicMetricsSessionObjectMother.giveMeOppgaveSessionWithOneCountedEvent())
            }
        } `should throw` MetricsReportingException::class `with message containing` "oppgave_intern"

        invoking {
            runBlocking {
                topicMetricsReporter.report(TopicMetricsSessionObjectMother.giveMeStatusoppdateringSessionWithOneCountedEvent())
            }
        } `should throw` MetricsReportingException::class `with message containing` "statusoppdatering_intern"
    }

}
