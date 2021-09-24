package no.nav.personbruker.internal.periodic.metrics.reporter.statusoppdatering

import no.nav.brukernotifikasjon.schemas.internal.StatusoppdateringIntern
import java.time.Instant

object AvroStatusoppdateringObjectMother {

    private val defaultLink = "http://dummyLink"
    private val defaultSikkerhetsnivaa = 4
    private val defaultStatusIntern = "statusIntern"
    private val defaultSakstema = "dummySakstema"

    fun createStatusoppdateringWithStatusGlobal(statusGlobal: String): StatusoppdateringIntern {
        return createStatusoppdatering(statusGlobal, defaultStatusIntern, defaultSakstema)
    }

    fun createStatusoppdatering(statusGlobal: String, statusIntern: String?, sakstema: String): StatusoppdateringIntern {
        return StatusoppdateringIntern(
                Instant.now().toEpochMilli(),
                defaultLink,
                defaultSikkerhetsnivaa,
                statusGlobal,
                statusIntern,
                sakstema)
    }

}
