package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.submitter

import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.personbruker.internal.periodic.metrics.reporter.common.exceptions.CountException
import no.nav.personbruker.internal.periodic.metrics.reporter.common.exceptions.MetricsReportingException
import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.CountingMetricsSession
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.CountingMetricsSessions
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count.CacheCountingMetricsSession
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count.CacheEventCounterGCPService
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count.CacheMetricsReporter
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.TopicEventCounterAivenService
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.TopicMetricsReporter
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.TopicMetricsSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MetricsSubmitterService(
        private val cacheEventCounterGCPService: CacheEventCounterGCPService,
        private val topicEventCounterServiceAiven: TopicEventCounterAivenService<Nokkel>,
        private val cacheMetricsReporter: CacheMetricsReporter,
        private val kafkaMetricsReporter: TopicMetricsReporter
) {

    private val log: Logger = LoggerFactory.getLogger(MetricsSubmitterService::class.java)

    private val lastReportedUniqueKafkaEvents = HashMap<EventType, Int>()

    suspend fun submitMetrics() {
        try {
            val topicSessionsAiven = topicEventCounterServiceAiven.countAllEventTypesAsync()
            val cacheSessions = cacheEventCounterGCPService.countAllEventTypesAsync()

            val sessionComparatorAiven = SessionComparator(topicSessionsAiven, cacheSessions)

            sessionComparatorAiven.eventTypesWithSessionFromBothSources().forEach { eventType ->
                reportMetricsByEventType(topicSessionsAiven, cacheSessions, eventType)
            }

        } catch (e: CountException) {
            log.warn("Klarte ikke å telle eventer", e)

        } catch (e: MetricsReportingException) {
            log.warn("Klarte ikke å rapportere metrikker", e)

        } catch (e: Exception) {
            log.error("En uventet feil oppstod, klarte ikke å telle og/eller rapportere metrikker.", e)
        }
    }

    private suspend fun reportMetricsByEventType(
            topicSessions: CountingMetricsSessions,
            cacheSessions: CountingMetricsSessions,
            eventType: EventType
    ) {
        val kafkaEventSession = topicSessions.getForType(eventType)

        if (EventType.FEILRESPONS == eventType) {
            kafkaMetricsReporter.report(kafkaEventSession as TopicMetricsSession)
            lastReportedUniqueKafkaEvents[eventType] = kafkaEventSession.getNumberOfUniqueEvents()
        } else if (countedMoreKafkaEventsThanLastCount(kafkaEventSession, eventType)) {
            val cacheSession = cacheSessions.getForType(eventType)
            cacheMetricsReporter.report(cacheSession as CacheCountingMetricsSession)
            kafkaMetricsReporter.report(kafkaEventSession as TopicMetricsSession)
            lastReportedUniqueKafkaEvents[eventType] = kafkaEventSession.getNumberOfUniqueEvents()
        } else if (!currentAndLastCountWasZero(kafkaEventSession, eventType)) {
            val currentCount = kafkaEventSession.getNumberOfUniqueEvents()
            val previousCount = lastReportedUniqueKafkaEvents.getOrDefault(eventType, 0)
            val msg = "Det har oppstått en tellefeil, rapporterer derfor ikke nye $eventType-metrikker. " +
                    "Antall unike eventer ved forrige rapportering $previousCount, antall telt nå $currentCount."
            log.warn(msg)
        }
    }

    private fun countedMoreKafkaEventsThanLastCount(session: CountingMetricsSession, eventType: EventType): Boolean {
        val currentCount = session.getNumberOfUniqueEvents()
        return currentCount > 0 && currentCount >= lastReportedUniqueKafkaEvents.getOrDefault(eventType, 0)
    }

    private fun currentAndLastCountWasZero(session: CountingMetricsSession, eventType: EventType): Boolean {
        val currentCount = session.getNumberOfUniqueEvents()
        return currentCount == 0 && lastReportedUniqueKafkaEvents.getOrDefault(eventType, 0) == 0
    }
}
