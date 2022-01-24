package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count

import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.EventCountForProducer
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.Producer
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.shouldContainAll
import org.junit.jupiter.api.Test

internal class CacheCountingMetricsSessionTest {

    val produsent1 = Producer("namespace1","produsent1")
    val produsent2 = Producer("namespace2","produsent2")
    val produsent3 = Producer("namespace3","produsent3")
    val produsent1Antall = 1
    val produsent2Antall = 2
    val produsent3Antall = 3
    val producerCounts = listOf(
        EventCountForProducer(produsent1.namespace, produsent1.appName, produsent1Antall),
        EventCountForProducer(produsent2.namespace, produsent2.appName, produsent2Antall),
        EventCountForProducer(produsent3.namespace, produsent3.appName, produsent3Antall)
    )
    val alleProdusenter = listOf(produsent1, produsent2, produsent3)


    @Test
    fun `Skal telle opp riktig totalantall eventer, og rapportere riktig per produsent`() {
        val session = CacheCountingMetricsSession(EventType.BESKJED_INTERN)
        session.addEventsByProducer(producerCounts)

        session.getTotalNumber() `should be equal to` (produsent1Antall + produsent2Antall + produsent3Antall)
        session.getNumberOfEvents() `should be equal to` session.getTotalNumber()

        session.getProducers().size `should be equal to` alleProdusenter.size
        session.getProducers() shouldContainAll alleProdusenter

        session.getNumberOfEventsFor(produsent1) `should be equal to` produsent1Antall
        session.getNumberOfEventsFor(produsent2) `should be equal to` produsent2Antall
        session.getNumberOfEventsFor(produsent3) `should be equal to` produsent3Antall
    }

    @Test
    fun `Skal telle opp riktig totalantall eventer, hvis eventer legges til flere ganger`() {
        val session = CacheCountingMetricsSession(EventType.BESKJED_INTERN)
        session.addEventsByProducer(producerCounts)
        session.addEventsByProducer(producerCounts)

        session.getTotalNumber() `should be equal to` (produsent1Antall + produsent2Antall + produsent3Antall) * 2

        session.getProducers().size `should be equal to` alleProdusenter.size
        session.getProducers() shouldContainAll alleProdusenter

        session.getNumberOfEventsFor(produsent1) `should be equal to` produsent1Antall * 2
        session.getNumberOfEventsFor(produsent2) `should be equal to` produsent2Antall * 2
        session.getNumberOfEventsFor(produsent3) `should be equal to` produsent3Antall * 2
    }

}
