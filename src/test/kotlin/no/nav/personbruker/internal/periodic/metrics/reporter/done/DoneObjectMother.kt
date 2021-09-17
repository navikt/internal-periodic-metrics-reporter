package no.nav.personbruker.internal.periodic.metrics.reporter.done

import java.time.LocalDateTime
import java.time.ZoneId

object DoneObjectMother {

    fun giveMeDone(eventId: String): Done {
        val appnavn = "dummyAppnavn"
        val fodselsnummer = "12345678901"
        return giveMeDone(eventId, appnavn, fodselsnummer)
    }

    fun giveMeDone(eventId: String, appnavn: String): Done {
        val fodselsnummer = "12345678901"
        return giveMeDone(eventId, appnavn, fodselsnummer)
    }

    fun giveMeDone(eventId: String, appnavn: String, fodselsnummer: String): Done {
        return Done(
                appnavn,
                eventId,
                LocalDateTime.now(ZoneId.of("UTC")),
                fodselsnummer,
                "100${eventId}"
        )
    }

}
