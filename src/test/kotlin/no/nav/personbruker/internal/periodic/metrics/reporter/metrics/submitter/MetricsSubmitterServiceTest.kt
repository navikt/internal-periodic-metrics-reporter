package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.submitter

import io.mockk.*
import kotlinx.coroutines.runBlocking
import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.CountingMetricsSessionsObjectMother
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.db.count.DbCountingMetricsSession
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.db.count.DbEventCounterGCPService
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.db.count.DbMetricsReporter
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.TopicEventCounterAivenService
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.TopicMetricsReporter
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.TopicMetricsSession
import org.amshove.kluent.`should contain`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MetricsSubmitterServiceTest {

    private val dbMetricsReporter: DbMetricsReporter = mockk(relaxed = true)
    private val kafkaMetricsReporter: TopicMetricsReporter = mockk(relaxed = true)
    private val dbEventCounterGCPService: DbEventCounterGCPService = mockk(relaxed = true)
    private val topicEventCounterServiceAiven: TopicEventCounterAivenService<Nokkel> = mockk(relaxed = true)

    private val submitter = MetricsSubmitterService(
            dbEventCounterGCPService = dbEventCounterGCPService,
            topicEventCounterServiceAiven = topicEventCounterServiceAiven,
            dbMetricsReporter = dbMetricsReporter,
            kafkaMetricsReporter = kafkaMetricsReporter
    )

    @BeforeEach
    fun cleanup() {
        clearAllMocks()
    }

    @Test
    fun `Should report metrics for both kafka topics and the database cache`() {
        val topicMetricsSessions = CountingMetricsSessionsObjectMother.giveMeTopicSessionsForAllInternalEventTypes()
        val dbMetricsSessions = CountingMetricsSessionsObjectMother.giveMeDatabaseSessionsForAllInternalEventTypes()

        coEvery { topicEventCounterServiceAiven.countAllEventTypesAsync() } returns topicMetricsSessions
        coEvery { dbEventCounterGCPService.countAllEventTypesAsync() } returns dbMetricsSessions

        runBlocking {
            submitter.submitMetrics()
        }

        coVerify(exactly = 1) { topicEventCounterServiceAiven.countAllEventTypesAsync() }
        coVerify(exactly = 5) { kafkaMetricsReporter.report(any()) }
        coVerify(exactly = 5) { dbMetricsReporter.report(any()) }

        confirmVerified(topicEventCounterServiceAiven)
        confirmVerified(kafkaMetricsReporter)
        confirmVerified(dbMetricsReporter)
    }

    @Test
    fun `Should not report metrics for event types without metrics session`() {
        val topicMetricsInternSessions = CountingMetricsSessionsObjectMother.giveMeTopicSessionsForAllInternalEventTypes()
        val dbMetricInternSessions = CountingMetricsSessionsObjectMother.giveMeDatabaseSessionsForAllInternalEventTypes()

        coEvery { topicEventCounterServiceAiven.countAllEventTypesAsync() } returns topicMetricsInternSessions
        coEvery { dbEventCounterGCPService.countAllEventTypesAsync() } returns dbMetricInternSessions

        val reportedTopicMetricsForEventTypes = mutableListOf<EventType>()
        val capturedReportedTopicMetrics = slot<TopicMetricsSession>()
        coEvery {
            kafkaMetricsReporter.report(capture(capturedReportedTopicMetrics))
        } answers {
            val currentlyCapturedEventType = capturedReportedTopicMetrics.captured.eventType
            reportedTopicMetricsForEventTypes.add(currentlyCapturedEventType)
        }

        val reportedDbMetricsForEventTypes = mutableListOf<EventType>()
        val capturedReportedDbMetrics = slot<DbCountingMetricsSession>()
        coEvery {
            dbMetricsReporter.report(capture(capturedReportedDbMetrics))
        } answers {
            val currentlyCapturedEventType = capturedReportedDbMetrics.captured.eventType
            reportedDbMetricsForEventTypes.add(currentlyCapturedEventType)
        }

        runBlocking {
            submitter.submitMetrics()
        }

        reportedTopicMetricsForEventTypes `should contain` EventType.INNBOKS //Todo burde denne endres?
        reportedTopicMetricsForEventTypes `should contain` EventType.BESKJED
        reportedTopicMetricsForEventTypes `should contain` EventType.DONE
        reportedTopicMetricsForEventTypes `should contain` EventType.OPPGAVE

        reportedDbMetricsForEventTypes `should contain` EventType.INNBOKS //Todo burde denne endres?
        reportedDbMetricsForEventTypes `should contain` EventType.BESKJED
        reportedDbMetricsForEventTypes `should contain` EventType.DONE
        reportedDbMetricsForEventTypes `should contain` EventType.OPPGAVE
    }

    @Test
    fun `Should not report metrics for count sessions with a lower count than the previous count session`() {
        val sessionWithCorrectCount = CountingMetricsSessionsObjectMother.giveMeTopicSessionsForAllInternalEventTypes()
        val simulatedWrongCount =
                CountingMetricsSessionsObjectMother.giveMeTopicSessionsWithSingleEventForAllInternalEventTypes()
        val dbMetricInternSessions = CountingMetricsSessionsObjectMother.giveMeDatabaseSessionsForAllInternalEventTypes()

        coEvery { topicEventCounterServiceAiven.countAllEventTypesAsync() } returns sessionWithCorrectCount andThen simulatedWrongCount andThen sessionWithCorrectCount
        coEvery { dbEventCounterGCPService.countAllEventTypesAsync() } returns dbMetricInternSessions

        runBlocking {
            submitter.submitMetrics()
            submitter.submitMetrics()
            submitter.submitMetrics()
        }

        coVerify(exactly = 3) { topicEventCounterServiceAiven.countAllEventTypesAsync() }

        coVerify(exactly = 5 * 2) { kafkaMetricsReporter.report(any()) }
        coVerify(exactly = 5 * 2) { dbMetricsReporter.report(any()) }

        confirmVerified(topicEventCounterServiceAiven)
        confirmVerified(kafkaMetricsReporter)
        confirmVerified(dbMetricsReporter)
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
        coVerify(exactly = 0) { dbMetricsReporter.report(any()) }

        confirmVerified(topicEventCounterServiceAiven)
        confirmVerified(kafkaMetricsReporter)
        confirmVerified(dbMetricsReporter)
    }

}
