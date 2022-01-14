package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic

import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.Producer
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.KafkaEventIdentifier

object TopicMetricsSessionObjectMother {

    fun giveMeBeskjedSessionWithOneCountedEvent(): TopicMetricsSession {
        val beskjedSession = TopicMetricsSession(EventType.BESKJED_INTERN)
        beskjedSession.countEvent(KafkaEventIdentifier("1", "dummyNamespace", "dummyAppnavn"))
        return beskjedSession
    }

    fun giveMeBeskjedSessionWithTwoCountedEvents(): TopicMetricsSession {
        val beskjedSession = TopicMetricsSession(EventType.BESKJED_INTERN)
        beskjedSession.countEvent(KafkaEventIdentifier("21", "dummyNamespace", "dummyAppnavn"))
        beskjedSession.countEvent(KafkaEventIdentifier("22", "dummyNamespace", "dummyAppnavn"))
        return beskjedSession
    }

    fun giveMeBeskjedInternSessionWithTwoCountedEvents(): TopicMetricsSession {
        val beskjedInternSession = TopicMetricsSession(EventType.BESKJED_INTERN)
        beskjedInternSession.countEvent(KafkaEventIdentifier("21", "dummyNamespace", "dummyAppnavn"))
        beskjedInternSession.countEvent(KafkaEventIdentifier("22", "dummyNamespace", "dummyAppnavn"))
        return beskjedInternSession
    }

    fun giveMeDoneSessionWithOneCountedEvent(): TopicMetricsSession {
        val doneSession = TopicMetricsSession(EventType.DONE_INTERN)
        doneSession.countEvent(KafkaEventIdentifier("21", "dummyNamespace", "dummyAppnavn"))
        return doneSession
    }

    fun giveMeDoneSessionWithThreeCountedEvent(): TopicMetricsSession {
        val doneSession = TopicMetricsSession(EventType.DONE_INTERN)
        doneSession.countEvent(KafkaEventIdentifier("31", "dummyNamespace", "dummyAppnavn"))
        doneSession.countEvent(KafkaEventIdentifier("32", "dummyNamespace", "dummyAppnavn"))
        doneSession.countEvent(KafkaEventIdentifier("33", "dummyNamespace", "dummyAppnavn"))
        return doneSession
    }

    fun giveMeDoneInternSessionWithThreeCountedEvent(): TopicMetricsSession {
        val doneSession = TopicMetricsSession(EventType.DONE_INTERN)
        doneSession.countEvent(KafkaEventIdentifier("31", "dummyNamespace", "dummyAppnavn"))
        doneSession.countEvent(KafkaEventIdentifier("32", "dummyNamespace", "dummyAppnavn"))
        doneSession.countEvent(KafkaEventIdentifier("33", "dummyNamespace", "dummyAppnavn"))
        return doneSession
    }

    fun giveMeInnboksSessionWithOneCountedEvent(): TopicMetricsSession {
        val innboksSession = TopicMetricsSession(EventType.INNBOKS_INTERN)
        innboksSession.countEvent(KafkaEventIdentifier("31", "dummyNamespace", "dummyAppnavn"))
        return innboksSession
    }

    fun giveMeInnboksSessionWithFourCountedEvent(): TopicMetricsSession {
        val innboksSession = TopicMetricsSession(EventType.INNBOKS_INTERN)
        innboksSession.countEvent(KafkaEventIdentifier("41", "dummyNamespace", "dummyAppnavn"))
        innboksSession.countEvent(KafkaEventIdentifier("42", "dummyNamespace", "dummyAppnavn"))
        innboksSession.countEvent(KafkaEventIdentifier("43", "dummyNamespace", "dummyAppnavn"))
        innboksSession.countEvent(KafkaEventIdentifier("44", "dummyNamespace", "dummyAppnavn"))
        return innboksSession
    }

    fun giveMeInnboksInternSessionWithFourCountedEvent(): TopicMetricsSession {
        val innboksSession = TopicMetricsSession(EventType.INNBOKS_INTERN)
        innboksSession.countEvent(KafkaEventIdentifier("41", "dummyNamespace", "dummyAppnavn"))
        innboksSession.countEvent(KafkaEventIdentifier("42", "dummyNamespace", "dummyAppnavn"))
        innboksSession.countEvent(KafkaEventIdentifier("43", "dummyNamespace", "dummyAppnavn"))
        innboksSession.countEvent(KafkaEventIdentifier("44", "dummyNamespace", "dummyAppnavn"))
        return innboksSession
    }

    fun giveMeOppgaveSessionWithOneCountedEvent(): TopicMetricsSession {
        val oppgaveSession = TopicMetricsSession(EventType.OPPGAVE_INTERN)
        oppgaveSession.countEvent(KafkaEventIdentifier("41", "dummyNamespace", "dummyAppnavn"))
        return oppgaveSession
    }

