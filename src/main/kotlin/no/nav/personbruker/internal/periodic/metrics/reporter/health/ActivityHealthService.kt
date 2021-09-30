package no.nav.personbruker.internal.periodic.metrics.reporter.health

import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.activity.ActivityLevel
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.activity.ActivityState
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.activity.TopicActivityService
import org.slf4j.LoggerFactory

class ActivityHealthService(
        private val beskjedTopicActivityService: TopicActivityService,
        private val oppgaveTopicActivityService: TopicActivityService,
        private val innboksTopicActivityService: TopicActivityService,
        private val doneTopicActivityService: TopicActivityService,
        private val statusoppdateringTopicActivityService: TopicActivityService,
        private val activityHealthDecider: ActivityHealthDecider,
        private val monitoringToggles: ActivityMonitoringToggles
) {

    private val log = LoggerFactory.getLogger(ActivityHealthService::class.java)

    fun assertTopicActivityHealth(): Boolean {
        var healthy = true

        if (monitoringToggles.monitorBeskjedActivity && !assertServiceHealth(beskjedTopicActivityService, "beskjed_intern")) {
            healthy = false
        }

        if (monitoringToggles.monitorOppgaveActivity && !assertServiceHealth(oppgaveTopicActivityService, "oppgave_intern")) {
            healthy = false
        }

        if (monitoringToggles.monitorInnboksActivity && !assertServiceHealth(innboksTopicActivityService, "innboks_intern")) {
            healthy = false
        }

        if (monitoringToggles.monitorDoneActivity && !assertServiceHealth(doneTopicActivityService, "done_intern")) {
            healthy = false
        }

        if (monitoringToggles.monitorStatusoppdateringActivity && !assertServiceHealth(statusoppdateringTopicActivityService, "statusoppdatering_intern")) {
            healthy = false
        }

        return healthy
    }

    private fun assertServiceHealth(service: TopicActivityService, topicSource: String): Boolean {
        val state = service.getActivityState()

        return if (activityHealthDecider.stateIsHealthy(state)) {
            true
        } else {
            log.warn("Topic counting for $topicSource looks unhealthy. Recent activity is ${state.recentActivityLevel} and counted zero events ${state.inactivityStreak} times in a row.")
            false
        }
    }
}

class ActivityHealthDecider(
        private val lowActivityStreakThreshold: Int,
        private val moderateActivityStreakThreshold: Int,
        private val highActivityStreakThreshold: Int

) {
    fun stateIsHealthy(state: ActivityState): Boolean {
        return when(state.recentActivityLevel) {
            ActivityLevel.HIGH -> state.inactivityStreak < highActivityStreakThreshold
            ActivityLevel.MODERATE -> state.inactivityStreak < moderateActivityStreakThreshold
            ActivityLevel.LOW -> state.inactivityStreak < lowActivityStreakThreshold
        }
    }
}

data class ActivityMonitoringToggles(
        val monitorBeskjedActivity: Boolean,
        val monitorOppgaveActivity: Boolean,
        val monitorInnboksActivity: Boolean,
        val monitorDoneActivity: Boolean,
        val monitorStatusoppdateringActivity: Boolean
)
