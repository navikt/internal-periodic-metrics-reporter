package no.nav.personbruker.internal.periodic.metrics.reporter.common.exceptions

class AggregatorBatchUpdateException(message: String, cause: Throwable?) : RetriableDatabaseException(message, cause) {
    constructor(message: String) : this(message, null)
}
