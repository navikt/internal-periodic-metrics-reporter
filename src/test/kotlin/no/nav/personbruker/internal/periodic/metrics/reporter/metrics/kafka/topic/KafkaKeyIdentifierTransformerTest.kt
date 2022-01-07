package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic

import no.nav.brukernotifikasjon.schemas.internal.NokkelIntern
import no.nav.personbruker.internal.periodic.metrics.reporter.beskjed.AvroBeskjedInternObjectMother
import no.nav.personbruker.internal.periodic.metrics.reporter.common.objectmother.ConsumerRecordsObjectMother
import no.nav.personbruker.internal.periodic.metrics.reporter.done.AvroDoneInternObjectMother
import no.nav.personbruker.internal.periodic.metrics.reporter.innboks.AvroInnboksInternObjectMother
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.KafkaEventIdentifier
import no.nav.personbruker.internal.periodic.metrics.reporter.oppgave.AvroOppgaveInternObjectMother
import no.nav.personbruker.internal.periodic.metrics.reporter.statusoppdatering.AvroStatusoppdateringInternObjectMother
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.shouldNotBeNull
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.junit.jupiter.api.Test

internal class KafkaKeyIdentifierTransformerTest {
    private val ulid = "01"
    private val eventid = "1"
    private val grupperingsId = "123"
    private val fodselsnummer = "1234"
    private val namespace = "dummyNamespace"
    private val appnavn = "dummyName"
    private val systembruker = "dummySystembruker"

    @Test
    fun `Should transform external Beskjed to internal`() {
        val nokkel = NokkelIntern(ulid, eventid, grupperingsId, fodselsnummer, namespace, appnavn, systembruker)
        val beskjed = AvroBeskjedInternObjectMother.createBeskjedWithoutSynligFremTilSatt()
        val original: ConsumerRecord<NokkelIntern, GenericRecord> =
                ConsumerRecordsObjectMother.createConsumerRecord(nokkel, beskjed)

        val transformed = KafkaKeyIdentifierTransformer.toInternal(original)

        transformed.eventId `should be equal to` nokkel.getEventId()
        transformed.appnavn `should be equal to` nokkel.getAppnavn()
    }

    @Test
    fun `Should transform external Innboks to internal`() {
        val nokkel = NokkelIntern(ulid, eventid, grupperingsId, fodselsnummer, namespace, appnavn, systembruker)
        val innboksEvent = AvroInnboksInternObjectMother.createInnboksWithText("Dummytekst")
        val original: ConsumerRecord<NokkelIntern, GenericRecord> =
                ConsumerRecordsObjectMother.createConsumerRecord(nokkel, innboksEvent)

        val transformed = KafkaKeyIdentifierTransformer.toInternal(original)

        transformed.eventId `should be equal to` nokkel.getEventId()
        transformed.appnavn `should be equal to` nokkel.getAppnavn()
    }

    @Test
    fun `Should transform external Oppgave to internal`() {
        val nokkel = NokkelIntern(ulid, eventid, grupperingsId, fodselsnummer, namespace, appnavn, systembruker)
        val oppgave = AvroOppgaveInternObjectMother.createOppgave("Dummytekst")
        val original: ConsumerRecord<NokkelIntern, GenericRecord> =
                ConsumerRecordsObjectMother.createConsumerRecord(nokkel, oppgave)

        val transformed = KafkaKeyIdentifierTransformer.toInternal(original)

        transformed.eventId `should be equal to` nokkel.getEventId()
        transformed.appnavn `should be equal to` nokkel.getAppnavn()
    }

    @Test
    fun `Should transform external Statusoppdatering to internal`() {
        val nokkel = NokkelIntern(ulid, eventid, grupperingsId, fodselsnummer, namespace, appnavn, systembruker)
        val statusoppdateringEvent = AvroStatusoppdateringInternObjectMother.createStatusoppdateringWithStatusGlobal("SENDT")
        val original: ConsumerRecord<NokkelIntern, GenericRecord> =
                ConsumerRecordsObjectMother.createConsumerRecord(nokkel, statusoppdateringEvent)

        val transformed = KafkaKeyIdentifierTransformer.toInternal(original)

        transformed.eventId `should be equal to` nokkel.getEventId()
        transformed.appnavn `should be equal to` nokkel.getAppnavn()
    }

    @Test
    fun `Should transform external Done-event to internal`() {
        val nokkel = NokkelIntern(ulid, eventid, grupperingsId, fodselsnummer, namespace, appnavn, systembruker)
        val done = AvroDoneInternObjectMother.createDone()
        val original: ConsumerRecord<NokkelIntern, GenericRecord> =
                ConsumerRecordsObjectMother.createConsumerRecord(nokkel, done)

        val transformed = KafkaKeyIdentifierTransformer.toInternal(original)

        transformed.eventId `should be equal to` nokkel.getEventId()
        transformed.appnavn `should be equal to` nokkel.getAppnavn()
    }

    @Test
    fun `Should handle null as key (NokkelIntern)`() {
        val done = AvroDoneInternObjectMother.createDone()
        val recordWithouKey: ConsumerRecord<NokkelIntern, GenericRecord> =
                ConsumerRecordsObjectMother.createConsumerRecordWithoutNokkel(done)
        val transformed = KafkaKeyIdentifierTransformer.toInternal(recordWithouKey)

        transformed.shouldNotBeNull()
        transformed `should be equal to` KafkaEventIdentifier.createInvalidEvent()
    }
}