    fun giveMeOppgaveSessionWithFiveCountedEvent(): TopicMetricsSession {
        val oppgaveSession = TopicMetricsSession(EventType.OPPGAVE_INTERN)
        oppgaveSession.countEvent(KafkaEventIdentifier("51", "dummyNamespace", "dummyAppnavn"))
        oppgaveSession.countEvent(KafkaEventIdentifier("52", "dummyNamespace", "dummyAppnavn"))
        oppgaveSession.countEvent(KafkaEventIdentifier("53", "dummyNamespace", "dummyAppnavn"))
        oppgaveSession.countEvent(KafkaEventIdentifier("54", "dummyNamespace", "dummyAppnavn"))
        oppgaveSession.countEvent(KafkaEventIdentifier("55", "dummyNamespace", "dummyAppnavn"))
        return oppgaveSession
    }

    fun giveMeOppgaveInternSessionWithFiveCountedEvent(): TopicMetricsSession {
        val oppgaveSession = TopicMetricsSession(EventType.OPPGAVE_INTERN)
        oppgaveSession.countEvent(KafkaEventIdentifier("51", "dummyNamespace", "dummyAppnavn"))
        oppgaveSession.countEvent(KafkaEventIdentifier("52", "dummyNamespace", "dummyAppnavn"))
        oppgaveSession.countEvent(KafkaEventIdentifier("53", "dummyNamespace", "dummyAppnavn"))
        oppgaveSession.countEvent(KafkaEventIdentifier("54", "dummyNamespace", "dummyAppnavn"))
        oppgaveSession.countEvent(KafkaEventIdentifier("55", "dummyNamespace", "dummyAppnavn"))
        return oppgaveSession
    }

    fun giveMeStatusoppdateringSessionWithOneCountedEvent(): TopicMetricsSession {
        val statusoppdateringSession = TopicMetricsSession(EventType.STATUSOPPDATERING_INTERN)
        statusoppdateringSession.countEvent(KafkaEventIdentifier("61", "dummyNamespace", "dummyAppnavn"))
        return statusoppdateringSession
    }

    fun giveMeStatusoppdateringSessionWithFiveCountedEvent(): TopicMetricsSession {
        val statusoppdateringSession = TopicMetricsSession(EventType.STATUSOPPDATERING_INTERN)
        statusoppdateringSession.countEvent(KafkaEventIdentifier("61", "dummyNamespace", "dummyAppnavn"))
        statusoppdateringSession.countEvent(KafkaEventIdentifier("62", "dummyNamespace", "dummyAppnavn"))
        statusoppdateringSession.countEvent(KafkaEventIdentifier("63", "dummyNamespace", "dummyAppnavn"))
        statusoppdateringSession.countEvent(KafkaEventIdentifier("64", "dummyNamespace", "dummyAppnavn"))
        statusoppdateringSession.countEvent(KafkaEventIdentifier("65", "dummyNamespace", "dummyAppnavn"))
        return statusoppdateringSession
    }

    fun giveMeStatusoppdateringInternSessionWithFiveCountedEvent(): TopicMetricsSession {
        val statusoppdateringSession = TopicMetricsSession(EventType.STATUSOPPDATERING_INTERN)
        statusoppdateringSession.countEvent(KafkaEventIdentifier("61", "dummyNamespace", "dummyAppnavn"))
        statusoppdateringSession.countEvent(KafkaEventIdentifier("62", "dummyNamespace", "dummyAppnavn"))
        statusoppdateringSession.countEvent(KafkaEventIdentifier("63", "dummyNamespace", "dummyAppnavn"))
        statusoppdateringSession.countEvent(KafkaEventIdentifier("64", "dummyNamespace", "dummyAppnavn"))
        statusoppdateringSession.countEvent(KafkaEventIdentifier("65", "dummyNamespace", "dummyAppnavn"))
        return statusoppdateringSession
    }

    fun giveMeATopicSessionWithConfiguration(type: EventType, config: Map<Producer, Int>): TopicMetricsSession {
        val session = TopicMetricsSession(type)

        unrollAndTransformToEvents(config).forEach { event ->
            session.countEvent(event)
        }

        return session
    }

    private fun unrollAndTransformToEvents(configs: Map<Producer, Int>): List<KafkaEventIdentifier> {

        return configs.entries
            .map { transformToEvents(it) }
            .flatten()
    }

    private fun transformToEvents(entry: Map.Entry<Producer, Int>): List<KafkaEventIdentifier> {
        val (producer, numberOfEvents) = entry

        return (0 until numberOfEvents).map { index ->
            KafkaEventIdentifier("$producer-$index", producer.namespace, producer.appName)
        }
    }
}
