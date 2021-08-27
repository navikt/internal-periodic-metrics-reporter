package no.nav.personbruker.internal.periodic.metrics.reporter.done

import no.nav.brukernotifikasjon.schemas.internal.DoneIntern
import no.nav.personbruker.internal.periodic.metrics.reporter.common.createULID
import java.time.Instant

object AvroDoneInternObjectMother {

    private val defaultGrupperingsId = "123"

    fun createDoneIntern(): DoneIntern {
        return DoneIntern(
            createULID(),
            Instant.now().toEpochMilli(),
            defaultGrupperingsId
        )
    }
}
