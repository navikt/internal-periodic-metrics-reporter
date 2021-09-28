package no.nav.personbruker.internal.periodic.metrics.reporter.beskjed

import no.nav.brukernotifikasjon.schemas.internal.BeskjedIntern
import java.time.Instant

object AvroBeskjedInternObjectMother {

    private val defaultLopenummer = 1
    private val defaultText = "Dette er Beskjed til brukeren"
    private val defaultEksternVarsling = false
    private val defaultPreferertKanaler = listOf("SMS")

    fun createBeskjed(lopenummer: Int): BeskjedIntern {
        return createBeskjed(lopenummer, defaultText)
    }

    fun createBeskjed(lopenummer: Int, text: String): BeskjedIntern {
        return BeskjedIntern(
                Instant.now().toEpochMilli(),
                Instant.now().toEpochMilli(),
                text,
                "https://nav.no/systemX/$lopenummer",
                4,
                defaultEksternVarsling,
                defaultPreferertKanaler)
    }

    fun createBeskjedWithoutSynligFremTilSatt(): BeskjedIntern {
        return BeskjedIntern(
                Instant.now().toEpochMilli(),
                null,
                defaultText,
                "https://nav.no/systemX/$defaultLopenummer",
                4,
                defaultEksternVarsling,
                defaultPreferertKanaler)
    }
}
