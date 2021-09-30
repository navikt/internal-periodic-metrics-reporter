package no.nav.personbruker.internal.periodic.metrics.reporter.health

interface HealthCheck {

    suspend fun status(): HealthStatus

}
