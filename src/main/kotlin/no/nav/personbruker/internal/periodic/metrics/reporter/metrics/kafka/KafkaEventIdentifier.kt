package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka

data class KafkaEventIdentifier(val eventId: String, val appnavn: String, val fodselsnummer: String) {

    companion object {

        private const val dummyFnr = "000"

        fun createInvalidEvent(): KafkaEventIdentifier {
            return KafkaEventIdentifier(
                    "undeserializableEvent",
                    "unknownProducer",
                    dummyFnr
            )
        }
    }

    override fun toString(): String {
        return "UniqueKafkaEventIdentifier(eventId='$eventId', appnavn='$appnavn', fodselsnummer='***')"
    }

}
