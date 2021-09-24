package no.nav.personbruker.internal.periodic.metrics.reporter.nokkel

import no.nav.brukernotifikasjon.schemas.internal.NokkelIntern

object AvroNokkelObjectMother {

    private val defaultUlid = "01"
    private val defaultFodselsnummer = "1234"
    private val defaultGrupperingsId = "123"
    private val defaultNamespace = "x-namespace"
    private val defaultAppnavn = "x-name"
    private val defaultSystembruker = "x-systembruker"

    fun createNokkel(eventId: Int): NokkelIntern = NokkelIntern(
            defaultUlid,
            eventId.toString(),
            defaultGrupperingsId,
            defaultFodselsnummer,
            defaultNamespace,
            defaultAppnavn,
            defaultSystembruker
    )

}
