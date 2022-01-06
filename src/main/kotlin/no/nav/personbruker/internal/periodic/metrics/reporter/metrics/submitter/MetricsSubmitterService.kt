package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.submitter

import no.nav.brukernotifikasjon.schemas.internal.NokkelIntern
import no.nav.personbruker.internal.periodic.metrics.reporter.common.exceptions.CountException
import no.nav.personbruker.internal.periodic.metrics.reporter.common.exceptions.MetricsReportingException
import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.CountingMetricsSession
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.CountingMetricsSessions
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count.CacheCountingMetricsSession
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count.CacheEventCounterService
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count.CacheMetricsReporter
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.TopicEventCounterAivenService
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.TopicMetricsReporter
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.TopicMetricsSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MetricsSubmitterService(
        private val cacheEventCounterService: CacheEventCounterService,
        private val topicEventCounterServiceAiven: TopicEventCounterAivenService<NokkelIntern>,
        private val cacheMetricsReporter: CacheMetricsReporter,
        private val kafkaMetricsReporter: TopicMetricsReporter
) {

    private val log: Logger = LoggerFactory.getLogger(MetricsSubmitterService::class.java)

    private val lastReportedNumberOfKafkaEvents = HashMap<EventType, Int>()

    suspend fun submitMetrics() {
        try {
            val topicSessionsAiven = topicEventCounterServiceAiven.countAllEventTypesAsync()
            val cacheSessions = cacheEventCounterService.countAllEventTypesAsync()

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

        if (countedMoreKafkaEventsThanLastCount(kafkaEventSession, eventType)) {
            val cacheSession = cacheSessions.getForType(eventType)
            cacheMetricsReporter.report(cacheSession as CacheCountingMetricsSession)
            kafkaMetricsReporter.report(kafkaEventSession as TopicMetricsSession)
            lastReportedNumberOfKafkaEvents[eventType] = kafkaEventSession.getNumberOfEvents()
        } else if (!currentAndLastCountWasZero(kafkaEventSession, eventType)) {
            val currentCount = kafkaEventSession.getNumberOfEvents()
            val previousCount = lastReportedNumberOfKafkaEvents.getOrDefault(eventType, 0)
            val msg = "Det har oppstått en tellefeil, rapporterer derfor ikke nye $eventType-metrikker. " +
                    "Antall unike eventer ved forrige rapportering $previousCount, antall telt nå $currentCount."
            log.warn(msg)
        }
    }

    private fun countedMoreKafkaEventsThanLastCount(session: CountingMetricsSession, eventType: EventType): Boolean {
        val currentCount = session.getNumberOfEvents()
        return currentCount > 0 && currentCount >= lastReportedNumberOfKafkaEvents.getOrDefault(eventType, 0)
    }

    private fun currentAndLastCountWasZero(session: CountingMetricsSession, eventType: EventType): Boolean {
        val currentCount = session.getNumberOfEvents()
        return currentCount == 0 && lastReportedNumberOfKafkaEvents.getOrDefault(eventType, 0) == 0
    }
}
