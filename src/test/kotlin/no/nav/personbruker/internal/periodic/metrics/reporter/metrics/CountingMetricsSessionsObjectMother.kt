package no.nav.personbruker.internal.periodic.metrics.reporter.metrics

import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count.CacheCountingMetricsSessionObjectMother
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.TopicMetricsSessionObjectMother

object CountingMetricsSessionsObjectMother {

    fun giveMeDatabaseSessionsForAllInternalEventTypes(): CountingMetricsSessions {
        return CountingMetricsSessions().apply {
            put(EventType.BESKJED, CacheCountingMetricsSessionObjectMother.giveMeBeskjedInternSessionWithOneCountedEvent())
            put(EventType.DONE, CacheCountingMetricsSessionObjectMother.giveMeDoneInternSessionWithTwoCountedEvents())
            put(EventType.INNBOKS, CacheCountingMetricsSessionObjectMother.giveMeInnboksInternSessionWithThreeCountedEvents())
            put(EventType.OPPGAVE, CacheCountingMetricsSessionObjectMother.giveMeOppgaveInternSessionWithFourCountedEvents())
            put(EventType.STATUSOPPDATERING, CacheCountingMetricsSessionObjectMother.giveMeStatusoppdateringInternSessionWithFourCountedEvents())
        }
    }


    fun giveMeDatabaseSessionsForAllInternalEventTypesExceptForInnboks(): CountingMetricsSessions {
        return CountingMetricsSessions().apply {
            put(EventType.BESKJED, CacheCountingMetricsSessionObjectMother.giveMeBeskjedInternSessionWithOneCountedEvent())
            put(EventType.DONE, CacheCountingMetricsSessionObjectMother.giveMeDoneInternSessionWithTwoCountedEvents())
            put(EventType.OPPGAVE, CacheCountingMetricsSessionObjectMother.giveMeOppgaveInternSessionWithFourCountedEvents())
            put(EventType.STATUSOPPDATERING, CacheCountingMetricsSessionObjectMother.giveMeStatusoppdateringInternSessionWithFourCountedEvents())
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
