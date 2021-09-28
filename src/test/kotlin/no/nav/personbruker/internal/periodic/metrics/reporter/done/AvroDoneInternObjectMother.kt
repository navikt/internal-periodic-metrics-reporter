package no.nav.personbruker.internal.periodic.metrics.reporter.done

import no.nav.brukernotifikasjon.schemas.internal.DoneIntern
import java.time.Instant

object AvroDoneInternObjectMother {

    fun createDone(): DoneIntern {
        return DoneIntern(
                Instant.now().toEpochMilli()
        )
    }
}
