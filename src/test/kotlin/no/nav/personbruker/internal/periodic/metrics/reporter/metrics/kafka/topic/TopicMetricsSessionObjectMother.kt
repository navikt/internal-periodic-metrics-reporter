package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic

import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.KafkaEventIdentifier

object TopicMetricsSessionObjectMother {

    fun giveMeBeskjedSessionWithOneCountedEvent(): TopicMetricsSession {
        val beskjedSession = TopicMetricsSession(EventType.BESKJED_INTERN)
        beskjedSession.countEvent(KafkaEventIdentifier("1", "dummyAppnavn", "123"))
        return beskjedSession
    }

    fun giveMeBeskjedSessionWithTwoCountedEvents(): TopicMetricsSession {
        val beskjedSession = TopicMetricsSession(EventType.BESKJED_INTERN)
        beskjedSession.countEvent(KafkaEventIdentifier("21", "dummyAppnavn", "123"))
        beskjedSession.countEvent(KafkaEventIdentifier("22", "dummyAppnavn", "123"))
        return beskjedSession
    }

    fun giveMeBeskjedInternSessionWithTwoCountedEvents(): TopicMetricsSession {
        val beskjedInternSession = TopicMetricsSession(EventType.BESKJED_INTERN)
        beskjedInternSession.countEvent(KafkaEventIdentifier("21", "dummyAppnavn", "123"))
        beskjedInternSession.countEvent(KafkaEventIdentifier("22", "dummyAppnavn", "123"))
        return beskjedInternSession
    }

    fun giveMeDoneSessionWithOneCountedEvent(): TopicMetricsSession {
        val doneSession = TopicMetricsSession(EventType.DONE_INTERN)
        doneSession.countEvent(KafkaEventIdentifier("21", "dummyAppnavn", "123"))
        return doneSession
    }

    fun giveMeDoneSessionWithThreeCountedEvent(): TopicMetricsSession {
        val doneSession = TopicMetricsSession(EventType.DONE_INTERN)
        doneSession.countEvent(KafkaEventIdentifier("31", "dummyAppnavn", "123"))
        doneSession.countEvent(KafkaEventIdentifier("32", "dummyAppnavn", "123"))
        doneSession.countEvent(KafkaEventIdentifier("33", "dummyAppnavn", "123"))
        return doneSession
    }

    fun giveMeDoneInternSessionWithThreeCountedEvent(): TopicMetricsSession {
        val doneSession = TopicMetricsSession(EventType.DONE_INTERN)
        doneSession.countEvent(KafkaEventIdentifier("31", "dummyAppnavn", "123"))
        doneSession.countEvent(KafkaEventIdentifier("32", "dummyAppnavn", "123"))
        doneSession.countEvent(KafkaEventIdentifier("33", "dummyAppnavn", "123"))
        return doneSession
    }

    fun giveMeInnboksSessionWithOneCountedEvent(): TopicMetricsSession {
        val innboksSession = TopicMetricsSession(EventType.INNBOKS_INTERN)
        innboksSession.countEvent(KafkaEventIdentifier("31", "dummyAppnavn", "123"))
        return innboksSession
    }

    fun giveMeInnboksSessionWithFourCountedEvent(): TopicMetricsSession {
        val innboksSession = TopicMetricsSession(EventType.INNBOKS_INTERN)
        innboksSession.countEvent(KafkaEventIdentifier("41", "dummyAppnavn", "123"))
        innboksSession.countEvent(KafkaEventIdentifier("42", "dummyAppnavn", "123"))
        innboksSession.countEvent(KafkaEventIdentifier("43", "dummyAppnavn", "123"))
        innboksSession.countEvent(KafkaEventIdentifier("44", "dummyAppnavn", "123"))
        return innboksSession
    }

    fun giveMeInnboksInternSessionWithFourCountedEvent(): TopicMetricsSession {
        val innboksSession = TopicMetricsSession(EventType.INNBOKS_INTERN)
        innboksSession.countEvent(KafkaEventIdentifier("41", "dummyAppnavn", "123"))
        innboksSession.countEvent(KafkaEventIdentifier("42", "dummyAppnavn", "123"))
        innboksSession.countEvent(KafkaEventIdentifier("43", "dummyAppnavn", "123"))
        innboksSession.countEvent(KafkaEventIdentifier("44", "dummyAppnavn", "123"))
        return innboksSession
    }

