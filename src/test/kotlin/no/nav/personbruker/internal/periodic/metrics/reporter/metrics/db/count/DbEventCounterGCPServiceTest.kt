package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.db.count

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.internal.periodic.metrics.reporter.common.HandlerConsumer
import no.nav.personbruker.internal.periodic.metrics.reporter.common.`with message containing`
import no.nav.personbruker.internal.periodic.metrics.reporter.common.exceptions.CountException
import org.amshove.kluent.`should throw`
import org.amshove.kluent.invoking
import org.junit.jupiter.api.Test

internal class DbEventCounterGCPServiceTest {

    private val metricsProbe: DbCountingMetricsProbe = mockk(relaxed = true)
    private val handlerConsumer = mockk<HandlerConsumer>(relaxed = true)

    private val dbEventCounterService = DbEventCounterGCPService(metricsProbe, handlerConsumer)

    @Test
    internal fun `Should handle exceptions and rethrow as internal exception`() {
        val simulatedException = Exception("Simulated error in a test")
        coEvery { metricsProbe.runWithMetrics(any(), any()) } throws simulatedException

        invoking {
            runBlocking {
                dbEventCounterService.countBeskjeder()
            }
        } `should throw` CountException::class `with message containing` "beskjed"

        invoking {
            runBlocking {
                dbEventCounterService.countDoneEvents()
            }
        } `should throw` CountException::class `with message containing` "done"

        invoking {
            runBlocking {
                dbEventCounterService.countInnboksEventer()
            }
        } `should throw` CountException::class `with message containing` "innboks"

        invoking {
            runBlocking {
                dbEventCounterService.countOppgaver()
            }
        } `should throw` CountException::class `with message containing` "oppgave"

        invoking {
            runBlocking {
                dbEventCounterService.countStatusoppdateringer()
            }
        } `should throw` CountException::class `with message containing` "statusoppdatering"

    }

}
