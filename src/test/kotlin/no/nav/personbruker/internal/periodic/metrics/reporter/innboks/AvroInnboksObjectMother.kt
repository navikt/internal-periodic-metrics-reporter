package no.nav.personbruker.internal.periodic.metrics.reporter.innboks

import no.nav.brukernotifikasjon.schemas.internal.InnboksIntern
import java.time.Instant

object AvroInnboksObjectMother {

    private val defaultLopenummer = 1

    fun createInnboksWithText(text: String): InnboksIntern {
        return createInnboks(defaultLopenummer, text)
    }

    fun createInnboks(lopenummer: Int, text: String): InnboksIntern {
        return InnboksIntern(
                Instant.now().toEpochMilli(),
                text,
                "https://nav.no/systemX/$lopenummer",
                4)
    }

}
