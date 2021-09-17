package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic

import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.UniqueKafkaEventIdentifier
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory

object UniqueKafkaKeyIdentifierTransformer {

    private val log = LoggerFactory.getLogger(UniqueKafkaKeyIdentifierTransformer::class.java)

    fun <K> toInternal(external: ConsumerRecord<K, GenericRecord>): UniqueKafkaEventIdentifier {
        val key = external.key()

        return when (key) {
            null -> {
                val invalidEvent = UniqueKafkaEventIdentifier.createInvalidEvent()
                log.warn("Kan ikke telle eventet, fordi kafka-key (Nokkel) er null. Transformerer til et dummy-event: $invalidEvent.")
                invalidEvent
            }
            is Nokkel -> {
                UniqueKafkaEventIdentifier(key.getEventId(), key.getAppnavn(), key.getFodselsnummer())
            }
            else -> {
                val invalidEvent = UniqueKafkaEventIdentifier.createInvalidEvent()
                log.warn("Kan ikke telle eventet, fordi kafka-key (Nokkel) er av ukjent type. Transformerer til et dummy-event: $invalidEvent.")
                invalidEvent
            }
        }
    }
}
