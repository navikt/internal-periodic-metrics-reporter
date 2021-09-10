package no.nav.personbruker.internal.periodic.metrics.reporter.metrics

import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.db.count.DbCountingMetricsSessionObjectMother
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.TopicMetricsSessionObjectMother

object CountingMetricsSessionsObjectMother {

    fun giveMeDatabaseSessionsForAllInternalEventTypes(): CountingMetricsSessions {
        return CountingMetricsSessions().apply {
            put(EventType.BESKJED, DbCountingMetricsSessionObjectMother.giveMeBeskjedInternSessionWithOneCountedEvent())
            put(EventType.DONE, DbCountingMetricsSessionObjectMother.giveMeDoneInternSessionWithTwoCountedEvents())
            put(EventType.INNBOKS, DbCountingMetricsSessionObjectMother.giveMeInnboksInternSessionWithThreeCountedEvents())
            put(EventType.OPPGAVE, DbCountingMetricsSessionObjectMother.giveMeOppgaveInternSessionWithFourCountedEvents())
            put(EventType.STATUSOPPDATERING, DbCountingMetricsSessionObjectMother.giveMeStatusoppdateringInternSessionWithFourCountedEvents())
        }
    }


    fun giveMeDatabaseSessionsForAllInternalEventTypesExceptForInnboks(): CountingMetricsSessions {
        return CountingMetricsSessions().apply {
            put(EventType.BESKJED, DbCountingMetricsSessionObjectMother.giveMeBeskjedInternSessionWithOneCountedEvent())
            put(EventType.DONE, DbCountingMetricsSessionObjectMother.giveMeDoneInternSessionWithTwoCountedEvents())
            put(EventType.OPPGAVE, DbCountingMetricsSessionObjectMother.giveMeOppgaveInternSessionWithFourCountedEvents())
            put(EventType.STATUSOPPDATERING, DbCountingMetricsSessionObjectMother.giveMeStatusoppdateringInternSessionWithFourCountedEvents())
        }
    }

    fun giveMeTopicSessionsForAllInternalEventTypes(): CountingMetricsSessions {
        return CountingMetricsSessions().apply {
            put(EventType.BESKJED, TopicMetricsSessionObjectMother.giveMeBeskjedInternSessionWithTwoCountedEvents())
            put(EventType.DONE, TopicMetricsSessionObjectMother.giveMeDoneInternSessionWithThreeCountedEvent())
            put(EventType.INNBOKS, TopicMetricsSessionObjectMother.giveMeInnboksInternSessionWithFourCountedEvent())
            put(EventType.OPPGAVE, TopicMetricsSessionObjectMother.giveMeOppgaveInternSessionWithFiveCountedEvent())
            put(EventType.STATUSOPPDATERING, TopicMetricsSessionObjectMother.giveMeStatusoppdateringInternSessionWithFiveCountedEvent())

        }
    }

    fun giveMeTopicSessionsWithSingleEventForAllInternalEventTypes(): CountingMetricsSessions {
        return CountingMetricsSessions().apply {
            put(EventType.BESKJED, TopicMetricsSessionObjectMother.giveMeBeskjedSessionWithOneCountedEvent())
            put(EventType.DONE, TopicMetricsSessionObjectMother.giveMeDoneSessionWithOneCountedEvent())
            put(EventType.INNBOKS, TopicMetricsSessionObjectMother.giveMeInnboksSessionWithOneCountedEvent())
            put(EventType.OPPGAVE, TopicMetricsSessionObjectMother.giveMeOppgaveSessionWithOneCountedEvent())
            put(EventType.STATUSOPPDATERING, TopicMetricsSessionObjectMother.giveMeStatusoppdateringSessionWithOneCountedEvent())
        }
    }
}
