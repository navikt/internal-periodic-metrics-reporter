package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count

import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.EventCountForProducer
import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.CountingMetricsSession
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.Producer

class CacheCountingMetricsSession(val eventType: EventType) : CountingMetricsSession {

    private val cachedEventsByProducer = HashMap<Producer, Int>(50)

    private val start = System.nanoTime()
    private var processingTime : Long = 0L

    fun addEventsByProducer(eventsByProducer: List<EventCountForProducer>) {
        eventsByProducer.forEach { producerCount ->
            cachedEventsByProducer.compute(producerCount.producer) { _, currentCount ->
                if (currentCount == null) {
                    producerCount.count
                } else {
                    currentCount + producerCount.count
                }
            }
        }
    }

    fun getProducers(): Set<Producer> {
        return cachedEventsByProducer.keys
    }

    fun getNumberOfEventsFor(producer: Producer): Int {
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
