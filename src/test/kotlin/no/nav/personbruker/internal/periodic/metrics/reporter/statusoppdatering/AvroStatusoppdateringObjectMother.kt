package no.nav.personbruker.internal.periodic.metrics.reporter.statusoppdatering

import no.nav.brukernotifikasjon.schemas.Statusoppdatering
import java.time.Instant

object AvroStatusoppdateringObjectMother {

    private val defaultLink = "http://dummyLink"
    private val defaultSikkerhetsnivaa = 4
    private val defaultStatusIntern = "statusIntern"
    private val defaultSakstema = "dummySakstema"

    fun createStatusoppdateringWithStatusGlobal(statusGlobal: String): Statusoppdatering {
        return createStatusoppdatering(statusGlobal, defaultStatusIntern, defaultSakstema)
    }

    fun createStatusoppdatering(statusGlobal: String, statusIntern: String?, sakstema: String): Statusoppdatering {
        return Statusoppdatering(
                Instant.now().toEpochMilli(),
                defaultLink,
                defaultSikkerhetsnivaa,
                statusGlobal,
                statusIntern,
                sakstema)
    }

}
