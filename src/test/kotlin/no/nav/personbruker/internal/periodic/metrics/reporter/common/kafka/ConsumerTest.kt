package no.nav.personbruker.internal.periodic.metrics.reporter.common.kafka

import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import no.nav.brukernotifikasjon.schemas.internal.BeskjedIntern
import no.nav.brukernotifikasjon.schemas.internal.NokkelIntern
import no.nav.personbruker.internal.periodic.metrics.reporter.common.objectmother.ConsumerRecordsObjectMother
import no.nav.personbruker.internal.periodic.metrics.reporter.health.Status
import org.amshove.kluent.`should be equal to`
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration

class ConsumerTest {

    private val kafkaConsumer = mockk<KafkaConsumer<NokkelIntern, BeskjedIntern>>(relaxUnitFun = true)

    companion object {
        private val defaultMaxPollTimeout = Duration.ofMillis(5000)
    }

    @BeforeEach
    fun initMocks() {
        clearMocks(kafkaConsumer)
    }

    @Test
    fun `Skal polle mot Kafka hvis ingen feil skjer`() {
        val topic = "dummyTopicNoErrors"
        val records: ConsumerRecords<NokkelIntern, BeskjedIntern>
        every { kafkaConsumer.poll(any<Duration>()) } returns ConsumerRecordsObjectMother.giveMeANumberOfBeskjedRecords(1, topic)

        val consumer: Consumer<NokkelIntern, BeskjedIntern> = Consumer(topic, kafkaConsumer)

        runBlocking {
            consumer.startSubscription()
            records = consumer.kafkaConsumer.poll(defaultMaxPollTimeout)
            delay(300)

            records.count() `should be equal to` 1
            consumer.status().status `should be equal to` Status.OK
            consumer.stop()
        }
    }
}
