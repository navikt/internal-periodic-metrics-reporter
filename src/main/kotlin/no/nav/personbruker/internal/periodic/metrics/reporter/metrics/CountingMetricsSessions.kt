package no.nav.personbruker.internal.periodic.metrics.reporter.metrics

import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count.CacheCountingMetricsSession
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.TopicMetricsSession

class CountingMetricsSessions<T : CountingMetricsSession> {

    private val sessions = mutableMapOf<EventType, T>()

    fun put(eventType: EventType, session: T) {
        sessions[eventType] = session
    }

    fun getEventTypesWithSession(): Set<EventType> {
        return sessions.keys
    }

    fun getForType(eventType: EventType): T {
        return sessions[eventType]
            ?: throw Exception("Det finnes ingen sesjon for '$eventType'.")
    }

    override fun toString(): String {
        return "CountingMetricsSessions(sessions=$sessions)"
    }

}

interface CountingMetricsSession {
    fun getNumberOfEvents(): Int
}

typealias TopicMetricsSessions = CountingMetricsSessions<TopicMetricsSession>
typealias CacheCountingMetricsSessions = CountingMetricsSessions<CacheCountingMetricsSession>
