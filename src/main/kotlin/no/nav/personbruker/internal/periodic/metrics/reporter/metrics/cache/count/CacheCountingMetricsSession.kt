package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count

import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.CountingMetricsSession

class CacheCountingMetricsSession(val eventType: EventType) : CountingMetricsSession {

    private val cachedEventsByProducer = HashMap<String, Int>(50)

    private val start = System.nanoTime()
    private var processingTime : Long = 0L

    fun addEventsByProducer(eventsByProducer: Map<String, Int>) {
        eventsByProducer.forEach { producer ->
            cachedEventsByProducer[producer.key] = cachedEventsByProducer.getOrDefault(producer.key, 0).plus(producer.value)
        }
    }

    fun getProducers(): Set<String> {
        return cachedEventsByProducer.keys
    }

    fun getNumberOfEventsFor(producer: String): Int {
        return cachedEventsByProducer.getOrDefault(producer, 0)
    }

    fun getTotalNumber(): Int {
        var total = 0
        cachedEventsByProducer.forEach { producer ->
            total += producer.value
        }
        return total
    }

    /**
     * Databasen inneholder kun unike-eventer, derfor er `getTotalNumber == getNumberOfUniqueEvents`.
     */
    override fun getNumberOfEvents(): Int {
        return getTotalNumber()
    }

    override fun toString(): String {
        return """CacheCountingMetricsSession(
|                   eventType=$eventType, 
|                   cachedEventsByProducer=$cachedEventsByProducer
|                 )""".trimMargin()
    }

    fun calculateProcessingTime() {
        processingTime = System.nanoTime() - start
    }

    fun getProcessingTime(): Long {
        return processingTime
    }

}
