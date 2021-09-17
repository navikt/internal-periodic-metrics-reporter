package no.nav.personbruker.internal.periodic.metrics.reporter.done

import no.nav.brukernotifikasjon.schemas.Done
import java.time.Instant

object AvroDoneObjectMother {

    fun createDone(): Done {
        return Done(
                Instant.now().toEpochMilli()
        )
    }
}
