package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.activity

data class ActivityState(
        val recentActivityLevel: ActivityLevel,
        val inactivityStreak: Int
)
