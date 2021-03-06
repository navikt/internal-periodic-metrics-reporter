package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.submitter

import kotlinx.coroutines.*
import kotlinx.coroutines.time.delay
import no.nav.personbruker.internal.periodic.metrics.reporter.health.HealthStatus
import no.nav.personbruker.internal.periodic.metrics.reporter.health.Status
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration
import java.time.Instant
import kotlin.coroutines.CoroutineContext

class PeriodicMetricsSubmitter(
    private val metricsSubmitterService: MetricsSubmitterService,
    countingIntervalMinutes: Long,
    private val job: Job = Job()
) : CoroutineScope {

    private val log: Logger = LoggerFactory.getLogger(PeriodicMetricsSubmitter::class.java)
    private val minutesToWait = Duration.ofMinutes(countingIntervalMinutes)

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    fun status(): HealthStatus {
        return when (job.isActive) {
            true -> HealthStatus("PeriodicMetricsSubmitter", Status.OK, "Submitter is running", false)
            false -> HealthStatus("PeriodicMetricsSubmitter", Status.ERROR, "Submitter is not running", false)
        }
    }

    suspend fun stop() {
        log.info("Stopper periodisk innrapportering av metrikker")
        job.cancelAndJoin()
    }

    fun isCompleted(): Boolean {
        return job.isCompleted
    }

    fun start() {
        log.info("Periodisk innrapportering av metrikker har blitt aktivert, første prosessering skjer om $minutesToWait minutter.")
        launch {
            while (job.isActive) {
                delay(minutesToWait)
                submitMetrics()
            }
        }
    }

    suspend fun submitMetrics() {
        val start = Instant.now()
        log.info("Starter å rapportere inn metrikker...")

        metricsSubmitterService.submitMetrics()

        val elapsedTime = calculateElapsedTime(start)
        log.info("...ferdig med å rapportere inn metrikker, det ${elapsedTime}ms.")
    }

    private fun calculateElapsedTime(start: Instant): Long {
        val stop = Instant.now()
        val elapsedTime = stop.toEpochMilli() - start.toEpochMilli()
        return elapsedTime
    }
}
