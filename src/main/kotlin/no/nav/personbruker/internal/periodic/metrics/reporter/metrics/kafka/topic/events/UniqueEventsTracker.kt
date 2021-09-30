package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.events

import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.UniqueKafkaEventIdentifier

class UniqueEventsTracker {

    private val perProducerMap = HashMap<String, PerProducerTracker>()

    private var uniqueEventsVar: Int = 0

    val uniqueEvents: Int get() = uniqueEventsVar

    fun addEvent(eventIdentifier: UniqueKafkaEventIdentifier): Boolean {
        return if (perProducerMap.containsKey(eventIdentifier.appnavn)) {
            val isUnique = perProducerMap[eventIdentifier.appnavn]!!.addEvent(eventIdentifier)

            if (isUnique) {
                uniqueEventsVar++
            }

            isUnique
        } else {
            perProducerMap[eventIdentifier.appnavn] = PerProducerTracker(eventIdentifier)
            uniqueEventsVar++
            true
        }
    }
}