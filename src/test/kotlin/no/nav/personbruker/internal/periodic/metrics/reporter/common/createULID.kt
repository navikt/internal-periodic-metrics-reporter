package no.nav.personbruker.internal.periodic.metrics.reporter.common

import de.huxhorn.sulky.ulid.ULID

fun createULID(): String {
    return ULID().nextULID()
}
