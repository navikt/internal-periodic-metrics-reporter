package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.internal.periodic.metrics.reporter.common.HandlerConsumer
import no.nav.personbruker.internal.periodic.metrics.reporter.common.`with message containing`
import no.nav.personbruker.internal.periodic.metrics.reporter.common.exceptions.CountException
import org.amshove.kluent.`should throw`
import org.amshove.kluent.invoking
import org.junit.jupiter.api.Test

internal class CacheEventCounterServiceTest {

    private val metricsProbe: CacheCountingMetricsProbe = mockk(relaxed = true)
    private val handlerConsumer = mockk<HandlerConsumer>(relaxed = true)

    private val cacheEventCounterService = CacheEventCounterService(metricsProbe, handlerConsumer)

    @Test
    internal fun `Should handle exceptions and rethrow as internal exception`() {
        val simulatedException = Exception("Simulated error in a test")
        coEvery { metricsProbe.runWithMetrics(any(), any()) } throws simulatedException

        invoking {
            runBlocking {
                cacheEventCounterService.countBeskjeder()
            }
        } `should throw` CountException::class `with message containing` "beskjed_intern"

        invoking {
            runBlocking {
                cacheEventCounterService.countDoneEvents()
            }
        } `should throw` CountException::class `with message containing` "done_intern"

        invoking {
            runBlocking {
                cacheEventCounterService.countInnboksEventer()
            }
        } `should throw` CountException::class `with message containing` "innboks_intern"

        invoking {
            runBlocking {
                cacheEventCounterService.countOppgaver()
            }
        } `should throw` CountException::class `with message containing` "oppgave_intern"

        invoking {
            runBlocking {
                cacheEventCounterService.countStatusoppdateringer()
            }
        } `should throw` CountException::class `with message containing` "statusoppdatering_intern"

    }

}
