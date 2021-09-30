package no.nav.personbruker.internal.periodic.metrics.reporter.oppgave

import no.nav.brukernotifikasjon.schemas.internal.OppgaveIntern
import java.time.Instant

object AvroOppgaveInternObjectMother {

    private val defaultLopenummer = 1
    private val defaultEksternVarsling = false
    private val defaultPreferertKanaler = listOf("SMS")

    fun createOppgave(tekst: String): OppgaveIntern {
        return createOppgave(defaultLopenummer, tekst)
    }

    fun createOppgave(lopenummer: Int, tekst: String): OppgaveIntern {
        return OppgaveIntern(
                Instant.now().toEpochMilli(),
                tekst,
                "https://nav.no/systemX/$lopenummer",
                4,
                defaultEksternVarsling,
                defaultPreferertKanaler)
    }

}
