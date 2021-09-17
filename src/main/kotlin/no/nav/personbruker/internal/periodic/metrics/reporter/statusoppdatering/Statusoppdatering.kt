package no.nav.personbruker.internal.periodic.metrics.reporter.statusoppdatering

import no.nav.brukernotifikasjon.schemas.builders.domain.Eventtype
import no.nav.brukernotifikasjon.schemas.builders.util.ValidationUtil
import java.time.LocalDateTime

data class Statusoppdatering(
        val id: Int?,
        val appnavn: String,
        val eventId: String,
        val eventTidspunkt: LocalDateTime,
        val fodselsnummer: String,
        val grupperingsId: String,
        val link: String,
        val sikkerhetsnivaa: Int,
        val sistOppdatert: LocalDateTime,
        val statusGlobal: String,
        val statusIntern: String?,
        val sakstema: String
) {
    constructor(
            appnavn: String,
            eventId: String,
            eventTidspunkt: LocalDateTime,
            fodselsnummer: String,
            grupperingsId: String,
            link: String,
            sikkerhetsnivaa: Int,
            sistOppdatert: LocalDateTime,
            statusGlobal: String,
            statusIntern: String?,
            sakstema: String
    ) : this(null,
        appnavn,
        eventId,
        eventTidspunkt,
        fodselsnummer,
        grupperingsId,
        link,
        sikkerhetsnivaa,
        sistOppdatert,
        statusGlobal,
        statusIntern,
        sakstema

    ) {
        ValidationUtil.validateNonNullFieldMaxLength(appnavn, "appnavn", ValidationUtil.MAX_LENGTH_SYSTEMBRUKER)
        ValidationUtil.validateNonNullFieldMaxLength(eventId, "eventId", ValidationUtil.MAX_LENGTH_EVENTID)
        ValidationUtil.validateNonNullFieldMaxLength(fodselsnummer, "fodselsnummer", ValidationUtil.MAX_LENGTH_FODSELSNUMMER)
        ValidationUtil.validateNonNullFieldMaxLength(grupperingsId, "grupperingsId", ValidationUtil.MAX_LENGTH_GRUPPERINGSID)
        ValidationUtil.validateLinkAndConvertToString(
            ValidationUtil.validateLinkAndConvertToURL(link), "link", ValidationUtil.MAX_LENGTH_LINK, ValidationUtil.isLinkRequired(
                Eventtype.STATUSOPPDATERING))
        ValidationUtil.validateSikkerhetsnivaa(sikkerhetsnivaa)
        ValidationUtil.validateStatusGlobal(statusGlobal)
        statusIntern?.let { status -> ValidationUtil.validateMaxLength(status, "statusIntern",
            ValidationUtil.MAX_LENGTH_STATUSINTERN
        ) }
        ValidationUtil.validateNonNullFieldMaxLength(sakstema, "sakstema", ValidationUtil.MAX_LENGTH_SAKSTEMA)
    }

    override fun toString(): String {
        return "Statusoppdatering(" +
                "id=$id, " +
                "appnavn=***, " +
                "eventId=$eventId, " +
                "eventTidspunkt=$eventTidspunkt, " +
                "fodselsnummer=***, " +
                "grupperingsId=$grupperingsId, " +
                "link=***, " +
                "sikkerhetsnivaa=$sikkerhetsnivaa, " +
                "sistOppdatert=$sistOppdatert, " +
                "statusGlobal=$statusGlobal, " +
                "statusIntern=$statusIntern, " +
                "sakstema=$sakstema, "
    }
}
