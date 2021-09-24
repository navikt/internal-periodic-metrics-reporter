package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.brukernotifikasjon.schemas.internal.NokkelIntern
import no.nav.personbruker.internal.periodic.metrics.reporter.common.`with message containing`
import no.nav.personbruker.internal.periodic.metrics.reporter.common.exceptions.CountException
import no.nav.personbruker.internal.periodic.metrics.reporter.common.kafka.Consumer
import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.activity.TopicActivityService
import org.amshove.kluent.`should throw`
import org.amshove.kluent.invoking
import org.apache.avro.generic.GenericRecord
import org.junit.jupiter.api.Test
import java.time.Duration

internal class TopicEventCounterServiceTest {

    private val beskjedCountConsumer: Consumer<NokkelIntern, GenericRecord> = mockk(relaxed = true)
    private val innboksCountConsumer: Consumer<NokkelIntern, GenericRecord> = mockk(relaxed = true)
    private val oppgaveCountConsumer: Consumer<NokkelIntern, GenericRecord> = mockk(relaxed = true)
    private val statusoppdateringCountConsumer: Consumer<NokkelIntern, GenericRecord> = mockk(relaxed = true)
    private val doneCountConsumer: Consumer<NokkelIntern, GenericRecord> = mockk(relaxed = true)
    private val beskjedActivityService: TopicActivityService = mockk()
    private val innboksActivityService: TopicActivityService = mockk()
    private val oppgaveActivityService: TopicActivityService = mockk()
    private val statusoppdateringActivityService: TopicActivityService = mockk()
    private val doneActivityService: TopicActivityService = mockk()
    private val beskjedCounter = TopicEventTypeCounter(beskjedCountConsumer, beskjedActivityService, EventType.BESKJED_INTERN, false)
    private val innboksCounter = TopicEventTypeCounter(innboksCountConsumer, innboksActivityService, EventType.INNBOKS_INTERN, false)
    private val oppgaveCounter = TopicEventTypeCounter(oppgaveCountConsumer, oppgaveActivityService, EventType.OPPGAVE_INTERN, false)
    private val statusoppdateringCounter = TopicEventTypeCounter(statusoppdateringCountConsumer, statusoppdateringActivityService, EventType.STATUSOPPDATERING_INTERN, false)
    private val doneCounter = TopicEventTypeCounter(doneCountConsumer, doneActivityService, EventType.DONE__INTERN, false)

    @Test
    internal fun `Should handle exceptions and rethrow as internal exception`() {
        val simulatedException = Exception("Simulated error in a test")
        coEvery { beskjedCountConsumer.kafkaConsumer.poll(any<Duration>()) } throws simulatedException
        coEvery { innboksCountConsumer.kafkaConsumer.poll(any<Duration>()) } throws simulatedException
        coEvery { oppgaveCountConsumer.kafkaConsumer.poll(any<Duration>()) } throws simulatedException
        coEvery { statusoppdateringCountConsumer.kafkaConsumer.poll(any<Duration>()) } throws simulatedException
        coEvery { doneCountConsumer.kafkaConsumer.poll(any<Duration>()) } throws simulatedException

        invoking {
            runBlocking {
                beskjedCounter.countEventsAsync()
            }
        } `should throw` CountException::class `with message containing` "beskjed_intern"

        invoking {
            runBlocking {
                doneCounter.countEventsAsync()
            }
        } `should throw` CountException::class `with message containing` "done_intern"

        invoking {
            runBlocking {
                innboksCounter.countEventsAsync()
            }
        } `should throw` CountException::class `with message containing` "innboks_intern"

        invoking {
            runBlocking {
                oppgaveCounter.countEventsAsync()
            }
        } `should throw` CountException::class `with message containing` "oppgave_intern"

        invoking {
            runBlocking {
                statusoppdateringCounter.countEventsAsync()
            }
        } `should throw` CountException::class `with message containing` "statusoppdatering_intern"

    }
}
