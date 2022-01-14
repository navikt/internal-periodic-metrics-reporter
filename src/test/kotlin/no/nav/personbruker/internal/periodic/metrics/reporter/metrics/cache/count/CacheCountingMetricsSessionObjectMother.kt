package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count

import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.EventCountForProducer
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.Producer

object CacheCountingMetricsSessionObjectMother {

    private const val defaultNamespace = "producerNamespace"

    fun giveMeBeskjedInternSessionWithOneCountedEvent(): CacheCountingMetricsSession {
        val beskjedInternSession = CacheCountingMetricsSession(EventType.BESKJED_INTERN)
        beskjedInternSession.addEventsByProducer(createCount("produsent1", 1))
        return beskjedInternSession
    }

    fun giveMeBeskjedInternSessionWithTwoCountedEvent(): CacheCountingMetricsSession {
        val beskjedInternSession = CacheCountingMetricsSession(EventType.BESKJED_INTERN)
        beskjedInternSession.addEventsByProducer(createCount("produsent1", 2))
        return beskjedInternSession
    }

    fun giveMeDoneInternSessionWithTwoCountedEvents(): CacheCountingMetricsSession {
        val doneInternSession = CacheCountingMetricsSession(EventType.DONE_INTERN)
        doneInternSession.addEventsByProducer(createCount("produsent2", 2))
        return doneInternSession
    }

    fun giveMeInnboksInternSessionWithThreeCountedEvents(): CacheCountingMetricsSession {
        val innboksInternSession = CacheCountingMetricsSession(EventType.INNBOKS_INTERN)
        innboksInternSession.addEventsByProducer(createCount("produsent3", 3))
        return innboksInternSession
    }

    fun giveMeOppgaveInternSessionWithFourCountedEvents(): CacheCountingMetricsSession {
        val oppgaveInternSession = CacheCountingMetricsSession(EventType.OPPGAVE_INTERN)
        oppgaveInternSession.addEventsByProducer(createCount("produsent4", 4))
        return oppgaveInternSession
    }

    fun giveMeStatusoppdateringInternSessionWithFourCountedEvents(): CacheCountingMetricsSession {
        val statusoppdateringInternSession = CacheCountingMetricsSession(EventType.STATUSOPPDATERING_INTERN)
        statusoppdateringInternSession.addEventsByProducer(createCount("produsent5", 4))
        return statusoppdateringInternSession
    }

    fun giveMeACacheSessionWithConfiguration(type: EventType, config: Map<Producer, Int>): CacheCountingMetricsSession {
        val session = CacheCountingMetricsSession(type)

        val counts = config.entries.map { (producer, count) ->
            EventCountForProducer(producer.namespace, producer.appName, count)
        }

        session.addEventsByProducer(counts)

        return session
    }

    private fun createCount(producerAppName: String, count: Int) = listOf(EventCountForProducer(defaultNamespace, producerAppName, count))
}
