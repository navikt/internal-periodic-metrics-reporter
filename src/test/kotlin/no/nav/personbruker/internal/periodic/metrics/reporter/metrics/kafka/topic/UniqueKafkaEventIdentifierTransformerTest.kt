package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic

import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.personbruker.internal.periodic.metrics.reporter.beskjed.AvroBeskjedObjectMother
import no.nav.personbruker.internal.periodic.metrics.reporter.common.objectmother.ConsumerRecordsObjectMother
import no.nav.personbruker.internal.periodic.metrics.reporter.done.AvroDoneObjectMother
import no.nav.personbruker.internal.periodic.metrics.reporter.innboks.AvroInnboksObjectMother
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.UniqueKafkaEventIdentifier
import no.nav.personbruker.internal.periodic.metrics.reporter.oppgave.AvroOppgaveObjectMother
import no.nav.personbruker.internal.periodic.metrics.reporter.statusoppdatering.AvroStatusoppdateringObjectMother
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.shouldNotBeNull
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.junit.jupiter.api.Test

internal class UniqueKafkaEventIdentifierTransformerTest {
    private val eventid = "1"
    private val grupperingsId = "123"
    private val fodselsnummer = "1234"
    private val namespace = "dummyNamespace"
    private val appnavn = "dummyName"

    @Test
    fun `Should transform external Beskjed to internal`() {
        val nokkel = Nokkel(eventid, grupperingsId, fodselsnummer, namespace, appnavn)
        val beskjed = AvroBeskjedObjectMother.createBeskjedWithoutSynligFremTilSatt()
        val original: ConsumerRecord<Nokkel, GenericRecord> =
            ConsumerRecordsObjectMother.createConsumerRecord(nokkel, beskjed)

        val transformed = UniqueKafkaEventIdentifierTransformer.toInternal(original)

        transformed.eventId `should be equal to` nokkel.getEventId()
        transformed.appnavn `should be equal to` nokkel.getAppnavn()
        transformed.fodselsnummer `should be equal to` nokkel.getFodselsnummer()
    }

    @Test
    fun `Should transform external Innboks to internal`() {
        val nokkel = Nokkel(eventid, grupperingsId, fodselsnummer, namespace, appnavn)
        val innboksEvent = AvroInnboksObjectMother.createInnboksWithText("Dummytekst")
        val original: ConsumerRecord<Nokkel, GenericRecord> =
            ConsumerRecordsObjectMother.createConsumerRecord(nokkel, innboksEvent)

        val transformed = UniqueKafkaEventIdentifierTransformer.toInternal(original)

        transformed.eventId `should be equal to` nokkel.getEventId()
        transformed.appnavn `should be equal to` nokkel.getAppnavn()
        transformed.fodselsnummer `should be equal to` nokkel.getFodselsnummer()
    }

    @Test
    fun `Should transform external Oppgave to internal`() {
        val nokkel = Nokkel(eventid, grupperingsId, fodselsnummer, namespace, appnavn)
        val oppgave = AvroOppgaveObjectMother.createOppgave("Dummytekst")
        val original: ConsumerRecord<Nokkel, GenericRecord> =
            ConsumerRecordsObjectMother.createConsumerRecord(nokkel, oppgave)

        val transformed = UniqueKafkaEventIdentifierTransformer.toInternal(original)

        transformed.eventId `should be equal to` nokkel.getEventId()
        transformed.appnavn `should be equal to` nokkel.getAppnavn()
        transformed.fodselsnummer `should be equal to` nokkel.getFodselsnummer()
    }

    @Test
    fun `Should transform external Statusoppdatering to internal`() {
        val nokkel = Nokkel(eventid, grupperingsId, fodselsnummer, namespace, appnavn)
        val statusoppdateringEvent = AvroStatusoppdateringObjectMother.createStatusoppdateringWithStatusGlobal("SENDT")
        val original: ConsumerRecord<Nokkel, GenericRecord> =
            ConsumerRecordsObjectMother.createConsumerRecord(nokkel, statusoppdateringEvent)

        val transformed = UniqueKafkaEventIdentifierTransformer.toInternal(original)

        transformed.eventId `should be equal to` nokkel.getEventId()
        transformed.appnavn `should be equal to` nokkel.getAppnavn()
        transformed.fodselsnummer `should be equal to` nokkel.getFodselsnummer()
    }

    @Test
    fun `Should transform external Done-event to internal`() {
        val nokkel = Nokkel(eventid, grupperingsId, fodselsnummer, namespace, appnavn)
        val done = AvroDoneObjectMother.createDone()
        val original: ConsumerRecord<Nokkel, GenericRecord> =
            ConsumerRecordsObjectMother.createConsumerRecord(nokkel, done)

        val transformed = UniqueKafkaEventIdentifierTransformer.toInternal(original)

        transformed.eventId `should be equal to` nokkel.getEventId()
        transformed.appnavn `should be equal to` nokkel.getAppnavn()
        transformed.fodselsnummer `should be equal to` nokkel.getFodselsnummer()
    }

    @Test
    fun `Should handle null as key (Nokkel)`() {
        val done = AvroDoneObjectMother.createDone()
        val recordWithouKey: ConsumerRecord<Nokkel, GenericRecord> =
            ConsumerRecordsObjectMother.createConsumerRecordWithoutNokkel(done)
        val transformed = UniqueKafkaEventIdentifierTransformer.toInternal(recordWithouKey)

        transformed.shouldNotBeNull()
        transformed `should be equal to` UniqueKafkaEventIdentifier.createInvalidEvent()
    }

    @Test
    fun `Should handle null as value (record)`() {
        val nokkel = Nokkel(eventid, grupperingsId, fodselsnummer, namespace, appnavn)
        val recordWithoutValue: ConsumerRecord<Nokkel, GenericRecord> = ConsumerRecordsObjectMother.createConsumerRecordWithoutRecord(nokkel)
        val transformed = UniqueKafkaEventIdentifierTransformer.toInternal(recordWithoutValue)

        transformed.shouldNotBeNull()
        transformed `should be equal to` UniqueKafkaEventIdentifier.createEventWithoutValidFnr(
            nokkel.getEventId(),
            nokkel.getAppnavn()
        )
    }
}
