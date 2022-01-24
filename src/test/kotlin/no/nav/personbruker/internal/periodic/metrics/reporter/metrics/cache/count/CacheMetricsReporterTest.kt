package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count

import io.mockk.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.common.metrics.MetricsReporter
import no.nav.personbruker.internal.periodic.metrics.reporter.common.`with message containing`
import no.nav.personbruker.internal.periodic.metrics.reporter.common.exceptions.MetricsReportingException
import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.DB_COUNT_PROCESSING_TIME
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.DB_TOTAL_EVENTS_IN_CACHE_BY_PRODUCER
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.EventCountForProducer
import org.amshove.kluent.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CacheMetricsReporterTest {

    private val metricsReporter = mockk<MetricsReporter>(relaxed = true)
    private val cacheMetricsReporter = CacheMetricsReporter(metricsReporter)

    @BeforeEach
    fun cleanup() {
        clearAllMocks()
    }

    @Test
    fun `Should report correct number of events`() {
        val dummyCountResultFromDb = listOf(
            EventCountForProducer("namespace2", "produsent2", 5)
        )

        val capturedTotalEventsInCacheByProducer = slot<Map<String, Any>>()

        coEvery { metricsReporter.registerDataPoint(DB_TOTAL_EVENTS_IN_CACHE_BY_PRODUCER, capture(capturedTotalEventsInCacheByProducer), any()) } returns Unit

        val session = CacheCountingMetricsSession(EventType.BESKJED_INTERN)
        session.addEventsByProducer(dummyCountResultFromDb)
        runBlocking {
            cacheMetricsReporter.report(session)
        }

        coVerify(exactly = 3) { metricsReporter.registerDataPoint(any(), any(), any()) }

        capturedTotalEventsInCacheByProducer.captured["counter"] `should be equal to` 5
    }

    @Test
    fun `Should report the elapsed time to count the events`() {
        val expectedProcessingTimeMs = 100L
        val expectedProcessingTimeNs = expectedProcessingTimeMs * 1000000

        val dummyCountResultFromDb = listOf(
            EventCountForProducer("namespace1","produsent1", 1),
            EventCountForProducer("namespace2", "produsent2", 2)
        )

        val capturedProcessingTime = slot<Map<String, Long>>()

        coEvery { metricsReporter.registerDataPoint(DB_COUNT_PROCESSING_TIME, capture(capturedProcessingTime), any()) } returns Unit

        val session = CacheCountingMetricsSession(EventType.BESKJED_INTERN)
        session.addEventsByProducer(dummyCountResultFromDb)
        runBlocking {
            delay(expectedProcessingTimeMs)
        }
        session.calculateProcessingTime()
        runBlocking {
            cacheMetricsReporter.report(session)
        }

        capturedProcessingTime.captured["counter"]!!.shouldBeGreaterOrEqualTo(expectedProcessingTimeNs)
        val twentyPercentMoreThanExpectedTime = (expectedProcessingTimeNs * 1.2).toLong()
        capturedProcessingTime.captured["counter"]!!.shouldBeLessThan(twentyPercentMoreThanExpectedTime)
    }

    @Test
    fun `Should use the provided appname as producername`() {
        val producerAppName = "dummyAppnavn"
        val producerNamespace = "dummyNamespace"

        val capturedTagsForTotalByProducer = slot<Map<String, String>>()

        coEvery { metricsReporter.registerDataPoint(DB_TOTAL_EVENTS_IN_CACHE_BY_PRODUCER, any(), capture(capturedTagsForTotalByProducer)) } returns Unit

        val session = CacheCountingMetricsSession(EventType.BESKJED_INTERN)
        session.addEventsByProducer(listOf(EventCountForProducer(producerNamespace, producerAppName, 2)))
        runBlocking {
            cacheMetricsReporter.report(session)
        }

        coVerify(exactly = 1) { metricsReporter.registerDataPoint(DB_TOTAL_EVENTS_IN_CACHE_BY_PRODUCER, any(), any()) }


        capturedTagsForTotalByProducer.captured["producer"] `should be equal to` producerAppName
    }

    @Test
    internal fun `should handle exceptions and rethrow them as internal exceptions`() {
        val simulatedException = Exception("Simulated error in a test")
        coEvery { metricsReporter.registerDataPoint(any(), any(), any()) } throws simulatedException

        invoking {
            runBlocking {
                cacheMetricsReporter.report(CacheCountingMetricsSessionObjectMother.giveMeBeskjedInternSessionWithOneCountedEvent())
            }
        } `should throw` MetricsReportingException::class `with message containing` "beskjed_intern"

        invoking {
            runBlocking {
                cacheMetricsReporter.report(CacheCountingMetricsSessionObjectMother.giveMeDoneInternSessionWithTwoCountedEvents())
            }
        } `should throw` MetricsReportingException::class `with message containing` "done_intern"

        invoking {
            runBlocking {
                cacheMetricsReporter.report(CacheCountingMetricsSessionObjectMother.giveMeInnboksInternSessionWithThreeCountedEvents())
            }
        } `should throw` MetricsReportingException::class `with message containing` "innboks_intern"

        invoking {
            runBlocking {
                cacheMetricsReporter.report(CacheCountingMetricsSessionObjectMother.giveMeOppgaveInternSessionWithFourCountedEvents())
            }
        } `should throw` MetricsReportingException::class `with message containing` "oppgave_intern"
    }

}
