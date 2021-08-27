package no.nav.personbruker.internal.periodic.metrics.reporter.nokkel

import no.nav.brukernotifikasjon.schemas.Nokkel

object AvroNokkelObjectMother {

    private val defaultSystembruker = "dummySystembruker"

    fun createNokkel(eventId: Int): Nokkel = Nokkel(defaultSystembruker, eventId.toString())

}
