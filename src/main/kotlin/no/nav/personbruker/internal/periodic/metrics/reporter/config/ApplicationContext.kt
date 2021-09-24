package no.nav.personbruker.internal.periodic.metrics.reporter.config

import no.nav.brukernotifikasjon.schemas.internal.NokkelIntern
import no.nav.personbruker.internal.periodic.metrics.reporter.common.HandlerConsumer
import no.nav.personbruker.internal.periodic.metrics.reporter.common.kafka.polling.PeriodicConsumerCheck
import no.nav.personbruker.internal.periodic.metrics.reporter.health.ActivityHealthDecider
import no.nav.personbruker.internal.periodic.metrics.reporter.health.ActivityHealthService
import no.nav.personbruker.internal.periodic.metrics.reporter.health.ActivityMonitoringToggles
import no.nav.personbruker.internal.periodic.metrics.reporter.health.HealthService
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count.CacheCountingMetricsProbe
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count.CacheEventCounterGCPService
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count.CacheMetricsReporter
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.TopicEventCounterAivenService
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.TopicEventTypeCounter
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.TopicMetricsReporter
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.activity.TopicActivityService
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.resolveMetricsReporter
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.submitter.MetricsSubmitterService
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.submitter.PeriodicMetricsSubmitter
import org.apache.avro.generic.GenericRecord
import org.slf4j.LoggerFactory
import java.util.*

class ApplicationContext {

    private val log = LoggerFactory.getLogger(ApplicationContext::class.java)

    val environment = Environment()
    private val httpClient = HttpClientBuilder.build()
    private val handlerConsumer = HandlerConsumer(httpClient, environment.eventHandlerURL)

    val cacheEventCountingMetricsProbe = CacheCountingMetricsProbe()
    val metricsReporter = resolveMetricsReporter(environment)
    val cacheEventCounterGCPService = CacheEventCounterGCPService(cacheEventCountingMetricsProbe, handlerConsumer)

    val healthService = HealthService(this)

    val cacheMetricsReporter = CacheMetricsReporter(metricsReporter)
    val kafkaMetricsReporter = TopicMetricsReporter(metricsReporter)

    val beskjedKafkaPropsAiven = Kafka.counterConsumerAivenProps(environment, EventType.BESKJED_INTERN)
    var beskjedCountAivenConsumer = initializeCountConsumerAiven(beskjedKafkaPropsAiven, environment.beskjedInternTopicName)
    val beskjedTopicActivityService = TopicActivityService(environment.activityHistoryLength)
    val beskjedAivenTopicActivityService = TopicActivityService(environment.activityHistoryLength)

    val beskjedCounterAiven = TopicEventTypeCounter(
            beskjedCountAivenConsumer,
            beskjedAivenTopicActivityService,
            EventType.BESKJED_INTERN,
            environment.deltaCountingEnabled
    )

    val oppgaveKafkaPropsAiven = Kafka.counterConsumerAivenProps(environment, EventType.OPPGAVE_INTERN)
    var oppgaveCountAivenConsumer = initializeCountConsumerAiven(oppgaveKafkaPropsAiven, environment.oppgaveInternTopicName)
    val oppgaveTopicActivityService = TopicActivityService(environment.activityHistoryLength)
    val oppgaveAivenTopicActivityService = TopicActivityService(environment.activityHistoryLength)

    val oppgaveCounterAiven = TopicEventTypeCounter(
            oppgaveCountAivenConsumer,
            oppgaveAivenTopicActivityService,
            EventType.OPPGAVE_INTERN,
            environment.deltaCountingEnabled
    )

    val innboksKafkaPropsAiven = Kafka.counterConsumerAivenProps(environment, EventType.INNBOKS_INTERN)
    var innboksCountAivenConsumer = initializeCountConsumerAiven(innboksKafkaPropsAiven, environment.innboksInternTopicName)
    val innboksTopicActivityService = TopicActivityService(environment.activityHistoryLength)
    val innboksAivenTopicActivityService = TopicActivityService(environment.activityHistoryLength)

    val innboksCounterAiven = TopicEventTypeCounter(
            innboksCountAivenConsumer,
            innboksAivenTopicActivityService,
            EventType.INNBOKS_INTERN,
            environment.deltaCountingEnabled
    )

    val statusoppdateringKafkaPropsAiven = Kafka.counterConsumerAivenProps(environment, EventType.STATUSOPPDATERING_INTERN)
    var statusoppdateringCountAivenConsumer = initializeCountConsumerAiven(statusoppdateringKafkaPropsAiven, environment.statusoppdateringInternTopicName)
    val statusoppdateringTopicActivityService = TopicActivityService(environment.activityHistoryLength)
    val statusoppdateringAivenTopicActivityService = TopicActivityService(environment.activityHistoryLength)

    val statusoppdateringCounterAiven = TopicEventTypeCounter(
            statusoppdateringCountAivenConsumer,
            statusoppdateringAivenTopicActivityService,
            EventType.STATUSOPPDATERING_INTERN,
            environment.deltaCountingEnabled
    )

    val doneKafkaPropsAiven = Kafka.counterConsumerAivenProps(environment, EventType.DONE__INTERN)
    var doneCountAivenConsumer = initializeCountConsumerAiven(doneKafkaPropsAiven, environment.doneInternTopicName)
    val doneTopicActivityService = TopicActivityService(environment.activityHistoryLength)
    val doneAivenTopicActivityService = TopicActivityService(environment.activityHistoryLength)

    val doneCounterAiven = TopicEventTypeCounter(
            doneCountAivenConsumer,
            doneAivenTopicActivityService,
            EventType.DONE__INTERN,
            environment.deltaCountingEnabled
    )

