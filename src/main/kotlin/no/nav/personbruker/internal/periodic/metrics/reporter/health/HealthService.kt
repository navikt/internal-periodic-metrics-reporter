package no.nav.personbruker.internal.periodic.metrics.reporter.health

import no.nav.personbruker.internal.periodic.metrics.reporter.config.ApplicationContext

class HealthService(private val applicationContext: ApplicationContext) {

    suspend fun getHealthChecks(): List<HealthStatus> {
        return listOf(
                applicationContext.periodicMetricsSubmitter.status(),
                applicationContext.periodicConsumerCheck.status(),
                applicationContext.beskjedCountAivenConsumer.status(),
                applicationContext.innboksCountAivenConsumer.status(),
                applicationContext.oppgaveCountAivenConsumer.status(),
                applicationContext.statusoppdateringCountAivenConsumer.status(),
                applicationContext.doneCountAivenConsumer.status()
        )
    }
}
