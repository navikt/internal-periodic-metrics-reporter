package no.nav.personbruker.internal.periodic.metrics.reporter.beskjed

import no.nav.brukernotifikasjon.schemas.Beskjed
import java.time.Instant

object AvroBeskjedObjectMother {

    private val defaultLopenummer = 1
    private val defaultText = "Dette er Beskjed til brukeren"
    private val defaultEksternVarsling = false
    private val defaultPreferertKanaler = listOf("SMS")

    fun createBeskjed(lopenummer: Int): Beskjed {
        return createBeskjed(lopenummer, defaultText)
    }

    fun createBeskjed(lopenummer: Int, text: String): Beskjed {
        return Beskjed(
                Instant.now().toEpochMilli(),
                Instant.now().toEpochMilli(),
                text,
                "https://nav.no/systemX/$lopenummer",
                4,
                defaultEksternVarsling,
                defaultPreferertKanaler)
    }

    fun createBeskjedWithoutSynligFremTilSatt(): Beskjed {
        return Beskjed(
                Instant.now().toEpochMilli(),
                null,
                defaultText,
                "https://nav.no/systemX/$defaultLopenummer",
                4,
                defaultEksternVarsling,
                defaultPreferertKanaler)
    }
}
