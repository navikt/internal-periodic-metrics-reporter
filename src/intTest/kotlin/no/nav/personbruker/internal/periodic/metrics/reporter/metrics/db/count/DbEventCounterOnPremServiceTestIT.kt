package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.db.count

import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.internal.periodic.metrics.reporter.common.getClientWith
import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.config.HttpClientBuilder
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test
import java.net.URL

internal class DbEventCounterOnPremServiceTestIT {
//TODO skriv om til å teste gcp versjon som henter data fra handler

    private val pathToEndpoint = URL("https://event-handler/systemuser")

    @Test
    fun `Should count beskjed events`() {
      //val client = HttpClientBuilder.build()
        val systembrukere = listOf("b_systembruker_A", "b_systembruker_B")
        val result = createResult(systembrukere)
/*
        runBlocking {
            coEvery {
                client.get(any())
            }.returns(result)
        }


 */
        val client = getClientWith(result)
        val metricsProbe = mockk<DbCountingMetricsProbe>(relaxed = true)
        val metricsSession = initMetricsSession(metricsProbe, EventType.BESKJED)
        val service = DbEventCounterGCPService(metricsProbe, pathToEndpoint, client)

        runBlocking {
            service.countBeskjeder()
        }

        metricsSession.getTotalNumber() `should be equal to` 2
        metricsSession.getProducers().size `should be equal to` 2
        metricsSession.getNumberOfEventsFor(systembrukere[0]) `should be equal to` 1
        metricsSession.getNumberOfEventsFor(systembrukere[1]) `should be equal to` 1
    }

    /*
        @Test
        fun `Should count innboks events`() {
            val innboksEventer = createInnboksEventer()
            val metricsProbe = mockk<DbCountingMetricsProbe>(relaxed = true)
            val metricsSession = initMetricsSession(metricsProbe, EventType.INNBOKS)
            val service = DbEventCounterOnPremService(metricsProbe, repository)

            runBlocking {
                service.countInnboksEventer()
            }

            metricsSession.getTotalNumber() `should be equal to` 2
            metricsSession.getProducers().size `should be equal to` 2
            metricsSession.getNumberOfEventsFor(innboksEventer[0].systembruker) `should be equal to` 1
            metricsSession.getNumberOfEventsFor(innboksEventer[1].systembruker) `should be equal to` 1
        }

        @Test
        fun `Should count oppgave events`() {
            val oppgaver = createOppgaveEventer()
            val metricsProbe = mockk<DbCountingMetricsProbe>(relaxed = true)
            val metricsSession = initMetricsSession(metricsProbe, EventType.OPPGAVE)
            val service = DbEventCounterOnPremService(metricsProbe, repository)

            runBlocking {
                service.countOppgaver()
            }

            metricsSession.getTotalNumber() `should be equal to` 2
            metricsSession.getProducers().size `should be equal to` 2
            metricsSession.getNumberOfEventsFor(oppgaver[0].systembruker) `should be equal to` 1
            metricsSession.getNumberOfEventsFor(oppgaver[1].systembruker) `should be equal to` 1
        }

        @Test
        fun `Should count done events from the wait table, and include brukernotifikasjoner marked as inactive (done)`() {
            createBeskjedEventer()
            createInnboksEventer()
            createOppgaveEventer()
            val doneEventer = createDoneEventInWaitingTable()

            val metricsProbe = mockk<DbCountingMetricsProbe>(relaxed = true)
            val metricsSession = initMetricsSession(metricsProbe, EventType.DONE)
            val service = DbEventCounterOnPremService(metricsProbe, repository)

            runBlocking {
                service.countDoneEvents()
            }

            metricsSession.getTotalNumber() `should be equal to` 4
            metricsSession.getProducers().size `should be equal to` 2
            metricsSession.getNumberOfEventsFor("dummySystembruker") `should be equal to` 3
            metricsSession.getNumberOfEventsFor(doneEventer[0].systembruker) `should be equal to` 1
        }
    */
    private fun initMetricsSession(metricsProbe: DbCountingMetricsProbe, eventType: EventType): DbCountingMetricsSession {
        val metricsSession = DbCountingMetricsSession(eventType)
        `Sorg for at metrics session trigges`(metricsProbe, metricsSession, eventType)
        return metricsSession
    }

    private fun `Sorg for at metrics session trigges`(metricsProbe: DbCountingMetricsProbe, metricsSession: DbCountingMetricsSession, eventType: EventType) {
        val slot = slot<suspend DbCountingMetricsSession.() -> Unit>()
        coEvery {
            metricsProbe.runWithMetrics(eventType, capture(slot))
        } coAnswers {
            slot.captured.invoke(metricsSession)
            metricsSession
        }
    }

    private fun createResult(systembrukere: List<String>): Map<String, Int> {
        val result = mutableMapOf<String, Int>()

        systembrukere.forEach { systembruker ->
            result.put(systembruker, 1)
        }
        return result
    }
/*
    private fun createInnboksEventer(): List<Innboks> {
        val innboksEventer = listOf(
                InnboksObjectMother.giveMeAktivInnboks("213", "678", "systembrukerI"),
                InnboksObjectMother.giveMeInaktivInnboks()
        )
        runBlocking {
            database.dbQuery {
                createInnboksEventer(innboksEventer)
            }
        }
        return innboksEventer
    }

    private fun createOppgaveEventer(): List<Oppgave> {
        val oppgaver = listOf(
                OppgaveObjectMother.giveMeAktivOppgave("132", "789", "systembrukerO"),
                OppgaveObjectMother.giveMeInaktivOppgave()
        )
        runBlocking {
            database.dbQuery {
                createOppgaver(oppgaver)
            }
        }
        return oppgaver
    }

    private fun createStatusoppdateringEventer(): List<Statusoppdatering> {
        val statusoppdateringer = listOf(
            StatusoppdateringObjectMother.giveMeStatusoppdatering("132", "789"),
            StatusoppdateringObjectMother.giveMeStatusoppdatering("321", "789")
        )
        runBlocking {
            database.dbQuery {
                createStatusoppdateringer(statusoppdateringer)
            }
        }
        return statusoppdateringer
    }

    private fun createDoneEventInWaitingTable(): List<Done> {
        val doneEventer = listOf(
                DoneObjectMother.giveMeDone("e-2", "systembrukerD")
        )
        runBlocking {
            database.dbQuery {
                createDoneEvents(doneEventer)
            }
        }
        return doneEventer
    }

 */

}

