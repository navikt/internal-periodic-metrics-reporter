package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.db.count

import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import org.slf4j.LoggerFactory

class DbCountingMetricsProbe {

    private val log = LoggerFactory.getLogger(DbCountingMetricsProbe::class.java)

    suspend fun runWithMetrics(eventType: EventType, block: suspend DbCountingMetricsSession.() -> Unit): DbCountingMetricsSession {
        val session = DbCountingMetricsSession(eventType)
        block.invoke(session)
        session.calculateProcessingTime()
        return session
    }

}
