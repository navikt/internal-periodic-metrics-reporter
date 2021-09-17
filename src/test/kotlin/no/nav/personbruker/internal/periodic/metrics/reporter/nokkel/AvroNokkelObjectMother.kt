package no.nav.personbruker.internal.periodic.metrics.reporter.nokkel

import no.nav.brukernotifikasjon.schemas.Nokkel

object AvroNokkelObjectMother {

    private val defaultFodselsnummer = "1234"
    private val defaultGrupperingsId = "123"
    private val defaultNamespace = "x-namespace"
    private val defaultAppnavn = "x-name"

    fun createNokkel(eventId: Int): Nokkel = Nokkel(eventId.toString(),
            defaultGrupperingsId,
            defaultFodselsnummer,
            defaultNamespace,
            defaultAppnavn
    )

}
