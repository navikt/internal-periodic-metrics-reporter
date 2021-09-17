package no.nav.personbruker.internal.periodic.metrics.reporter.innboks

import no.nav.brukernotifikasjon.schemas.Innboks
import java.time.Instant

object AvroInnboksObjectMother {

    private val defaultLopenummer = 1

    fun createInnboksWithText(text: String): Innboks {
        return createInnboks(defaultLopenummer, text)
    }

    fun createInnboks(lopenummer: Int, text: String): Innboks {
        return Innboks(
                Instant.now().toEpochMilli(),
                text,
                "https://nav.no/systemX/$lopenummer",
                4)
    }

}
