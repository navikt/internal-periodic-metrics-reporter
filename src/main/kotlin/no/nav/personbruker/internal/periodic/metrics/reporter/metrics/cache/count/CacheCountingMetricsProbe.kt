package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count

import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import org.slf4j.LoggerFactory

class CacheCountingMetricsProbe {

    private val log = LoggerFactory.getLogger(CacheCountingMetricsProbe::class.java)

    suspend fun runWithMetrics(eventType: EventType, block: suspend CacheCountingMetricsSession.() -> Unit): CacheCountingMetricsSession {
        val session = CacheCountingMetricsSession(eventType)
        block.invoke(session)
        session.calculateProcessingTime()
        return session
    }

}
