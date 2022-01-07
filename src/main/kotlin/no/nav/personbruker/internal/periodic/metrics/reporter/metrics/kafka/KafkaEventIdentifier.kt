package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka

data class KafkaEventIdentifier(val eventId: String, val appnavn: String) {

    companion object {

        fun createInvalidEvent(): KafkaEventIdentifier {
            return KafkaEventIdentifier(
                    "undeserializableEvent",
                    "unknownProducer"
            )
        }
    }

    override fun toString(): String {
        return "UniqueKafkaEventIdentifier(eventId='$eventId', appnavn='$appnavn)"
    }

}
