package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.count

import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.internal.periodic.metrics.reporter.common.HandlerConsumer
import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count.CacheCountingMetricsProbe
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count.CacheCountingMetricsSession
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count.CacheEventCounterGCPService
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

internal class EventCounterServiceTestIT {

    private val handlerConsumer = mockk<HandlerConsumer>()

    @Test
    fun `Should count beskjed events`() {
        val systembrukere = listOf("b_systembruker_A", "b_systembruker_B")
        val result = createResult(systembrukere)
        coEvery { handlerConsumer.getEventCount(any()) }.returns(result)

        val metricsProbe = mockk<CacheCountingMetricsProbe>(relaxed = true)
        val metricsSession = initMetricsSession(metricsProbe, EventType.BESKJED)
        val service = CacheEventCounterGCPService(metricsProbe, handlerConsumer)

        runBlocking {
            service.countBeskjeder()
        }

        metricsSession.getTotalNumber() `should be equal to` 2
        metricsSession.getProducers().size `should be equal to` 2
        metricsSession.getNumberOfEventsFor(systembrukere[0]) `should be equal to` 1
        metricsSession.getNumberOfEventsFor(systembrukere[1]) `should be equal to` 1
    }

    @Test
    fun `Should count innboks events`() {
        val systembrukere = listOf("i_systembruker_A", "i_systembruker_B")
        val result = createResult(systembrukere)
        coEvery { handlerConsumer.getEventCount(any()) }.returns(result)

        val metricsProbe = mockk<CacheCountingMetricsProbe>(relaxed = true)
        val metricsSession = initMetricsSession(metricsProbe, EventType.INNBOKS)
        val service = CacheEventCounterGCPService(metricsProbe, handlerConsumer)

        runBlocking {
            service.countInnboksEventer()
        }

        metricsSession.getTotalNumber() `should be equal to` 2
        metricsSession.getProducers().size `should be equal to` 2
        metricsSession.getNumberOfEventsFor(systembrukere[0]) `should be equal to` 1
        metricsSession.getNumberOfEventsFor(systembrukere[1]) `should be equal to` 1
    }

    @Test
    fun `Should count oppgave events`() {
        val systembrukere = listOf("o_systembruker_A", "o_systembruker_B")
        val result = createResult(systembrukere)
        coEvery { handlerConsumer.getEventCount(any()) }.returns(result)

        val metricsProbe = mockk<CacheCountingMetricsProbe>(relaxed = true)
        val metricsSession = initMetricsSession(metricsProbe, EventType.OPPGAVE)
        val service = CacheEventCounterGCPService(metricsProbe, handlerConsumer)

        runBlocking {
            service.countOppgaver()
        }

        metricsSession.getTotalNumber() `should be equal to` 2
        metricsSession.getProducers().size `should be equal to` 2
        metricsSession.getNumberOfEventsFor(systembrukere[0]) `should be equal to` 1
        metricsSession.getNumberOfEventsFor(systembrukere[1]) `should be equal to` 1
    }

    @Test
    fun `Should count done events`() {
        val systembrukere = listOf("d_systembruker_A", "d_systembruker_B")
        val result = createResult(systembrukere)
        coEvery { handlerConsumer.getEventCount(any()) }.returns(result)

        val metricsProbe = mockk<CacheCountingMetricsProbe>(relaxed = true)
        val metricsSession = initMetricsSession(metricsProbe, EventType.DONE)
        val service = CacheEventCounterGCPService(metricsProbe, handlerConsumer)

        runBlocking {
            service.countDoneEvents()
        }

        metricsSession.getTotalNumber() `should be equal to` 2
        metricsSession.getProducers().size `should be equal to` 2
        metricsSession.getNumberOfEventsFor(systembrukere[0]) `should be equal to` 1
        metricsSession.getNumberOfEventsFor(systembrukere[1]) `should be equal to` 1
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

    private fun createResult(systembrukere: List<String>): Map<String, Int> {
        val result = mutableMapOf<String, Int>()

        systembrukere.forEach { systembruker ->
            result.put(systembruker, 1)
        }
        return result
    }

}
