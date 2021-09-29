package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count

import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType

object CacheCountingMetricsSessionObjectMother {

    fun giveMeBeskjedInternSessionWithOneCountedEvent(): CacheCountingMetricsSession {
        val beskjedInternSession = CacheCountingMetricsSession(EventType.BESKJED_INTERN)
        beskjedInternSession.addEventsByProducer(mapOf("produsent1" to 1))
        return beskjedInternSession
    }

    fun giveMeDoneInternSessionWithTwoCountedEvents(): CacheCountingMetricsSession {
        val doneInternSession = CacheCountingMetricsSession(EventType.DONE_INTERN)
        doneInternSession.addEventsByProducer(mapOf("produsent2" to 21))
        doneInternSession.addEventsByProducer(mapOf("produsent2" to 22))
        return doneInternSession
    }

    fun giveMeInnboksInternSessionWithThreeCountedEvents(): CacheCountingMetricsSession {
        val innboksInternSession = CacheCountingMetricsSession(EventType.INNBOKS_INTERN)
        innboksInternSession.addEventsByProducer(mapOf("produsent3" to 31))
        innboksInternSession.addEventsByProducer(mapOf("produsent3" to 32))
        innboksInternSession.addEventsByProducer(mapOf("produsent3" to 33))
        return innboksInternSession
    }

    fun giveMeOppgaveInternSessionWithFourCountedEvents(): CacheCountingMetricsSession {
        val oppgaveInternSession = CacheCountingMetricsSession(EventType.OPPGAVE_INTERN)
        oppgaveInternSession.addEventsByProducer(mapOf("produsent4" to 41))
        oppgaveInternSession.addEventsByProducer(mapOf("produsent4" to 42))
        oppgaveInternSession.addEventsByProducer(mapOf("produsent4" to 43))
        oppgaveInternSession.addEventsByProducer(mapOf("produsent4" to 44))
        return oppgaveInternSession
    }

    fun giveMeStatusoppdateringInternSessionWithFourCountedEvents(): CacheCountingMetricsSession {
        val statusoppdateringInternSession = CacheCountingMetricsSession(EventType.STATUSOPPDATERING_INTERN)
        statusoppdateringInternSession.addEventsByProducer(mapOf("produsent5" to 51))
        statusoppdateringInternSession.addEventsByProducer(mapOf("produsent5" to 52))
        statusoppdateringInternSession.addEventsByProducer(mapOf("produsent5" to 53))
        statusoppdateringInternSession.addEventsByProducer(mapOf("produsent5" to 54))
        return statusoppdateringInternSession
    }

}
