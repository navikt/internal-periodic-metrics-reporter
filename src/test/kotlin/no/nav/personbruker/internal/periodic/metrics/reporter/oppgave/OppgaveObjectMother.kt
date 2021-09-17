package no.nav.personbruker.internal.periodic.metrics.reporter.oppgave

import java.time.LocalDateTime
import java.time.ZoneId

object OppgaveObjectMother {

    fun giveMeAktivOppgave(eventId: String, fodselsnummer: String): Oppgave {
        return giveMeAktivOppgave(eventId, fodselsnummer, "dummyAppnavn")
    }

    fun giveMeAktivOppgave(eventId: String, fodselsnummer: String, appnavn: String): Oppgave {
        return Oppgave(
                appnavn = appnavn,
                eventTidspunkt = LocalDateTime.now(ZoneId.of("UTC")),
                fodselsnummer = fodselsnummer,
                eventId = eventId,
                grupperingsId = "Dok12345",
                tekst = "Dette er en oppgave til brukeren",
                link = "https://nav.no/systemX/",
                sikkerhetsnivaa = 4,
                sistOppdatert = LocalDateTime.now(ZoneId.of("UTC")),
                aktiv = true)
    }

    fun giveMeInaktivOppgave(): Oppgave {
        return Oppgave(
                appnavn = "dummyAppnavn",
                eventTidspunkt = LocalDateTime.now(ZoneId.of("UTC")),
                fodselsnummer = "123",
                eventId = "o-2",
                grupperingsId = "Dok12345",
                tekst = "Dette er en oppgave til brukeren",
                link = "https://nav.no/systemX/",
                sikkerhetsnivaa = 4,
                sistOppdatert = LocalDateTime.now(ZoneId.of("UTC")),
                aktiv = false)
    }

}
