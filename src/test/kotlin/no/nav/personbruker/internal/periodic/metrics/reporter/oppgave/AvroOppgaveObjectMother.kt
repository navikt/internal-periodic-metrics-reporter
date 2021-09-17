package no.nav.personbruker.internal.periodic.metrics.reporter.oppgave

import no.nav.brukernotifikasjon.schemas.Oppgave
import java.time.Instant

object AvroOppgaveObjectMother {

    private val defaultLopenummer = 1
    private val defaultEksternVarsling = false
    private val defaultPreferertKanaler = listOf("SMS")

    fun createOppgave(tekst: String): Oppgave {
        return createOppgave(defaultLopenummer, tekst)
    }

    fun createOppgave(lopenummer: Int, tekst: String): Oppgave {
        return Oppgave(
                Instant.now().toEpochMilli(),
                tekst,
                "https://nav.no/systemX/$lopenummer",
                4,
                defaultEksternVarsling,
                defaultPreferertKanaler)
    }

}
