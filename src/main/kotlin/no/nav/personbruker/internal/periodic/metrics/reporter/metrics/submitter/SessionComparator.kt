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
    private val internalEventTypes = listOf(EventType.BESKJED_INTERN, EventType.INNBOKS_INTERN, EventType.STATUSOPPDATERING_INTERN, EventType.DONE_INTERN, EventType.OPPGAVE_INTERN, EventType.FEILRESPONS)

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
        return if(internalEventTypes.contains(eventType)) {
            topic.getEventTypesWithSession().contains(eventType)
        } else {
            topic.getEventTypesWithSession().contains(eventType) && cache.getEventTypesWithSession().contains(eventType)
        }
    }

    private fun logWarningWithInfoAboutWhatSourcesWasMissingTheEventType(eventType: EventType) {
        when {
            topic.getEventTypesWithSession().contains(eventType) -> {
                val numberOfEvents = topic.getForType(eventType).getNumberOfUniqueEvents()
                log.warn("Eventtypen '$eventType' ble kun telt for topic, og ikke i cache. Fant $numberOfEvents eventer.")

            }
            cache.getEventTypesWithSession().contains(eventType) -> {
                val numberOfEvents = cache.getForType(eventType).getNumberOfUniqueEvents()
                log.warn("Eventtypen '$eventType' ble kun telt for cache, og ikke på topic. Fant $numberOfEvents eventer.")
            }
        }
    }

    fun eventTypesWithSessionFromBothSources(): List<EventType> {
        return eventTypesInBothSources
    }
}
