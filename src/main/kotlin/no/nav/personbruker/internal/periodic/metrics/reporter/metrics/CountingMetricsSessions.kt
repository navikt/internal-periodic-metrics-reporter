package no.nav.personbruker.internal.periodic.metrics.reporter.metrics

import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType

class CountingMetricsSessions {

    private val sessions = mutableMapOf<EventType, CountingMetricsSession>()

    fun put(eventType: EventType, session: CountingMetricsSession) {
        sessions[eventType] = session
    }

    fun getEventTypesWithSession(): Set<EventType> {
        return sessions.keys
    }

    fun getForType(eventType: EventType): CountingMetricsSession {
        return sessions[eventType]
            ?: throw Exception("Det finnes ingen sesjon for '$eventType'.")
    }

    override fun toString(): String {
        return "CountingMetricsSessions(sessions=$sessions)"
    }

}

interface CountingMetricsSession {
    fun getNumberOfUniqueEvents(): Int
}
