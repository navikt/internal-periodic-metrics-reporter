package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic

import no.nav.personbruker.dittnav.common.metrics.MetricsReporter
import no.nav.personbruker.internal.periodic.metrics.reporter.common.exceptions.MetricsReportingException
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.*
import org.slf4j.LoggerFactory

class TopicMetricsReporter(private val metricsReporter: MetricsReporter) {

    private val log = LoggerFactory.getLogger(TopicMetricsReporter::class.java)

    suspend fun report(session: TopicMetricsSession) {
        try {
            reportIfEventsCounted(session)

        } catch (e: Exception) {
            val msg = "Klarte ikke å rapportere topic-metrikker for ${session.eventType.eventType}"
            throw MetricsReportingException(msg, e)
        }
    }

    private suspend fun reportIfEventsCounted(session: TopicMetricsSession) {
        if (session.getNumberOfEvents() > 0) {
            reportNumberOfEvents(session)
            reportNumberOfEventsByProducer(session)
            reportTimeUsed(session)

        } else {
            log.info("Ingen eventer ble funnet på topic for ${session.eventType}.")
        }
    }

    private suspend fun reportNumberOfEvents(session: TopicMetricsSession) {
        val uniqueEvents = session.getNumberOfEvents()
        val eventTypeName = session.eventType.toString()

        reportEvents(uniqueEvents, eventTypeName, KAFKA_TOTAL_EVENTS_ON_TOPIC)
    }

    private suspend fun reportNumberOfEventsByProducer(session: TopicMetricsSession) {
        session.getProducersWithEvents().forEach { producer ->
            val total = session.getNumberOfEventsForProducer(producer)
            val eventTypeName = session.eventType.toString()

            reportEvents(total, eventTypeName, producer, KAFKA_TOTAL_EVENTS_ON_TOPIC_BY_PRODUCER)
        }
    }

    private suspend fun reportTimeUsed(session: TopicMetricsSession) {
        reportProcessingTimeEvent(session)
    }

    private suspend fun reportEvents(count: Int, eventType: String, producer: Producer, metricName: String) {
        metricsReporter.registerDataPoint(metricName, counterField(count), createTagMap(eventType, producer))
    }

    private suspend fun reportProcessingTimeEvent(session: TopicMetricsSession) {
        val metricName = KAFKA_COUNT_PROCESSING_TIME
        metricsReporter.registerDataPoint(
                metricName, counterField(session.getProcessingTime()),
                createTagMap(session.eventType.eventType)
        )
    }

    private fun counterField(events: Int): Map<String, Int> = listOf("counter" to events).toMap()

    private fun counterField(events: Long): Map<String, Long> = listOf("counter" to events).toMap()

    private fun createTagMap(eventType: String, producer: Producer): Map<String, String> =
            listOf(
                "eventType" to eventType,
                "producer" to producer.appName,
                "producerNamespace" to producer.namespace
            ).toMap()

    private suspend fun reportEvents(count: Int, eventType: String, metricName: String) {
        metricsReporter.registerDataPoint(metricName, counterField(count), createTagMap(eventType))
    }

    private fun createTagMap(eventType: String): Map<String, String> =
            listOf("eventType" to eventType).toMap()

}
