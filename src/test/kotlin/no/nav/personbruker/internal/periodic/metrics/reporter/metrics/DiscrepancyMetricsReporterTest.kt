package no.nav.personbruker.internal.periodic.metrics.reporter.metrics

import io.mockk.*
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.common.metrics.MetricsReporter
import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType.BESKJED_INTERN
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count.CacheCountingMetricsSessionObjectMother.giveMeACacheSessionWithConfiguration
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.TopicMetricsSessionObjectMother.giveMeATopicSessionWithConfiguration
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be in`
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test


internal class DiscrepancyMetricsReporterTest {

    private val producer1 = Producer("namespace1", "producer1")
    private val producer2 = Producer("namespace2", "producer2")

    private val metricsReporter: MetricsReporter = mockk()

    private val discrepancyMetricsReporter = DiscrepancyMetricsReporter(metricsReporter)

    @AfterEach
    fun cleanup() {
        clearMocks(metricsReporter)
    }

    @Test
    fun `should report zero if cache and topic have same number of events`() {
        val capturedFieldsForTotalEvents = slot<Map<String, Any>>()
        val capturedFieldsForTotalEventsByProducer = slot<Map<String, Any>>()

        val events = listOf(
            producer1 to 4
        ).toMap()

        val topicEventSession = giveMeATopicSessionWithConfiguration(BESKJED_INTERN, events)
        val cacheEventSession = giveMeACacheSessionWithConfiguration(BESKJED_INTERN, events)

        coEvery { metricsReporter.registerDataPoint(DB_TOPIC_EVENTS_DISCREPANCY, capture(capturedFieldsForTotalEvents), any()) } returns Unit
        coEvery { metricsReporter.registerDataPoint(DB_TOPIC_EVENTS_DISCREPANCY_BY_PRODUCER, capture(capturedFieldsForTotalEventsByProducer), any()) } returns Unit

        runBlocking {
            discrepancyMetricsReporter.report(topicEventSession, cacheEventSession)
        }

        coVerify(exactly = 2) { metricsReporter.registerDataPoint(any(), any(), any()) }

        capturedFieldsForTotalEvents.captured["counter"] `should be equal to` 0
        capturedFieldsForTotalEventsByProducer.captured["counter"] `should be equal to` 0
    }

    @Test
    fun `should report correct number if cache and topic have same different number of events`() {
        val capturedFieldsForTotalEvents = slot<Map<String, Any>>()
        val capturedFieldsForTotalEventsByProducer = slot<Map<String, Any>>()

        val topicNumber = 4
        val cacheNumber = 7


        val topicEvents = listOf(
            producer1 to topicNumber
        ).toMap()

        val cacheEvents = listOf(
            producer1 to cacheNumber
        ).toMap()

        val topicEventSession = giveMeATopicSessionWithConfiguration(BESKJED_INTERN, topicEvents)
        val cacheEventSession = giveMeACacheSessionWithConfiguration(BESKJED_INTERN, cacheEvents)

        coEvery { metricsReporter.registerDataPoint(DB_TOPIC_EVENTS_DISCREPANCY, capture(capturedFieldsForTotalEvents), any()) } returns Unit
        coEvery { metricsReporter.registerDataPoint(DB_TOPIC_EVENTS_DISCREPANCY_BY_PRODUCER, capture(capturedFieldsForTotalEventsByProducer), any()) } returns Unit

        runBlocking {
            discrepancyMetricsReporter.report(topicEventSession, cacheEventSession)
        }

        coVerify(exactly = 2) { metricsReporter.registerDataPoint(any(), any(), any()) }

        val expectedDifference = cacheNumber - topicNumber

        capturedFieldsForTotalEvents.captured["counter"] `should be equal to` expectedDifference
        capturedFieldsForTotalEventsByProducer.captured["counter"] `should be equal to` expectedDifference
    }

    @Test
    fun `should report correct discrepancy for each producer`() {
        val capturedFieldsForTotalEvents = slot<Map<String, Any>>()
        val capturedFieldsForTotalEventsByProducer = slot<Map<String, Any>>()

        val topicNumber1 = 4
        val topicNumber2 = 13
        val cacheNumber1 = 7
        val cacheNumber2 = 19


        val topicEvents = listOf(
            producer1 to topicNumber1,
            producer2 to topicNumber2
        ).toMap()

        val cacheEvents = listOf(
            producer1 to cacheNumber1,
            producer2 to cacheNumber2
        ).toMap()

        val topicEventSession = giveMeATopicSessionWithConfiguration(BESKJED_INTERN, topicEvents)
        val cacheEventSession = giveMeACacheSessionWithConfiguration(BESKJED_INTERN, cacheEvents)

        coEvery { metricsReporter.registerDataPoint(DB_TOPIC_EVENTS_DISCREPANCY, capture(capturedFieldsForTotalEvents), any()) } returns Unit
        coEvery { metricsReporter.registerDataPoint(DB_TOPIC_EVENTS_DISCREPANCY_BY_PRODUCER, capture(capturedFieldsForTotalEventsByProducer), any()) } returns Unit

        runBlocking {
            discrepancyMetricsReporter.report(topicEventSession, cacheEventSession)
        }

        coVerify(exactly = 3) { metricsReporter.registerDataPoint(any(), any(), any()) }

        val expectedDifference1 = cacheNumber1 - topicNumber1
        val expectedDifference2 = cacheNumber2 - topicNumber2

        capturedFieldsForTotalEvents.captured["counter"] `should be equal to` expectedDifference1 + expectedDifference2
        capturedFieldsForTotalEventsByProducer.captured["counter"] `should be in` listOf(expectedDifference1, expectedDifference2)
    }

}
