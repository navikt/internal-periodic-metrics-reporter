package no.nav.personbruker.internal.periodic.metrics.reporter.statusoppdatering

import java.time.LocalDateTime
import java.time.ZoneId

object StatusoppdateringObjectMother {

    fun giveMeStatusoppdatering(eventId: String, fodselsnummer: String): Statusoppdatering {
        return giveMeStatusoppdatering(eventId = eventId, fodselsnummer = fodselsnummer, appnavn = "dummyAppnavn", link = "https://nav.no/systemX/$eventId")
    }

    fun giveMeStatusoppdateringWithLink(link: String): Statusoppdatering {
        return giveMeStatusoppdatering(eventId = "s-1", fodselsnummer = "1234", appnavn = "dummyAppnavn", link = link)
    }

    fun giveMeStatusoppdatering(eventId: String, fodselsnummer: String, appnavn: String, link: String): Statusoppdatering {
        return Statusoppdatering(
            appnavn = appnavn,
            eventId = eventId,
            eventTidspunkt = LocalDateTime.now(ZoneId.of("UTC")),
            fodselsnummer = fodselsnummer,
            grupperingsId = "systemA010",
            link = link,
            sikkerhetsnivaa = 4,
            sistOppdatert = LocalDateTime.now(ZoneId.of("UTC")),
            statusGlobal = "SENDT",
            statusIntern = "dummyStatusIntern",
            sakstema = "dummySakstema")
    }
}
