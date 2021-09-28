package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.submitter

import io.mockk.*
import kotlinx.coroutines.runBlocking
import no.nav.brukernotifikasjon.schemas.internal.NokkelIntern
import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.CountingMetricsSessionsObjectMother
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count.CacheCountingMetricsSession
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count.CacheEventCounterService
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count.CacheMetricsReporter
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.TopicEventCounterAivenService
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.TopicMetricsReporter
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.TopicMetricsSession
import org.amshove.kluent.`should contain`
import org.amshove.kluent.`should not contain`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MetricsSubmitterServiceTest {

    private val cacheMetricsReporter: CacheMetricsReporter = mockk(relaxed = true)
    private val kafkaMetricsReporter: TopicMetricsReporter = mockk(relaxed = true)
    private val cacheEventCounterService: CacheEventCounterService = mockk(relaxed = true)
    private val topicEventCounterServiceAiven: TopicEventCounterAivenService<NokkelIntern> = mockk(relaxed = true)

    private val submitter = MetricsSubmitterService(
            cacheEventCounterService = cacheEventCounterService,
            topicEventCounterServiceAiven = topicEventCounterServiceAiven,
            cacheMetricsReporter = cacheMetricsReporter,
            kafkaMetricsReporter = kafkaMetricsReporter
    )

    @BeforeEach
    fun cleanup() {
        clearAllMocks()
    }

    @Test
    fun `Should report metrics for both kafka topics and the database cache`() {
        val topicMetricsSessions = CountingMetricsSessionsObjectMother.giveMeTopicSessionsForAllInternalEventTypes()
        val cacheMetricsSessions = CountingMetricsSessionsObjectMother.giveMeCacheSessionsForAllInternalEventTypes()

        coEvery { topicEventCounterServiceAiven.countAllEventTypesAsync() } returns topicMetricsSessions
        coEvery { cacheEventCounterService.countAllEventTypesAsync() } returns cacheMetricsSessions

        runBlocking {
            submitter.submitMetrics()
        }

        coVerify(exactly = 1) { topicEventCounterServiceAiven.countAllEventTypesAsync() }
        coVerify(exactly = 5) { kafkaMetricsReporter.report(any()) }
        coVerify(exactly = 5) { cacheMetricsReporter.report(any()) }

        confirmVerified(topicEventCounterServiceAiven)
        confirmVerified(kafkaMetricsReporter)
        confirmVerified(cacheMetricsReporter)
    }

    @Test
    fun `Should not report metrics for event types without metrics session`() {
        val topicMetricsInternSessions = CountingMetricsSessionsObjectMother.giveMeTopicSessionsForAllInternalEventTypesExceptForInnboks()
        val cacheMetricInternSessions = CountingMetricsSessionsObjectMother.giveMeCacheSessionsForAllInternalEventTypesExceptForInnboks()

        coEvery { topicEventCounterServiceAiven.countAllEventTypesAsync() } returns topicMetricsInternSessions
        coEvery { cacheEventCounterService.countAllEventTypesAsync() } returns cacheMetricInternSessions

        val reportedTopicMetricsForEventTypes = mutableListOf<EventType>()
        val capturedReportedTopicMetrics = slot<TopicMetricsSession>()
        coEvery {
            kafkaMetricsReporter.report(capture(capturedReportedTopicMetrics))
        } answers {
            val currentlyCapturedEventType = capturedReportedTopicMetrics.captured.eventType
            reportedTopicMetricsForEventTypes.add(currentlyCapturedEventType)
        }

        val reportedCacheMetricsForEventTypes = mutableListOf<EventType>()
        val capturedReportedCacheMetrics = slot<CacheCountingMetricsSession>()
        coEvery {
            cacheMetricsReporter.report(capture(capturedReportedCacheMetrics))
        } answers {
            val currentlyCapturedEventType = capturedReportedCacheMetrics.captured.eventType
            reportedCacheMetricsForEventTypes.add(currentlyCapturedEventType)
        }

        runBlocking {
            submitter.submitMetrics()
        }

        reportedTopicMetricsForEventTypes `should not contain` EventType.INNBOKS_INTERN
        reportedTopicMetricsForEventTypes `should contain` EventType.BESKJED_INTERN
        reportedTopicMetricsForEventTypes `should contain` EventType.DONE__INTERN
        reportedTopicMetricsForEventTypes `should contain` EventType.OPPGAVE_INTERN

        reportedCacheMetricsForEventTypes `should not contain` EventType.INNBOKS_INTERN
        reportedCacheMetricsForEventTypes `should contain` EventType.BESKJED_INTERN
        reportedCacheMetricsForEventTypes `should contain` EventType.DONE__INTERN
        reportedCacheMetricsForEventTypes `should contain` EventType.OPPGAVE_INTERN
    }

    @Test
    fun `Should not report metrics for count sessions with a lower count than the previous count session`() {
        val sessionWithCorrectCount = CountingMetricsSessionsObjectMother.giveMeTopicSessionsForAllInternalEventTypes()
        val simulatedWrongCount =
                CountingMetricsSessionsObjectMother.giveMeTopicSessionsWithSingleEventForAllInternalEventTypes()
        val dbMetricInternSessions = CountingMetricsSessionsObjectMother.giveMeCacheSessionsForAllInternalEventTypes()

        coEvery { topicEventCounterServiceAiven.countAllEventTypesAsync() } returns sessionWithCorrectCount andThen simulatedWrongCount andThen sessionWithCorrectCount
        coEvery { cacheEventCounterService.countAllEventTypesAsync() } returns dbMetricInternSessions

        runBlocking {
            submitter.submitMetrics()
            submitter.submitMetrics()
            submitter.submitMetrics()
        }

        coVerify(exactly = 3) { topicEventCounterServiceAiven.countAllEventTypesAsync() }

        coVerify(exactly = 5 * 2) { kafkaMetricsReporter.report(any()) }
        coVerify(exactly = 5 * 2) { cacheMetricsReporter.report(any()) }

        confirmVerified(topicEventCounterServiceAiven)
        confirmVerified(kafkaMetricsReporter)
        confirmVerified(cacheMetricsReporter)
    }

    @Test
    fun `Should not report metrics if one of the counting fails`() {
        val topicMetricsSessions = CountingMetricsSessionsObjectMother.giveMeTopicSessionsForAllInternalEventTypes()
        coEvery { topicEventCounterServiceAiven.countAllEventTypesAsync() } returns topicMetricsSessions

        runBlocking {
            submitter.submitMetrics()
        }

        coVerify(exactly = 1) { topicEventCounterServiceAiven.countAllEventTypesAsync() }

        coVerify(exactly = 0) { kafkaMetricsReporter.report(any()) }
        coVerify(exactly = 0) { cacheMetricsReporter.report(any()) }

        confirmVerified(topicEventCounterServiceAiven)
        confirmVerified(kafkaMetricsReporter)
        confirmVerified(cacheMetricsReporter)
    }

}
