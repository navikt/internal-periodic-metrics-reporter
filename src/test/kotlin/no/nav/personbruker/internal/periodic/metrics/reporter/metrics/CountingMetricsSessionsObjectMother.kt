package no.nav.personbruker.internal.periodic.metrics.reporter.metrics

import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count.CacheCountingMetricsSessionObjectMother
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.TopicMetricsSessionObjectMother

object CountingMetricsSessionsObjectMother {

    fun giveMeCacheSessionsForAllInternalEventTypes(): CountingMetricsSessions {
        return CountingMetricsSessions().apply {
            put(EventType.BESKJED_INTERN, CacheCountingMetricsSessionObjectMother.giveMeBeskjedInternSessionWithOneCountedEvent())
            put(EventType.DONE__INTERN, CacheCountingMetricsSessionObjectMother.giveMeDoneInternSessionWithTwoCountedEvents())
            put(EventType.INNBOKS_INTERN, CacheCountingMetricsSessionObjectMother.giveMeInnboksInternSessionWithThreeCountedEvents())
            put(EventType.OPPGAVE_INTERN, CacheCountingMetricsSessionObjectMother.giveMeOppgaveInternSessionWithFourCountedEvents())
            put(EventType.STATUSOPPDATERING_INTERN, CacheCountingMetricsSessionObjectMother.giveMeStatusoppdateringInternSessionWithFourCountedEvents())
        }
    }


    fun giveMeCacheSessionsForAllInternalEventTypesExceptForInnboks(): CountingMetricsSessions {
        return CountingMetricsSessions().apply {
            put(EventType.BESKJED_INTERN, CacheCountingMetricsSessionObjectMother.giveMeBeskjedInternSessionWithOneCountedEvent())
            put(EventType.DONE__INTERN, CacheCountingMetricsSessionObjectMother.giveMeDoneInternSessionWithTwoCountedEvents())
            put(EventType.OPPGAVE_INTERN, CacheCountingMetricsSessionObjectMother.giveMeOppgaveInternSessionWithFourCountedEvents())
            put(EventType.STATUSOPPDATERING_INTERN, CacheCountingMetricsSessionObjectMother.giveMeStatusoppdateringInternSessionWithFourCountedEvents())
        }
    }

    fun giveMeTopicSessionsForAllInternalEventTypes(): CountingMetricsSessions {
        return CountingMetricsSessions().apply {
            put(EventType.BESKJED_INTERN, TopicMetricsSessionObjectMother.giveMeBeskjedInternSessionWithTwoCountedEvents())
            put(EventType.DONE__INTERN, TopicMetricsSessionObjectMother.giveMeDoneInternSessionWithThreeCountedEvent())
            put(EventType.INNBOKS_INTERN, TopicMetricsSessionObjectMother.giveMeInnboksInternSessionWithFourCountedEvent())
            put(EventType.OPPGAVE_INTERN, TopicMetricsSessionObjectMother.giveMeOppgaveInternSessionWithFiveCountedEvent())
            put(EventType.STATUSOPPDATERING_INTERN, TopicMetricsSessionObjectMother.giveMeStatusoppdateringInternSessionWithFiveCountedEvent())

        }
    }

    fun giveMeTopicSessionsWithSingleEventForAllInternalEventTypes(): CountingMetricsSessions {
        return CountingMetricsSessions().apply {
            put(EventType.BESKJED_INTERN, TopicMetricsSessionObjectMother.giveMeBeskjedSessionWithOneCountedEvent())
            put(EventType.DONE__INTERN, TopicMetricsSessionObjectMother.giveMeDoneSessionWithOneCountedEvent())
            put(EventType.INNBOKS_INTERN, TopicMetricsSessionObjectMother.giveMeInnboksSessionWithOneCountedEvent())
            put(EventType.OPPGAVE_INTERN, TopicMetricsSessionObjectMother.giveMeOppgaveSessionWithOneCountedEvent())
            put(EventType.STATUSOPPDATERING_INTERN, TopicMetricsSessionObjectMother.giveMeStatusoppdateringSessionWithOneCountedEvent())
        }
    }

    fun giveMeTopicSessionsForAllInternalEventTypesExceptForInnboks(): CountingMetricsSessions {
        return CountingMetricsSessions().apply {
            put(EventType.BESKJED_INTERN, TopicMetricsSessionObjectMother.giveMeBeskjedSessionWithTwoCountedEvents())
            put(EventType.DONE__INTERN, TopicMetricsSessionObjectMother.giveMeDoneSessionWithThreeCountedEvent())
            put(EventType.OPPGAVE_INTERN, TopicMetricsSessionObjectMother.giveMeOppgaveSessionWithFiveCountedEvent())
            put(EventType.STATUSOPPDATERING_INTERN, TopicMetricsSessionObjectMother.giveMeStatusoppdateringSessionWithFiveCountedEvent())
        }
    }
}
