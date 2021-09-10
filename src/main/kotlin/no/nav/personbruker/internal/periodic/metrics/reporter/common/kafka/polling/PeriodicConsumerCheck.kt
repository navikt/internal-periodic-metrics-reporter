package no.nav.personbruker.internal.periodic.metrics.reporter.common.kafka.polling

import kotlinx.coroutines.*
import kotlinx.coroutines.time.delay
import no.nav.personbruker.internal.periodic.metrics.reporter.config.ApplicationContext
import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.config.KafkaConsumerSetup
import no.nav.personbruker.internal.periodic.metrics.reporter.health.HealthStatus
import no.nav.personbruker.internal.periodic.metrics.reporter.health.Status
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration
import kotlin.coroutines.CoroutineContext

class PeriodicConsumerCheck(
        private val appContext: ApplicationContext,
        private val job: Job = Job()) : CoroutineScope {

    private val log: Logger = LoggerFactory.getLogger(PeriodicConsumerCheck::class.java)
    private val minutesToWait = Duration.ofMinutes(30)

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    fun start() {
        log.info("Periodisk sjekking at konsumerne kjører, første sjekk skjer om $minutesToWait minutter.")
        launch {
            while (job.isActive) {
                delay(minutesToWait)
                checkIfConsumersAreRunningAndRestartIfNot()
            }
        }
    }

    suspend fun checkIfConsumersAreRunningAndRestartIfNot() {
        val stoppedConsumersAiven = getConsumersThatHaveStoppedAiven()

        if(stoppedConsumersAiven.isNotEmpty()) {
            restartConsumersAiven(stoppedConsumersAiven)
        }
    }

    fun getConsumersThatHaveStoppedAiven(): MutableList<EventType> {
        val stoppedConsumers = mutableListOf<EventType>()

        if (appContext.beskjedCountAivenConsumer.isStopped()) {
            stoppedConsumers.add(EventType.BESKJED)
        }
        if (appContext.doneCountAivenConsumer.isStopped()) {
            stoppedConsumers.add(EventType.DONE)
        }
        if (appContext.oppgaveCountAivenConsumer.isStopped()) {
            stoppedConsumers.add(EventType.OPPGAVE)
        }
        if(appContext.statusoppdateringCountAivenConsumer.isStopped()) {
            stoppedConsumers.add(EventType.STATUSOPPDATERING)
        }
        if(appContext.feilresponsCountAivenConsumer.isStopped()) {
            stoppedConsumers.add(EventType.FEILRESPONS)
        }
        return stoppedConsumers
    }

    suspend fun restartConsumersAiven(stoppedConsumers: MutableList<EventType>) {
        log.warn("Følgende konsumere på Aiven hadde stoppet ${stoppedConsumers}, de(n) vil bli restartet.")
        KafkaConsumerSetup.restartConsumersAiven(appContext)
        log.info("$stoppedConsumers konsumern(e) har blitt restartet.")
    }

    suspend fun stop() {
        log.info("Stopper periodisk sjekking av at konsumerne kjører.")
        job.cancelAndJoin()
    }

    fun isCompleted(): Boolean {
        return job.isCompleted
    }

    fun status(): HealthStatus {
        return when (job.isActive) {
            true -> HealthStatus("PeriodicConsumerPollingCheck", Status.OK, "Checker is running", false)
            false -> HealthStatus("PeriodicConsumerPollingCheck", Status.ERROR, "Checker is not running", false)
        }
    }
}
