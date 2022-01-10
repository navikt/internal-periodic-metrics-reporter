package no.nav.personbruker.internal.periodic.metrics.reporter.metrics

import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count.CacheCountingMetricsSessionObjectMother
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.TopicMetricsSessionObjectMother

object CountingMetricsSessionsObjectMother {

    fun giveMeCacheSessionsForAllInternalEventTypes(): CacheCountingMetricsSessions {
        return CacheCountingMetricsSessions().apply {
            put(EventType.BESKJED_INTERN, CacheCountingMetricsSessionObjectMother.giveMeBeskjedInternSessionWithOneCountedEvent())
            put(EventType.DONE_INTERN, CacheCountingMetricsSessionObjectMother.giveMeDoneInternSessionWithTwoCountedEvents())
            put(EventType.INNBOKS_INTERN, CacheCountingMetricsSessionObjectMother.giveMeInnboksInternSessionWithThreeCountedEvents())
            put(EventType.OPPGAVE_INTERN, CacheCountingMetricsSessionObjectMother.giveMeOppgaveInternSessionWithFourCountedEvents())
            put(EventType.STATUSOPPDATERING_INTERN, CacheCountingMetricsSessionObjectMother.giveMeStatusoppdateringInternSessionWithFourCountedEvents())
        }
    }


    fun giveMeCacheSessionsForAllInternalEventTypesExceptForInnboks(): CacheCountingMetricsSessions {
        return CacheCountingMetricsSessions().apply {
            put(EventType.BESKJED_INTERN, CacheCountingMetricsSessionObjectMother.giveMeBeskjedInternSessionWithOneCountedEvent())
            put(EventType.DONE_INTERN, CacheCountingMetricsSessionObjectMother.giveMeDoneInternSessionWithTwoCountedEvents())
            put(EventType.OPPGAVE_INTERN, CacheCountingMetricsSessionObjectMother.giveMeOppgaveInternSessionWithFourCountedEvents())
            put(EventType.STATUSOPPDATERING_INTERN, CacheCountingMetricsSessionObjectMother.giveMeStatusoppdateringInternSessionWithFourCountedEvents())
        }
    }

    fun giveMeTopicSessionsForAllInternalEventTypes(): TopicMetricsSessions {
        return TopicMetricsSessions().apply {
            put(EventType.BESKJED_INTERN, TopicMetricsSessionObjectMother.giveMeBeskjedInternSessionWithTwoCountedEvents())
            put(EventType.DONE_INTERN, TopicMetricsSessionObjectMother.giveMeDoneInternSessionWithThreeCountedEvent())
            put(EventType.INNBOKS_INTERN, TopicMetricsSessionObjectMother.giveMeInnboksInternSessionWithFourCountedEvent())
            put(EventType.OPPGAVE_INTERN, TopicMetricsSessionObjectMother.giveMeOppgaveInternSessionWithFiveCountedEvent())
            put(EventType.STATUSOPPDATERING_INTERN, TopicMetricsSessionObjectMother.giveMeStatusoppdateringInternSessionWithFiveCountedEvent())

        }
    }

    fun giveMeTopicSessionsWithSingleEventForAllInternalEventTypes(): TopicMetricsSessions {
        return TopicMetricsSessions().apply {
            put(EventType.BESKJED_INTERN, TopicMetricsSessionObjectMother.giveMeBeskjedSessionWithOneCountedEvent())
            put(EventType.DONE_INTERN, TopicMetricsSessionObjectMother.giveMeDoneSessionWithOneCountedEvent())
            put(EventType.INNBOKS_INTERN, TopicMetricsSessionObjectMother.giveMeInnboksSessionWithOneCountedEvent())
            put(EventType.OPPGAVE_INTERN, TopicMetricsSessionObjectMother.giveMeOppgaveSessionWithOneCountedEvent())
            put(EventType.STATUSOPPDATERING_INTERN, TopicMetricsSessionObjectMother.giveMeStatusoppdateringSessionWithOneCountedEvent())
        }
    }

    fun giveMeTopicSessionsForAllInternalEventTypesExceptForInnboks(): TopicMetricsSessions {
        return TopicMetricsSessions().apply {
            put(EventType.BESKJED_INTERN, TopicMetricsSessionObjectMother.giveMeBeskjedSessionWithTwoCountedEvents())
            put(EventType.DONE_INTERN, TopicMetricsSessionObjectMother.giveMeDoneSessionWithThreeCountedEvent())
            put(EventType.OPPGAVE_INTERN, TopicMetricsSessionObjectMother.giveMeOppgaveSessionWithFiveCountedEvent())
            put(EventType.STATUSOPPDATERING_INTERN, TopicMetricsSessionObjectMother.giveMeStatusoppdateringSessionWithFiveCountedEvent())
        }
    }
}
