package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic

import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.KafkaEventIdentifier
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should not be equal to`
import org.junit.jupiter.api.Test

internal class TopicMetricsSessionTest {

    val produsent1 = "p-1"
    val produsent2 = "p-2"
    val produsent3 = "p-3"

    @Test
    fun `Skal summere opp eventer riktig i hver kategori per produsent`() {
        val metricsSession = createAndInitiateBeskjedMetricsSession()

        metricsSession.getNumberOfEvents() `should be equal to` 1 + 2 + 3
        metricsSession.getNumberOfEventsForProducer(produsent1) `should be equal to` 1
        metricsSession.getNumberOfEventsForProducer(produsent2) `should be equal to` 2
        metricsSession.getNumberOfEventsForProducer(produsent3) `should be equal to` 3

        metricsSession.getProducersWithEvents().size `should be equal to` 3
    }

    @Test
    fun `Should copy state from one session to another`() {
        val originalMetricsSession = createAndInitiateBeskjedMetricsSession()

        val other = TopicMetricsSession(originalMetricsSession)

        other.getNumberOfEvents() `should be equal to` originalMetricsSession.getNumberOfEvents()
        other.getNumberOfEventsForProducer(produsent1) `should be equal to` originalMetricsSession.getNumberOfEventsForProducer(produsent1)
        other.getNumberOfEventsForProducer(produsent2) `should be equal to` originalMetricsSession.getNumberOfEventsForProducer(produsent2)
        other.getNumberOfEventsForProducer(produsent3) `should be equal to` originalMetricsSession.getNumberOfEventsForProducer(produsent3)

        other.getProducersWithEvents().size `should be equal to` 3

        other.getProcessingTime() `should not be equal to` originalMetricsSession.getProcessingTime()
    }

    @Test
    fun `Should copy state from one session to another and allow for further counting`() {
        val originalMetricsSession = createAndInitiateBeskjedMetricsSession()

        val other = TopicMetricsSession(originalMetricsSession)

        val produsent1Event2 = KafkaEventIdentifier("b-2", produsent1)
        val produsent2Event3 = KafkaEventIdentifier("b-13", produsent2)
        val produsent2Event4 = KafkaEventIdentifier("b-14", produsent2)

        other.countEvent(produsent1Event2)
        other.countEvent(produsent2Event3)
        other.countEvent(produsent2Event4)

        other.getNumberOfEvents() `should be equal to` 6 + 3
        other.getNumberOfEventsForProducer(produsent1) `should be equal to` 1 + 1
        other.getNumberOfEventsForProducer(produsent2) `should be equal to` 2 + 2
        other.getNumberOfEventsForProducer(produsent3) `should be equal to` 3 + 0

        other.getProducersWithEvents().size `should be equal to` 3
    }

    private fun createAndInitiateBeskjedMetricsSession(): TopicMetricsSession {

        val metricsSession = TopicMetricsSession(EventType.BESKJED_INTERN)

        val produsent1Event1 = KafkaEventIdentifier("b-1", produsent1)
        val produsent2Event1 = KafkaEventIdentifier("b-11", produsent2)
        val produsent2Event2 = KafkaEventIdentifier("b-12", produsent2)
        val produsent3Event1 = KafkaEventIdentifier("b-21", produsent3)
        val produsent3Event2 = KafkaEventIdentifier("b-22", produsent3)
        val produsent3Event3 = KafkaEventIdentifier("b-23", produsent3)


        metricsSession.countEvent(produsent1Event1)

        metricsSession.countEvent(produsent2Event1)
        metricsSession.countEvent(produsent2Event2)

        metricsSession.countEvent(produsent3Event1)
        metricsSession.countEvent(produsent3Event2)
        metricsSession.countEvent(produsent3Event3)

        metricsSession.calculateProcessingTime()

        return metricsSession
    }

}
