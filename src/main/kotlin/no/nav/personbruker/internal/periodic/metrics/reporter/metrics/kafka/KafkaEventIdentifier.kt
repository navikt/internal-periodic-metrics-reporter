package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka

data class KafkaEventIdentifier(
    val eventId: String,
    val namespace: String,
    val appnavn: String
) {

    companion object {

        fun createInvalidEvent(): KafkaEventIdentifier {
            return KafkaEventIdentifier(
                    "undeserializableEvent",
                    "unknownProducerNamespace",
                    "unknownProducerAppName"
            )
        }
    }

    override fun toString(): String {
        return "UniqueKafkaEventIdentifier(eventId='$eventId', namespace='$namespace', appnavn='$appnavn')"
    }

}