    val topicEventCounterServiceAiven = TopicEventCounterAivenService(
            beskjedCounter = beskjedCounterAiven,
            innboksCounter = innboksCounterAiven,
            oppgaveCounter = oppgaveCounterAiven,
            statusoppdateringCounter = statusoppdateringCounterAiven,
            doneCounter = doneCounterAiven
    )

    val metricsSubmitterService = MetricsSubmitterService(
            cacheEventCounterGCPService = cacheEventCounterGCPService,
            topicEventCounterServiceAiven = topicEventCounterServiceAiven,
            cacheMetricsReporter = cacheMetricsReporter,
            kafkaMetricsReporter = kafkaMetricsReporter
    )

    val activityHealthServiceConfig = ActivityMonitoringToggles(
            monitorBeskjedActivity = environment.monitorBeskjedActivity,
            monitorOppgaveActivity = environment.monitorOppgaveActivity,
            monitorInnboksActivity = environment.monitorInnboksActivity,
            monitorDoneActivity = environment.monitorDoneActivity,
            monitorStatusoppdateringActivity = environment.monitorStatusoppdateringActivity
    )

    val activityHealthDecider = ActivityHealthDecider(
            lowActivityStreakThreshold = environment.lowActivityStreakThreshold,
            moderateActivityStreakThreshold = environment.moderateActivityStreakThreshold,
            highActivityStreakThreshold = environment.highActivityStreakThreshold
    )

    val activityHealthService = ActivityHealthService(
            beskjedTopicActivityService = beskjedTopicActivityService,
            oppgaveTopicActivityService = oppgaveTopicActivityService,
            innboksTopicActivityService = innboksTopicActivityService,
            doneTopicActivityService = doneTopicActivityService,
            statusoppdateringTopicActivityService = statusoppdateringTopicActivityService,
            activityHealthDecider = activityHealthDecider,
            monitoringToggles = activityHealthServiceConfig
    )

    var periodicMetricsSubmitter = initializePeriodicMetricsSubmitter()
    var periodicConsumerCheck = initializePeriodicConsumerCheck()

    private fun initializePeriodicConsumerCheck() =
            PeriodicConsumerCheck(this)

    private fun initializeCountConsumerAiven(kafkaProps: Properties, topic: String) =
            KafkaConsumerSetup.setupCountConsumer<NokkelIntern, GenericRecord>(kafkaProps, topic)

    private fun initializePeriodicMetricsSubmitter(): PeriodicMetricsSubmitter =
            PeriodicMetricsSubmitter(metricsSubmitterService, environment.countingIntervalMinutes)

    fun reinitializePeriodicMetricsSubmitter() {
        if (periodicMetricsSubmitter.isCompleted()) {
            periodicMetricsSubmitter = initializePeriodicMetricsSubmitter()
            log.info("periodicMetricsSubmitter har blitt reinstansiert.")
        } else {
            log.warn("periodicMetricsSubmitter kunne ikke bli reinstansiert fordi den fortsatt er aktiv.")
        }
    }

    fun reinitializePeriodicConsumerCheck() {
        if (periodicConsumerCheck.isCompleted()) {
            periodicConsumerCheck = initializePeriodicConsumerCheck()
            log.info("periodicConsumerCheck har blitt reinstansiert.")
        } else {
            log.warn("periodicConsumerCheck kunne ikke bli reinstansiert fordi den fortsatt er aktiv.")
        }
    }

    fun reinitializeConsumersAiven() {
        if (beskjedCountAivenConsumer.isCompleted()) {
            beskjedCountAivenConsumer = initializeCountConsumerAiven(beskjedKafkaPropsAiven, environment.beskjedInternTopicName)
            log.info("beskjedCountConsumer på Aiven har blitt reinstansiert.")
        } else {
            log.warn("beskjedCountConsumer på Aiven kunne ikke bli reinstansiert fordi den fortsatt er aktiv.")
        }

        if (oppgaveCountAivenConsumer.isCompleted()) {
            oppgaveCountAivenConsumer = initializeCountConsumerAiven(oppgaveKafkaPropsAiven, environment.oppgaveInternTopicName)
            log.info("oppgaveCountConsumer på Aiven har blitt reinstansiert.")
        } else {
            log.warn("oppgaveCountConsumer på Aiven kunne ikke bli reinstansiert fordi den fortsatt er aktiv.")
        }

        if (innboksCountAivenConsumer.isCompleted()) {
            innboksCountAivenConsumer = initializeCountConsumerAiven(innboksKafkaPropsAiven, environment.innboksInternTopicName)
            log.info("innboksCountConsumer på Aiven blitt reinstansiert.")
        } else {
            log.warn("innboksCountConsumer på Aiven kunne ikke bli reinstansiert fordi den fortsatt er aktiv.")
        }

        if (statusoppdateringCountAivenConsumer.isCompleted()) {
            statusoppdateringCountAivenConsumer = initializeCountConsumerAiven(statusoppdateringKafkaPropsAiven, environment.statusoppdateringInternTopicName)
            log.info("statusoppdateringCountConsumer på Aiven blitt reinstansiert.")
        } else {
            log.warn("statusoppdateringCountConsumer på Aiven kunne ikke bli reinstansiert fordi den fortsatt er aktiv.")
        }

        if (doneCountAivenConsumer.isCompleted()) {
            doneCountAivenConsumer = initializeCountConsumerAiven(doneKafkaPropsAiven, environment.doneInternTopicName)
            log.info("doneConsumer på Aiven har blitt reinstansiert.")
        } else {
            log.warn("doneConsumer på Aiven kunne ikke bli reinstansiert fordi den fortsatt er aktiv.")
        }

    }
}

