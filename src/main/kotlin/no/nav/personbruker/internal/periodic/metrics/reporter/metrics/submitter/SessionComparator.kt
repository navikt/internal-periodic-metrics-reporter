package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.submitter

import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.CountingMetricsSessions
import org.slf4j.LoggerFactory

class SessionComparator(
        val topic: CountingMetricsSessions,
        val cache: CountingMetricsSessions
) {

    private val log = LoggerFactory.getLogger(SessionComparator::class.java)
    private val eventTypesInBothSources = mutableListOf<EventType>()

    init {
        EventType.values().forEach { eventType ->
            if (isPresentInBothSources(eventType)) {
                eventTypesInBothSources.add(eventType)
            } else {
                logWarningWithInfoAboutWhatSourcesWasMissingTheEventType(eventType)
            }
        }
    }

    private fun isPresentInBothSources(eventType: EventType): Boolean {
        return topic.getEventTypesWithSession().contains(eventType) && cache.getEventTypesWithSession().contains(eventType)
    }

    private fun logWarningWithInfoAboutWhatSourcesWasMissingTheEventType(eventType: EventType) {
        if (topic.getEventTypesWithSession().contains(eventType)) {
            val numberOfEvents = topic.getForType(eventType).getNumberOfEvents()
            log.warn("Eventtypen '$eventType' ble kun telt for topic, og ikke i cache. Fant $numberOfEvents eventer.")

        } else if (cache.getEventTypesWithSession().contains(eventType)) {
            val numberOfEvents = cache.getForType(eventType).getNumberOfEvents()
            log.warn("Eventtypen '$eventType' ble kun telt for cache, og ikke på topic. Fant $numberOfEvents eventer.")
        }
    }

    fun eventTypesWithSessionFromBothSources(): List<EventType> {
        return eventTypesInBothSources
    }
}
