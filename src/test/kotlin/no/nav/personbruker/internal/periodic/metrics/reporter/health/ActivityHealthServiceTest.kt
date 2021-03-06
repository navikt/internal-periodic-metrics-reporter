package no.nav.personbruker.internal.periodic.metrics.reporter.health

import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.activity.ActivityLevel
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.activity.ActivityState
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.activity.TopicActivityService
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class ActivityHealthServiceTest {

    private val beskjedTopicActivityService: TopicActivityService = mockk()
    private val oppgaveTopicActivityService: TopicActivityService = mockk()
    private val innboksTopicActivityService: TopicActivityService = mockk()
    private val doneTopicActivityService: TopicActivityService = mockk()
    private val statusoppdateringTopicActivityService: TopicActivityService = mockk()

    private val decider: ActivityHealthDecider = mockk()
    private val config: ActivityMonitoringToggles = mockk()

    private val activityHealthService = ActivityHealthService(
            beskjedTopicActivityService,
            oppgaveTopicActivityService,
            innboksTopicActivityService,
            doneTopicActivityService,
            statusoppdateringTopicActivityService,
            decider,
            config
    )

    @AfterEach
    fun cleanUp() {
        clearMocks(beskjedTopicActivityService)
        clearMocks(oppgaveTopicActivityService)
        clearMocks(innboksTopicActivityService)
        clearMocks(doneTopicActivityService)
        clearMocks(statusoppdateringTopicActivityService)
        clearMocks(config)
    }

    @Test
    fun `Should only monitor specific topic activity if requested`() {
        every { config.monitorBeskjedActivity } returns true
        every { config.monitorOppgaveActivity } returns true
        every { config.monitorInnboksActivity } returns false
        every { config.monitorDoneActivity } returns false
        every { config.monitorStatusoppdateringActivity } returns false

        val healthyActivity = ActivityState(ActivityLevel.HIGH, 0)

        every { decider.stateIsHealthy(healthyActivity) } returns true

        every { beskjedTopicActivityService.getActivityState() } returns healthyActivity
        every { oppgaveTopicActivityService.getActivityState() } returns healthyActivity


        val result = activityHealthService.assertTopicActivityHealth()

        verify(exactly = 1) { beskjedTopicActivityService.getActivityState() }
        verify(exactly = 1) { oppgaveTopicActivityService.getActivityState() }
        verify(exactly = 0) { innboksTopicActivityService.getActivityState() }
        verify(exactly = 0) { doneTopicActivityService.getActivityState() }
        verify(exactly = 0) { statusoppdateringTopicActivityService.getActivityState() }

        result `should be equal to` true
    }

    @Test
    fun `Should report unhealthy if any one service reports being unhealthy`() {
        every { config.monitorBeskjedActivity } returns true
        every { config.monitorOppgaveActivity } returns true
        every { config.monitorInnboksActivity } returns true
        every { config.monitorDoneActivity } returns true
        every { config.monitorStatusoppdateringActivity } returns true

        val threshold = 10

        val healthyActivity = ActivityState(ActivityLevel.HIGH, 0)
        val unhealthyActivity = ActivityState(ActivityLevel.HIGH, threshold + 1)

        every { decider.stateIsHealthy(healthyActivity) } returns true
        every { decider.stateIsHealthy(unhealthyActivity) } returns false

        every { beskjedTopicActivityService.getActivityState() } returns healthyActivity
        every { oppgaveTopicActivityService.getActivityState() } returns healthyActivity
        every { innboksTopicActivityService.getActivityState() } returns unhealthyActivity
        every { doneTopicActivityService.getActivityState() } returns healthyActivity
        every { statusoppdateringTopicActivityService.getActivityState() } returns healthyActivity


        val result = activityHealthService.assertTopicActivityHealth()

        verify(exactly = 1) { beskjedTopicActivityService.getActivityState() }
        verify(exactly = 1) { oppgaveTopicActivityService.getActivityState() }
        verify(exactly = 1) { innboksTopicActivityService.getActivityState() }
        verify(exactly = 1) { doneTopicActivityService.getActivityState() }
        verify(exactly = 1) { statusoppdateringTopicActivityService.getActivityState() }

        result `should be equal to` false
    }
}
