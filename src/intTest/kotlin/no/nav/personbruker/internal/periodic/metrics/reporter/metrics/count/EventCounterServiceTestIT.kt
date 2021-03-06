package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.count

import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.internal.periodic.metrics.reporter.common.HandlerConsumer
import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.EventCountForProducer
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.Producer
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count.CacheCountingMetricsProbe
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count.CacheCountingMetricsSession
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count.CacheEventCounterService
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

internal class EventCounterServiceTestIT {

    private val handlerConsumer = mockk<HandlerConsumer>()

    private val commonNamespace = "namespace"

    @Test
    fun `Should count beskjed events`() {
        val appnavne = createProducers("b_appnavn_A", "b_appnavn_B")
        val result = createResult(appnavne)
        coEvery { handlerConsumer.getEventCount(any()) }.returns(result)

        val metricsProbe = mockk<CacheCountingMetricsProbe>(relaxed = true)
        val metricsSession = initMetricsSession(metricsProbe, EventType.BESKJED_INTERN)
        val service = CacheEventCounterService(metricsProbe, handlerConsumer)

        runBlocking {
            service.countBeskjeder()
        }

        metricsSession.getTotalNumber() `should be equal to` 2
        metricsSession.getProducers().size `should be equal to` 2
        metricsSession.getNumberOfEventsFor(appnavne[0]) `should be equal to` 1
        metricsSession.getNumberOfEventsFor(appnavne[1]) `should be equal to` 1
    }

    @Test
    fun `Should count innboks events`() {
        val appnavne = createProducers("i_appnavn_A", "i_appnavn_B")
        val result = createResult(appnavne)
        coEvery { handlerConsumer.getEventCount(any()) }.returns(result)

        val metricsProbe = mockk<CacheCountingMetricsProbe>(relaxed = true)
        val metricsSession = initMetricsSession(metricsProbe, EventType.INNBOKS_INTERN)
        val service = CacheEventCounterService(metricsProbe, handlerConsumer)

        runBlocking {
            service.countInnboksEventer()
        }

        metricsSession.getTotalNumber() `should be equal to` 2
        metricsSession.getProducers().size `should be equal to` 2
        metricsSession.getNumberOfEventsFor(appnavne[0]) `should be equal to` 1
        metricsSession.getNumberOfEventsFor(appnavne[1]) `should be equal to` 1
    }

    @Test
    fun `Should count oppgave events`() {
        val appnavne = createProducers("o_appnavn_A", "o_appnavn_B")
        val result = createResult(appnavne)
        coEvery { handlerConsumer.getEventCount(any()) }.returns(result)

        val metricsProbe = mockk<CacheCountingMetricsProbe>(relaxed = true)
        val metricsSession = initMetricsSession(metricsProbe, EventType.OPPGAVE_INTERN)
        val service = CacheEventCounterService(metricsProbe, handlerConsumer)

        runBlocking {
            service.countOppgaver()
        }

        metricsSession.getTotalNumber() `should be equal to` 2
        metricsSession.getProducers().size `should be equal to` 2
        metricsSession.getNumberOfEventsFor(appnavne[0]) `should be equal to` 1
        metricsSession.getNumberOfEventsFor(appnavne[1]) `should be equal to` 1
    }

    @Test
    fun `Should count done events`() {
        val appnavne = createProducers("d_appnavn_A", "d_appnavn_B")
        val result = createResult(appnavne)
        coEvery { handlerConsumer.getEventCount(any()) }.returns(result)

        val metricsProbe = mockk<CacheCountingMetricsProbe>(relaxed = true)
        val metricsSession = initMetricsSession(metricsProbe, EventType.DONE_INTERN)
        val service = CacheEventCounterService(metricsProbe, handlerConsumer)

        runBlocking {
            service.countDoneEvents()
        }

        metricsSession.getTotalNumber() `should be equal to` 2
        metricsSession.getProducers().size `should be equal to` 2
        metricsSession.getNumberOfEventsFor(appnavne[0]) `should be equal to` 1
        metricsSession.getNumberOfEventsFor(appnavne[1]) `should be equal to` 1
    }

    private fun initMetricsSession(metricsProbe: CacheCountingMetricsProbe, eventType: EventType): CacheCountingMetricsSession {
        val metricsSession = CacheCountingMetricsSession(eventType)
        `Sorg for at metrics session trigges`(metricsProbe, metricsSession, eventType)
        return metricsSession
    }

    private fun `Sorg for at metrics session trigges`(metricsProbe: CacheCountingMetricsProbe, metricsSession: CacheCountingMetricsSession, eventType: EventType) {
        val slot = slot<suspend CacheCountingMetricsSession.() -> Unit>()
        coEvery {
            metricsProbe.runWithMetrics(eventType, capture(slot))
        } coAnswers {
            slot.captured.invoke(metricsSession)
            metricsSession
        }
    }

    fun createProducers(vararg producerNames: String): List<Producer> {
        return producerNames.map { producerName ->
            Producer(commonNamespace, producerName)
        }
    }

    private fun createResult(producers: List<Producer>): List<EventCountForProducer> {
        return producers.map { producer ->
            EventCountForProducer(producer.namespace, producer.appName, 1)
        }
    }

}

