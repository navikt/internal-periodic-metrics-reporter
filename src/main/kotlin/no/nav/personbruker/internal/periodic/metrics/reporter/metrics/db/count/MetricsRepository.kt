package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.db.count

import no.nav.personbruker.internal.periodic.metrics.reporter.common.database.Database
import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MetricsRepository(private val database: Database) {

    suspend fun getNumberOfEventsOfTypeGroupedByProdusent(eventType: EventType): Map<String, Int> {
        return database.queryWithExceptionTranslation {
            countTotalNumberOfEventsGroupedBySystembruker(eventType)
        }
    }

    suspend fun getNumberOfInactiveBrukernotifikasjonerGroupedByProdusent(): Map<String, Int> {
        return database.queryWithExceptionTranslation {
            countTotalNumberOfBrukernotifikasjonerByActiveStatus(false)
        }
    }
}
