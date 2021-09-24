package no.nav.personbruker.internal.periodic.metrics.reporter.common.objectmother

import no.nav.brukernotifikasjon.schemas.internal.BeskjedIntern
import no.nav.brukernotifikasjon.schemas.internal.NokkelIntern
import no.nav.personbruker.internal.periodic.metrics.reporter.beskjed.AvroBeskjedObjectMother
import no.nav.personbruker.internal.periodic.metrics.reporter.nokkel.AvroNokkelObjectMother.createNokkel
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.common.TopicPartition

object ConsumerRecordsObjectMother {

    fun giveMeANumberOfBeskjedRecords(numberOfRecords: Int, topicName: String): ConsumerRecords<NokkelIntern, BeskjedIntern> {
        val records = mutableMapOf<TopicPartition, List<ConsumerRecord<NokkelIntern, BeskjedIntern>>>()
        val recordsForSingleTopic = createBeskjedRecords(topicName, numberOfRecords)
        records[TopicPartition(topicName, numberOfRecords)] = recordsForSingleTopic
        return ConsumerRecords(records)
    }

    private fun createBeskjedRecords(topicName: String, totalNumber: Int): List<ConsumerRecord<NokkelIntern, BeskjedIntern>> {
        val allRecords = mutableListOf<ConsumerRecord<NokkelIntern, BeskjedIntern>>()
        for (i in 0 until totalNumber) {
            val schemaRecord = AvroBeskjedObjectMother.createBeskjed(i)
            val nokkel = createNokkel(i)
            allRecords.add(ConsumerRecord(topicName, i, i.toLong(), nokkel, schemaRecord))
        }
        return allRecords
    }

    fun <K, V> createConsumerRecord(nokkel: K, actualEvent: V): ConsumerRecord<K, V> {
        return ConsumerRecord("dummyTopic", 1, 0, nokkel, actualEvent)
    }

    @Suppress("UNCHECKED_CAST")
    fun <K, V> createConsumerRecordWithoutNokkel(actualEvent: V): ConsumerRecord<K, V> {
        return ConsumerRecord("dummyTopic", 1, 0, null, actualEvent) as ConsumerRecord<K, V>
    }

    @Suppress("UNCHECKED_CAST")
    fun createConsumerRecordWithoutRecord(nokkel: NokkelIntern): ConsumerRecord<NokkelIntern, GenericRecord> {
        return ConsumerRecord("dummyTopic", 1, 0, nokkel, null) as ConsumerRecord<NokkelIntern, GenericRecord>
    }

}
