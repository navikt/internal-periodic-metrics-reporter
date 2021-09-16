package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import org.amshove.kluent.`should be greater than`
import org.junit.jupiter.api.Test

internal class CacheCountingMetricsProbeTest {

    @Test
    internal fun `Should calculate processing time`() {
        val probe = CacheCountingMetricsProbe()
        val minimumProcessingTimeInMs: Long = 500
        val minimumProcessingTimeInNs: Long = minimumProcessingTimeInMs * 1000000
        val session = runBlocking {
            probe.runWithMetrics(EventType.BESKJED) {
                delay(minimumProcessingTimeInMs)
            }
        }

        session.getProcessingTime() `should be greater than` minimumProcessingTimeInNs
    }

}
