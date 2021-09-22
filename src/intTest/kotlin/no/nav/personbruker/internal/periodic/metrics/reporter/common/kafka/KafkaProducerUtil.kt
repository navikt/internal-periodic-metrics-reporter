package no.nav.personbruker.internal.periodic.metrics.reporter.common.kafka

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig
import kotlinx.coroutines.withTimeoutOrNull
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.*

object KafkaProducerUtil {

    suspend fun <K> kafkaAvroProduce(
            brokersURL: String,
            schemaRegistryUrl: String,
            topic: String,
            data: Map<K, GenericRecord>
    ): Boolean =
            try {
                KafkaProducer<K, GenericRecord>(
                        Properties().apply {
                            set(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokersURL)
                            set(ProducerConfig.CLIENT_ID_CONFIG, "funKafkaAvroProduce")
                            set(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaAvroSerializer")
                            set(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaAvroSerializer")
                            set(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl)
                            set(ProducerConfig.ACKS_CONFIG, "all")
                            set(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1)
                            set(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 500)
                        }
                ).use { p ->
                    withTimeoutOrNull(10_000) {
                        data.forEach { k, v -> p.send(ProducerRecord(topic, k, v)).get() }
                        true
                    } ?: false
                }
            } catch (e: Exception) {
                false
            }
}
