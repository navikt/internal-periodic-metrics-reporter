package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka

data class UniqueKafkaEventIdentifier(val eventId: String, val appnavn: String, val fodselsnummer: String) {

    companion object {

        private const val dummyFnr = "000"

        fun createInvalidEvent(): UniqueKafkaEventIdentifier {
            return UniqueKafkaEventIdentifier(
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
