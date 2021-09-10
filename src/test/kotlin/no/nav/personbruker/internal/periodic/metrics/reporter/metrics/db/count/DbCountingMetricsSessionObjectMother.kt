package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.db.count

import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType

object DbCountingMetricsSessionObjectMother {

    fun giveMeBeskjedInternSessionWithOneCountedEvent(): DbCountingMetricsSession {
        val beskjedInternSession = DbCountingMetricsSession(EventType.BESKJED)
        beskjedInternSession.addEventsByProducer(mapOf("produsent1" to 1))
        return beskjedInternSession
    }

    fun giveMeDoneInternSessionWithTwoCountedEvents(): DbCountingMetricsSession {
        val doneInternSession = DbCountingMetricsSession(EventType.DONE)
        doneInternSession.addEventsByProducer(mapOf("produsent2" to 21))
        doneInternSession.addEventsByProducer(mapOf("produsent2" to 22))
        return doneInternSession
    }

    fun giveMeInnboksInternSessionWithThreeCountedEvents(): DbCountingMetricsSession {
        val innboksInternSession = DbCountingMetricsSession(EventType.INNBOKS)
        innboksInternSession.addEventsByProducer(mapOf("produsent3" to 31))
        innboksInternSession.addEventsByProducer(mapOf("produsent3" to 32))
        innboksInternSession.addEventsByProducer(mapOf("produsent3" to 33))
        return innboksInternSession
    }

    fun giveMeOppgaveInternSessionWithFourCountedEvents(): DbCountingMetricsSession {
        val oppgaveInternSession = DbCountingMetricsSession(EventType.OPPGAVE)
        oppgaveInternSession.addEventsByProducer(mapOf("produsent4" to 41))
        oppgaveInternSession.addEventsByProducer(mapOf("produsent4" to 42))
        oppgaveInternSession.addEventsByProducer(mapOf("produsent4" to 43))
        oppgaveInternSession.addEventsByProducer(mapOf("produsent4" to 44))
        return oppgaveInternSession
    }

    fun giveMeStatusoppdateringInternSessionWithFourCountedEvents(): DbCountingMetricsSession {
        val statusoppdateringInternSession = DbCountingMetricsSession(EventType.STATUSOPPDATERING)
        statusoppdateringInternSession.addEventsByProducer(mapOf("produsent5" to 51))
        statusoppdateringInternSession.addEventsByProducer(mapOf("produsent5" to 52))
        statusoppdateringInternSession.addEventsByProducer(mapOf("produsent5" to 53))
        statusoppdateringInternSession.addEventsByProducer(mapOf("produsent5" to 54))
        return statusoppdateringInternSession
    }

}
