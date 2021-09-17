package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count

import no.nav.personbruker.dittnav.common.metrics.MetricsReporter
import no.nav.personbruker.internal.periodic.metrics.reporter.common.exceptions.MetricsReportingException
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.DB_COUNT_PROCESSING_TIME
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.DB_TOTAL_EVENTS_IN_CACHE
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.DB_TOTAL_EVENTS_IN_CACHE_BY_PRODUCER
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.PrometheusMetricsCollector
import org.slf4j.LoggerFactory

class CacheMetricsReporter(private val metricsReporter: MetricsReporter) {

    private val log = LoggerFactory.getLogger(CacheMetricsReporter::class.java)

    suspend fun report(session: CacheCountingMetricsSession) {
        try {
            reportIfEventsCounted(session)

        } catch (e: Exception) {
            val msg = "Klarte ikke å rapportere cache-metrikker for ${session.eventType.eventType}"
            throw MetricsReportingException(msg, e)
        }
    }

    private suspend fun reportIfEventsCounted(session: CacheCountingMetricsSession) {
        if (session.getProducers().isNotEmpty()) {
            reportTotalEvents(session)
            reportTotalEventsByProducer(session)
            reportTimeUsed(session)

        } else {
            log.info("Ingen eventer ble funnet i cache for ${session.eventType}.")
        }
    }

    private suspend fun reportTotalEvents(session: CacheCountingMetricsSession) {
        val numberOfUniqueEvents = session.getTotalNumber()
        val eventTypeName = session.eventType.toString()

        reportEvents(numberOfUniqueEvents, eventTypeName, DB_TOTAL_EVENTS_IN_CACHE)
        PrometheusMetricsCollector.registerTotalNumberOfEventsInCache(numberOfUniqueEvents, session.eventType)
    }

    private suspend fun reportTotalEventsByProducer(session: CacheCountingMetricsSession) {
        session.getProducers().forEach { producerName ->
            val numberOfEvents = session.getNumberOfEventsFor(producerName)
            val eventTypeName = session.eventType.toString()

            reportEvents(numberOfEvents, eventTypeName, producerName, DB_TOTAL_EVENTS_IN_CACHE_BY_PRODUCER)
            PrometheusMetricsCollector.registerTotalNumberOfEventsInCacheByProducer(
                    numberOfEvents,
                    session.eventType,
                    producerName
            )
        }
    }

    private suspend fun reportTimeUsed(session: CacheCountingMetricsSession) {
        reportProcessingTimeEvent(session)
        PrometheusMetricsCollector.registerProcessingTimeInCache(session.getProcessingTime(), session.eventType)
    }

    private suspend fun reportEvents(count: Int, eventType: String, producerAlias: String, metricName: String) {
        metricsReporter.registerDataPoint(metricName, counterField(count), createTagMap(eventType, producerAlias))
    }

    private fun counterField(events: Int): Map<String, Int> = listOf("counter" to events).toMap()

    private fun counterField(events: Long): Map<String, Long> = listOf("counter" to events).toMap()

    private fun createTagMap(eventType: String, producer: String): Map<String, String> =
            listOf("eventType" to eventType, "producer" to producer).toMap()

    private suspend fun reportEvents(count: Int, eventType: String, metricName: String) {
        metricsReporter.registerDataPoint(metricName, counterField(count), createTagMap(eventType))
    }

    private suspend fun reportProcessingTimeEvent(session: CacheCountingMetricsSession) {
        val metricName = DB_COUNT_PROCESSING_TIME
        metricsReporter.registerDataPoint(
                metricName, counterField(session.getProcessingTime()),
                createTagMap(session.eventType.eventType)
        )
    }

    private fun createTagMap(eventType: String): Map<String, String> =
            listOf("eventType" to eventType).toMap()

}
