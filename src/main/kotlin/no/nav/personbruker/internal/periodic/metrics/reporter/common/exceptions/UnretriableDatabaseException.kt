package no.nav.personbruker.internal.periodic.metrics.reporter.common.exceptions

class UnretriableDatabaseException(message: String, cause: Throwable?) : AbstractPersonbrukerException(message, cause) {
    constructor(message: String) : this(message, null)
} 
