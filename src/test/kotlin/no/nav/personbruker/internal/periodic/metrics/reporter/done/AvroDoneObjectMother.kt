package no.nav.personbruker.internal.periodic.metrics.reporter.done

import no.nav.brukernotifikasjon.schemas.Done
import java.time.Instant

object AvroDoneObjectMother {

    fun createDone(eventId: String, fodselsnummer: String): Done {
        return Done(
                Instant.now().toEpochMilli(),
                fodselsnummer,
                "100${eventId}"
        )
    }
}
