package no.nav.personbruker.internal.periodic.metrics.reporter.metrics

import no.nav.personbruker.dittnav.common.metrics.MetricsReporter
import no.nav.personbruker.internal.periodic.metrics.reporter.common.exceptions.MetricsReportingException
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count.CacheCountingMetricsSession
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.TopicMetricsSession

class DiscrepancyMetricsReporter(private val metricsReporter: MetricsReporter) {

    suspend fun report(topicSession: TopicMetricsSession, cacheSession: CacheCountingMetricsSession) {
        try {
            reportIfEventsCounted(topicSession, cacheSession)

        } catch (e: Exception) {
            val msg = "Klarte ikke å rapportere topic-metrikker for ${topicSession.eventType.eventType}"
            throw MetricsReportingException(msg, e)
        }
    }

    private suspend fun reportIfEventsCounted(topicSession: TopicMetricsSession, cacheSession: CacheCountingMetricsSession) {
        if (topicSession.getNumberOfEvents() > 0 || cacheSession.getNumberOfEvents() > 0) {
            reportEventDiscrepancy(topicSession, cacheSession)
            reportEventDiscrepancyByProducer(topicSession, cacheSession)
        }
    }

    private suspend fun reportEventDiscrepancy(topicSession: TopicMetricsSession, cacheSession: CacheCountingMetricsSession) {
        val discrepancy = cacheSession.getNumberOfEvents() - topicSession.getNumberOfEvents()
        val eventTypeName = topicSession.eventType.toString()

        reportEvents(discrepancy, eventTypeName, DB_TOPIC_EVENTS_DISCREPANCY)
    }

    private suspend fun reportEventDiscrepancyByProducer(topicSession: TopicMetricsSession, cacheSession: CacheCountingMetricsSession) {
        val producers = (topicSession.getProducersWithEvents() + cacheSession.getProducers()).distinct()

        producers.forEach { producerName ->
            val topicEvents = topicSession.getNumberOfEventsForProducer(producerName)
            val cacheEvents = cacheSession.getNumberOfEventsFor(producerName)

            val discrepancy = cacheEvents - topicEvents

            val eventTypeName = topicSession.eventType.toString()

            reportEvents(discrepancy, eventTypeName, producerName, DB_TOPIC_EVENTS_DISCREPANCY_BY_PRODUCER)
        }
    }

    private suspend fun reportEvents(count: Int, eventType: String, producer: Producer, metricName: String) {
        metricsReporter.registerDataPoint(metricName, counterField(count), createTagMap(eventType, producer))
    }

    private fun counterField(events: Int): Map<String, Int> = listOf("counter" to events).toMap()

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
