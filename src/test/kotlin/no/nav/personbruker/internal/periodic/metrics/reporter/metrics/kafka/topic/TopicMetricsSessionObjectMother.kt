package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic

import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.KafkaEventIdentifier

object TopicMetricsSessionObjectMother {

    fun giveMeBeskjedSessionWithOneCountedEvent(): TopicMetricsSession {
        val beskjedSession = TopicMetricsSession(EventType.BESKJED_INTERN)
        beskjedSession.countEvent(KafkaEventIdentifier("1", "dummyAppnavn"))
        return beskjedSession
    }

    fun giveMeBeskjedSessionWithTwoCountedEvents(): TopicMetricsSession {
        val beskjedSession = TopicMetricsSession(EventType.BESKJED_INTERN)
        beskjedSession.countEvent(KafkaEventIdentifier("21", "dummyAppnavn"))
        beskjedSession.countEvent(KafkaEventIdentifier("22", "dummyAppnavn"))
        return beskjedSession
    }

    fun giveMeBeskjedInternSessionWithTwoCountedEvents(): TopicMetricsSession {
        val beskjedInternSession = TopicMetricsSession(EventType.BESKJED_INTERN)
        beskjedInternSession.countEvent(KafkaEventIdentifier("21", "dummyAppnavn"))
        beskjedInternSession.countEvent(KafkaEventIdentifier("22", "dummyAppnavn"))
        return beskjedInternSession
    }

    fun giveMeDoneSessionWithOneCountedEvent(): TopicMetricsSession {
        val doneSession = TopicMetricsSession(EventType.DONE_INTERN)
        doneSession.countEvent(KafkaEventIdentifier("21", "dummyAppnavn"))
        return doneSession
    }

    fun giveMeDoneSessionWithThreeCountedEvent(): TopicMetricsSession {
        val doneSession = TopicMetricsSession(EventType.DONE_INTERN)
        doneSession.countEvent(KafkaEventIdentifier("31", "dummyAppnavn"))
        doneSession.countEvent(KafkaEventIdentifier("32", "dummyAppnavn"))
        doneSession.countEvent(KafkaEventIdentifier("33", "dummyAppnavn"))
        return doneSession
    }

    fun giveMeDoneInternSessionWithThreeCountedEvent(): TopicMetricsSession {
        val doneSession = TopicMetricsSession(EventType.DONE_INTERN)
        doneSession.countEvent(KafkaEventIdentifier("31", "dummyAppnavn"))
        doneSession.countEvent(KafkaEventIdentifier("32", "dummyAppnavn"))
        doneSession.countEvent(KafkaEventIdentifier("33", "dummyAppnavn"))
        return doneSession
    }

    fun giveMeInnboksSessionWithOneCountedEvent(): TopicMetricsSession {
        val innboksSession = TopicMetricsSession(EventType.INNBOKS_INTERN)
        innboksSession.countEvent(KafkaEventIdentifier("31", "dummyAppnavn"))
        return innboksSession
    }

    fun giveMeInnboksSessionWithFourCountedEvent(): TopicMetricsSession {
        val innboksSession = TopicMetricsSession(EventType.INNBOKS_INTERN)
        innboksSession.countEvent(KafkaEventIdentifier("41", "dummyAppnavn"))
        innboksSession.countEvent(KafkaEventIdentifier("42", "dummyAppnavn"))
        innboksSession.countEvent(KafkaEventIdentifier("43", "dummyAppnavn"))
        innboksSession.countEvent(KafkaEventIdentifier("44", "dummyAppnavn"))
        return innboksSession
    }

    fun giveMeInnboksInternSessionWithFourCountedEvent(): TopicMetricsSession {
        val innboksSession = TopicMetricsSession(EventType.INNBOKS_INTERN)
        innboksSession.countEvent(KafkaEventIdentifier("41", "dummyAppnavn"))
        innboksSession.countEvent(KafkaEventIdentifier("42", "dummyAppnavn"))
        innboksSession.countEvent(KafkaEventIdentifier("43", "dummyAppnavn"))
        innboksSession.countEvent(KafkaEventIdentifier("44", "dummyAppnavn"))
        return innboksSession
    }

