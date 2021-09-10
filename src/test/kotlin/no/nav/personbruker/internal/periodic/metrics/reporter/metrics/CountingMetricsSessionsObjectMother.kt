package no.nav.personbruker.internal.periodic.metrics.reporter.metrics

import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.db.count.DbCountingMetricsSessionObjectMother
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.TopicMetricsSessionObjectMother

object CountingMetricsSessionsObjectMother {

    fun giveMeDatabaseSessionsForAllExternalEventTypes(): CountingMetricsSessions {
        return CountingMetricsSessions().apply {
            put(EventType.BESKJED, DbCountingMetricsSessionObjectMother.giveMeBeskjedSessionWithOneCountedEvent())
            put(EventType.DONE, DbCountingMetricsSessionObjectMother.giveMeDoneSessionWithTwoCountedEvents())
            put(EventType.INNBOKS, DbCountingMetricsSessionObjectMother.giveMeInnboksSessionWithThreeCountedEvents())
            put(EventType.OPPGAVE, DbCountingMetricsSessionObjectMother.giveMeOppgaveSessionWithFourCountedEvents())
            put(EventType.STATUSOPPDATERING, DbCountingMetricsSessionObjectMother.giveMeStatusoppdateringSessionWithFourCountedEvents())
        }
    }

    fun giveMeDatabaseSessionsForAllInternalEventTypes(): CountingMetricsSessions {
        return CountingMetricsSessions().apply {
            put(EventType.BESKJED, DbCountingMetricsSessionObjectMother.giveMeBeskjedInternSessionWithOneCountedEvent())
            put(EventType.DONE, DbCountingMetricsSessionObjectMother.giveMeDoneInternSessionWithTwoCountedEvents())
            put(EventType.INNBOKS, DbCountingMetricsSessionObjectMother.giveMeInnboksInternSessionWithThreeCountedEvents())
            put(EventType.OPPGAVE, DbCountingMetricsSessionObjectMother.giveMeOppgaveInternSessionWithFourCountedEvents())
            put(EventType.STATUSOPPDATERING, DbCountingMetricsSessionObjectMother.giveMeStatusoppdateringInternSessionWithFourCountedEvents())
        }
    }

    fun giveMeDatabaseSessionsForAllExternalEventTypesExceptForInnboks(): CountingMetricsSessions {
        return CountingMetricsSessions().apply {
            put(EventType.BESKJED, DbCountingMetricsSessionObjectMother.giveMeBeskjedSessionWithOneCountedEvent())
            put(EventType.DONE, DbCountingMetricsSessionObjectMother.giveMeDoneSessionWithTwoCountedEvents())
            put(EventType.OPPGAVE, DbCountingMetricsSessionObjectMother.giveMeOppgaveSessionWithFourCountedEvents())
            put(EventType.STATUSOPPDATERING, DbCountingMetricsSessionObjectMother.giveMeStatusoppdateringSessionWithFourCountedEvents())
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

    fun giveMeTopicSessionsForAllExternalEventTypes(): CountingMetricsSessions {
        return CountingMetricsSessions().apply {
            put(EventType.BESKJED, TopicMetricsSessionObjectMother.giveMeBeskjedSessionWithTwoCountedEvents())
            put(EventType.DONE, TopicMetricsSessionObjectMother.giveMeDoneSessionWithThreeCountedEvent())
            put(EventType.INNBOKS, TopicMetricsSessionObjectMother.giveMeInnboksSessionWithFourCountedEvent())
            put(EventType.OPPGAVE, TopicMetricsSessionObjectMother.giveMeOppgaveSessionWithFiveCountedEvent())
            put(EventType.STATUSOPPDATERING, TopicMetricsSessionObjectMother.giveMeStatusoppdateringSessionWithFiveCountedEvent())
        }
    }

    fun giveMeTopicSessionsForAllExternalEventTypesExceptForInnboks(): CountingMetricsSessions {
        return CountingMetricsSessions().apply {
            put(EventType.BESKJED, TopicMetricsSessionObjectMother.giveMeBeskjedSessionWithTwoCountedEvents())
            put(EventType.DONE, TopicMetricsSessionObjectMother.giveMeDoneSessionWithThreeCountedEvent())
            put(EventType.OPPGAVE, TopicMetricsSessionObjectMother.giveMeOppgaveSessionWithFiveCountedEvent())
            put(EventType.STATUSOPPDATERING, TopicMetricsSessionObjectMother.giveMeStatusoppdateringSessionWithFiveCountedEvent())
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

    fun giveMeTopicSessionsWithSingleEventForFeilrespons(): CountingMetricsSessions {
        return CountingMetricsSessions().apply {
            put(EventType.FEILRESPONS, TopicMetricsSessionObjectMother.giveMeFeilresponsSessionWithOneCountedEvent())
        }
    }

    fun giveMeTopicSessionsWithFiveEventsForFeilrespons(): CountingMetricsSessions {
        return CountingMetricsSessions().apply {
            put(EventType.FEILRESPONS, TopicMetricsSessionObjectMother.giveMeFeilresponsSessionWithFiveCountedEvent())
        }
    }
}
