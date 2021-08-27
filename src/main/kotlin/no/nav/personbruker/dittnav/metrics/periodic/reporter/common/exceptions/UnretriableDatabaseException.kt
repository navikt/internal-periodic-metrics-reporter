package no.nav.personbruker.dittnav.metrics.periodic.reporter.common.exceptions

class UnretriableDatabaseException(message: String, cause: Throwable?) : AbstractPersonbrukerException(message, cause) {
    constructor(message: String) : this(message, null)
} 
