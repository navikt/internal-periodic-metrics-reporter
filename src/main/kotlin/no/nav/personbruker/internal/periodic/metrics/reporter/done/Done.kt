package no.nav.personbruker.internal.periodic.metrics.reporter.done

import no.nav.brukernotifikasjon.schemas.builders.util.ValidationUtil.validateFodselsnummer
import no.nav.brukernotifikasjon.schemas.builders.util.ValidationUtil.validateNonNullFieldMaxLength
import java.time.LocalDateTime

data class Done(
        val appnavn: String,
        val eventId: String,
        val eventTidspunkt: LocalDateTime,
        val fodselsnummer: String,
        val grupperingsId: String
) {

    init {
        validateNonNullFieldMaxLength(appnavn, "appnavn", 100)
        validateNonNullFieldMaxLength(eventId, "eventId", 50)
        validateFodselsnummer(fodselsnummer)
    }

    override fun toString(): String {
        return "Done(" +
                "appnavn=***, " +
                "eventId=$eventId, " +
                "eventTidspunkt=$eventTidspunkt, " +
                "fodselsnummer=***, " +
                "grupperingsId=$grupperingsId"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Done

        if (appnavn != other.appnavn) return false
        if (eventId != other.eventId) return false
        if (eventTidspunkt != other.eventTidspunkt) return false
        if (fodselsnummer != other.fodselsnummer) return false
        if (grupperingsId != other.grupperingsId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = appnavn.hashCode()
        result = 31 * result + eventId.hashCode()
        result = 31 * result + eventTidspunkt.hashCode()
        result = 31 * result + fodselsnummer.hashCode()
        result = 31 * result + grupperingsId.hashCode()
        return result
    }
}
