package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic

import no.nav.brukernotifikasjon.schemas.internal.NokkelIntern
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.KafkaEventIdentifier
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory

object KafkaKeyIdentifierTransformer {

    private val log = LoggerFactory.getLogger(KafkaKeyIdentifierTransformer::class.java)

    fun <K> toInternal(external: ConsumerRecord<K, GenericRecord>): KafkaEventIdentifier {
        val key = external.key()

        return when (key) {
            null -> {
                val invalidEvent = KafkaEventIdentifier.createInvalidEvent()
                log.warn("Kan ikke telle eventet, fordi kafka-key (Nokkel) er null. Transformerer til et dummy-event: $invalidEvent.")
                invalidEvent
            }
            is NokkelIntern -> {
                KafkaEventIdentifier(key.getEventId(), key.getAppnavn())
            }
            else -> {
                val invalidEvent = KafkaEventIdentifier.createInvalidEvent()
                log.warn("Kan ikke telle eventet, fordi kafka-key (Nokkel) er av ukjent type. Transformerer til et dummy-event: $invalidEvent.")
                invalidEvent
            }
        }
    }
}