    fun giveMeOppgaveSessionWithOneCountedEvent(): TopicMetricsSession {
        val oppgaveSession = TopicMetricsSession(EventType.OPPGAVE_INTERN)
        oppgaveSession.countEvent(KafkaEventIdentifier("41", "dummyAppnavn"))
        return oppgaveSession
    }

    fun giveMeOppgaveSessionWithFiveCountedEvent(): TopicMetricsSession {
        val oppgaveSession = TopicMetricsSession(EventType.OPPGAVE_INTERN)
        oppgaveSession.countEvent(KafkaEventIdentifier("51", "dummyAppnavn"))
        oppgaveSession.countEvent(KafkaEventIdentifier("52", "dummyAppnavn"))
        oppgaveSession.countEvent(KafkaEventIdentifier("53", "dummyAppnavn"))
        oppgaveSession.countEvent(KafkaEventIdentifier("54", "dummyAppnavn"))
        oppgaveSession.countEvent(KafkaEventIdentifier("55", "dummyAppnavn"))
        return oppgaveSession
    }

    fun giveMeOppgaveInternSessionWithFiveCountedEvent(): TopicMetricsSession {
        val oppgaveSession = TopicMetricsSession(EventType.OPPGAVE_INTERN)
        oppgaveSession.countEvent(KafkaEventIdentifier("51", "dummyAppnavn"))
        oppgaveSession.countEvent(KafkaEventIdentifier("52", "dummyAppnavn"))
        oppgaveSession.countEvent(KafkaEventIdentifier("53", "dummyAppnavn"))
        oppgaveSession.countEvent(KafkaEventIdentifier("54", "dummyAppnavn"))
        oppgaveSession.countEvent(KafkaEventIdentifier("55", "dummyAppnavn"))
        return oppgaveSession
    }

    fun giveMeStatusoppdateringSessionWithOneCountedEvent(): TopicMetricsSession {
        val statusoppdateringSession = TopicMetricsSession(EventType.STATUSOPPDATERING_INTERN)
        statusoppdateringSession.countEvent(KafkaEventIdentifier("61", "dummyAppnavn"))
        return statusoppdateringSession
    }

    fun giveMeStatusoppdateringSessionWithFiveCountedEvent(): TopicMetricsSession {
        val statusoppdateringSession = TopicMetricsSession(EventType.STATUSOPPDATERING_INTERN)
        statusoppdateringSession.countEvent(KafkaEventIdentifier("61", "dummyAppnavn"))
        statusoppdateringSession.countEvent(KafkaEventIdentifier("62", "dummyAppnavn"))
        statusoppdateringSession.countEvent(KafkaEventIdentifier("63", "dummyAppnavn"))
        statusoppdateringSession.countEvent(KafkaEventIdentifier("64", "dummyAppnavn"))
        statusoppdateringSession.countEvent(KafkaEventIdentifier("65", "dummyAppnavn"))
        return statusoppdateringSession
    }

    fun giveMeStatusoppdateringInternSessionWithFiveCountedEvent(): TopicMetricsSession {
        val statusoppdateringSession = TopicMetricsSession(EventType.STATUSOPPDATERING_INTERN)
        statusoppdateringSession.countEvent(KafkaEventIdentifier("61", "dummyAppnavn"))
        statusoppdateringSession.countEvent(KafkaEventIdentifier("62", "dummyAppnavn"))
        statusoppdateringSession.countEvent(KafkaEventIdentifier("63", "dummyAppnavn"))
        statusoppdateringSession.countEvent(KafkaEventIdentifier("64", "dummyAppnavn"))
        statusoppdateringSession.countEvent(KafkaEventIdentifier("65", "dummyAppnavn"))
        return statusoppdateringSession
    }

    fun giveMeATopicSessionWithConfiguration(type: EventType, config: Map<String, Int>): TopicMetricsSession {
        val session = TopicMetricsSession(type)

        unrollAndTransformToEvents(config).forEach { event ->
            session.countEvent(event)
        }

        return session
    }

    private fun unrollAndTransformToEvents(configs: Map<String, Int>): List<KafkaEventIdentifier> {

        return configs.entries
            .map { transformToEvents(it) }
            .flatten()
    }

    private fun transformToEvents(entry: Map.Entry<String, Int>): List<KafkaEventIdentifier> {
        val (producer, numberOfEvents) = entry

        return (0 until numberOfEvents).map { index ->
            KafkaEventIdentifier("$producer-$index", producer)
        }
    }
}
