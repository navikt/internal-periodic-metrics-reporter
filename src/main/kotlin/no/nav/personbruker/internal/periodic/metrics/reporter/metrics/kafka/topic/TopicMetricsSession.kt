package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic

import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.CountingMetricsSession
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.KafkaEventIdentifier

class TopicMetricsSession : CountingMetricsSession {

    val eventType: EventType

    private var numberOfEventsByProducer: MutableMap<String, Int>

    private val start = System.nanoTime()
    private var processingTime : Long = 0L

    constructor(eventType: EventType) {
        this.eventType = eventType
        this.numberOfEventsByProducer = HashMap(50)
    }

    constructor(previousSession: TopicMetricsSession) {
        this.eventType = previousSession.eventType
        this.numberOfEventsByProducer = previousSession.numberOfEventsByProducer
    }

    fun countEvent(event: KafkaEventIdentifier) {
        val produsent = event.appnavn
        numberOfEventsByProducer[produsent] = numberOfEventsByProducer.getOrDefault(produsent, 0).inc()
    }

    fun getNumberOfEventsForProducer(prodcer: String): Int {
        return numberOfEventsByProducer.getOrDefault(prodcer, 0)
    }

    override fun getNumberOfEvents(): Int {
        return numberOfEventsByProducer.values.sum()
    }

    fun getProducersWithEvents(): Set<String> {
        return numberOfEventsByProducer.keys
    }

    override fun toString(): String {
        return """TopicMetricsSession(
|                   eventType=$eventType, 
|                   unique=${getNumberOfEvents()}
|                   total=$numberOfEventsByProducer, 
|                 )""".trimMargin()
    }

    fun calculateProcessingTime() {
        processingTime = System.nanoTime() - start
    }

    fun getProcessingTime(): Long {
        return processingTime
    }
}