    fun giveMeOppgaveSessionWithOneCountedEvent(): TopicMetricsSession {
        val oppgaveSession = TopicMetricsSession(EventType.OPPGAVE_INTERN)
        oppgaveSession.countEvent(KafkaEventIdentifier("41", "dummyAppnavn", "123"))
        return oppgaveSession
    }

    fun giveMeOppgaveSessionWithFiveCountedEvent(): TopicMetricsSession {
        val oppgaveSession = TopicMetricsSession(EventType.OPPGAVE_INTERN)
        oppgaveSession.countEvent(KafkaEventIdentifier("51", "dummyAppnavn", "123"))
        oppgaveSession.countEvent(KafkaEventIdentifier("52", "dummyAppnavn", "123"))
        oppgaveSession.countEvent(KafkaEventIdentifier("53", "dummyAppnavn", "123"))
        oppgaveSession.countEvent(KafkaEventIdentifier("54", "dummyAppnavn", "123"))
        oppgaveSession.countEvent(KafkaEventIdentifier("55", "dummyAppnavn", "123"))
        return oppgaveSession
    }

    fun giveMeOppgaveInternSessionWithFiveCountedEvent(): TopicMetricsSession {
        val oppgaveSession = TopicMetricsSession(EventType.OPPGAVE_INTERN)
        oppgaveSession.countEvent(KafkaEventIdentifier("51", "dummyAppnavn", "123"))
        oppgaveSession.countEvent(KafkaEventIdentifier("52", "dummyAppnavn", "123"))
        oppgaveSession.countEvent(KafkaEventIdentifier("53", "dummyAppnavn", "123"))
        oppgaveSession.countEvent(KafkaEventIdentifier("54", "dummyAppnavn", "123"))
        oppgaveSession.countEvent(KafkaEventIdentifier("55", "dummyAppnavn", "123"))
        return oppgaveSession
    }

    fun giveMeStatusoppdateringSessionWithOneCountedEvent(): TopicMetricsSession {
        val statusoppdateringSession = TopicMetricsSession(EventType.STATUSOPPDATERING_INTERN)
        statusoppdateringSession.countEvent(KafkaEventIdentifier("61", "dummyAppnavn", "123"))
        return statusoppdateringSession
    }

    fun giveMeStatusoppdateringSessionWithFiveCountedEvent(): TopicMetricsSession {
        val statusoppdateringSession = TopicMetricsSession(EventType.STATUSOPPDATERING_INTERN)
        statusoppdateringSession.countEvent(KafkaEventIdentifier("61", "dummyAppnavn", "123"))
        statusoppdateringSession.countEvent(KafkaEventIdentifier("62", "dummyAppnavn", "123"))
        statusoppdateringSession.countEvent(KafkaEventIdentifier("63", "dummyAppnavn", "123"))
        statusoppdateringSession.countEvent(KafkaEventIdentifier("64", "dummyAppnavn", "123"))
        statusoppdateringSession.countEvent(KafkaEventIdentifier("65", "dummyAppnavn", "123"))
        return statusoppdateringSession
    }

    fun giveMeStatusoppdateringInternSessionWithFiveCountedEvent(): TopicMetricsSession {
        val statusoppdateringSession = TopicMetricsSession(EventType.STATUSOPPDATERING_INTERN)
        statusoppdateringSession.countEvent(KafkaEventIdentifier("61", "dummyAppnavn", "123"))
        statusoppdateringSession.countEvent(KafkaEventIdentifier("62", "dummyAppnavn", "123"))
        statusoppdateringSession.countEvent(KafkaEventIdentifier("63", "dummyAppnavn", "123"))
        statusoppdateringSession.countEvent(KafkaEventIdentifier("64", "dummyAppnavn", "123"))
        statusoppdateringSession.countEvent(KafkaEventIdentifier("65", "dummyAppnavn", "123"))
        return statusoppdateringSession
    }
}
