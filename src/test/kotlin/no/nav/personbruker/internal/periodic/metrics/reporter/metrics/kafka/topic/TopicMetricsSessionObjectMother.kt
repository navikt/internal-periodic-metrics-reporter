package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic

import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.UniqueKafkaEventIdentifier

object TopicMetricsSessionObjectMother {

    fun giveMeBeskjedSessionWithOneCountedEvent(): TopicMetricsSession {
        val beskjedSession = TopicMetricsSession(EventType.BESKJED_INTERN)
        beskjedSession.countEvent(UniqueKafkaEventIdentifier("1", "dummyAppnavn", "123"))
        return beskjedSession
    }

    fun giveMeBeskjedSessionWithTwoCountedEvents(): TopicMetricsSession {
        val beskjedSession = TopicMetricsSession(EventType.BESKJED_INTERN)
        beskjedSession.countEvent(UniqueKafkaEventIdentifier("21", "dummyAppnavn", "123"))
        beskjedSession.countEvent(UniqueKafkaEventIdentifier("22", "dummyAppnavn", "123"))
        return beskjedSession
    }

    fun giveMeBeskjedInternSessionWithTwoCountedEvents(): TopicMetricsSession {
        val beskjedInternSession = TopicMetricsSession(EventType.BESKJED_INTERN)
        beskjedInternSession.countEvent(UniqueKafkaEventIdentifier("21", "dummyAppnavn", "123"))
        beskjedInternSession.countEvent(UniqueKafkaEventIdentifier("22", "dummyAppnavn", "123"))
        return beskjedInternSession
    }

    fun giveMeDoneSessionWithOneCountedEvent(): TopicMetricsSession {
        val doneSession = TopicMetricsSession(EventType.DONE_INTERN)
        doneSession.countEvent(UniqueKafkaEventIdentifier("21", "dummyAppnavn", "123"))
        return doneSession
    }

    fun giveMeDoneSessionWithThreeCountedEvent(): TopicMetricsSession {
        val doneSession = TopicMetricsSession(EventType.DONE_INTERN)
        doneSession.countEvent(UniqueKafkaEventIdentifier("31", "dummyAppnavn", "123"))
        doneSession.countEvent(UniqueKafkaEventIdentifier("32", "dummyAppnavn", "123"))
        doneSession.countEvent(UniqueKafkaEventIdentifier("33", "dummyAppnavn", "123"))
        return doneSession
    }

    fun giveMeDoneInternSessionWithThreeCountedEvent(): TopicMetricsSession {
        val doneSession = TopicMetricsSession(EventType.DONE_INTERN)
        doneSession.countEvent(UniqueKafkaEventIdentifier("31", "dummyAppnavn", "123"))
        doneSession.countEvent(UniqueKafkaEventIdentifier("32", "dummyAppnavn", "123"))
        doneSession.countEvent(UniqueKafkaEventIdentifier("33", "dummyAppnavn", "123"))
        return doneSession
    }

    fun giveMeInnboksSessionWithOneCountedEvent(): TopicMetricsSession {
        val innboksSession = TopicMetricsSession(EventType.INNBOKS_INTERN)
        innboksSession.countEvent(UniqueKafkaEventIdentifier("31", "dummyAppnavn", "123"))
        return innboksSession
    }

    fun giveMeInnboksSessionWithFourCountedEvent(): TopicMetricsSession {
        val innboksSession = TopicMetricsSession(EventType.INNBOKS_INTERN)
        innboksSession.countEvent(UniqueKafkaEventIdentifier("41", "dummyAppnavn", "123"))
        innboksSession.countEvent(UniqueKafkaEventIdentifier("42", "dummyAppnavn", "123"))
        innboksSession.countEvent(UniqueKafkaEventIdentifier("43", "dummyAppnavn", "123"))
        innboksSession.countEvent(UniqueKafkaEventIdentifier("44", "dummyAppnavn", "123"))
        return innboksSession
    }

    fun giveMeInnboksInternSessionWithFourCountedEvent(): TopicMetricsSession {
        val innboksSession = TopicMetricsSession(EventType.INNBOKS_INTERN)
        innboksSession.countEvent(UniqueKafkaEventIdentifier("41", "dummyAppnavn", "123"))
        innboksSession.countEvent(UniqueKafkaEventIdentifier("42", "dummyAppnavn", "123"))
        innboksSession.countEvent(UniqueKafkaEventIdentifier("43", "dummyAppnavn", "123"))
        innboksSession.countEvent(UniqueKafkaEventIdentifier("44", "dummyAppnavn", "123"))
        return innboksSession
    }

    fun giveMeOppgaveSessionWithOneCountedEvent(): TopicMetricsSession {
        val oppgaveSession = TopicMetricsSession(EventType.OPPGAVE_INTERN)
        oppgaveSession.countEvent(UniqueKafkaEventIdentifier("41", "dummyAppnavn", "123"))
        return oppgaveSession
    }

    fun giveMeOppgaveSessionWithFiveCountedEvent(): TopicMetricsSession {
        val oppgaveSession = TopicMetricsSession(EventType.OPPGAVE_INTERN)
        oppgaveSession.countEvent(UniqueKafkaEventIdentifier("51", "dummyAppnavn", "123"))
        oppgaveSession.countEvent(UniqueKafkaEventIdentifier("52", "dummyAppnavn", "123"))
        oppgaveSession.countEvent(UniqueKafkaEventIdentifier("53", "dummyAppnavn", "123"))
        oppgaveSession.countEvent(UniqueKafkaEventIdentifier("54", "dummyAppnavn", "123"))
        oppgaveSession.countEvent(UniqueKafkaEventIdentifier("55", "dummyAppnavn", "123"))
        return oppgaveSession
    }

    fun giveMeOppgaveInternSessionWithFiveCountedEvent(): TopicMetricsSession {
        val oppgaveSession = TopicMetricsSession(EventType.OPPGAVE_INTERN)
        oppgaveSession.countEvent(UniqueKafkaEventIdentifier("51", "dummyAppnavn", "123"))
        oppgaveSession.countEvent(UniqueKafkaEventIdentifier("52", "dummyAppnavn", "123"))
        oppgaveSession.countEvent(UniqueKafkaEventIdentifier("53", "dummyAppnavn", "123"))
        oppgaveSession.countEvent(UniqueKafkaEventIdentifier("54", "dummyAppnavn", "123"))
        oppgaveSession.countEvent(UniqueKafkaEventIdentifier("55", "dummyAppnavn", "123"))
        return oppgaveSession
    }

    fun giveMeStatusoppdateringSessionWithOneCountedEvent(): TopicMetricsSession {
        val statusoppdateringSession = TopicMetricsSession(EventType.STATUSOPPDATERING_INTERN)
        statusoppdateringSession.countEvent(UniqueKafkaEventIdentifier("61", "dummyAppnavn", "123"))
        return statusoppdateringSession
    }

    fun giveMeStatusoppdateringSessionWithFiveCountedEvent(): TopicMetricsSession {
        val statusoppdateringSession = TopicMetricsSession(EventType.STATUSOPPDATERING_INTERN)
        statusoppdateringSession.countEvent(UniqueKafkaEventIdentifier("61", "dummyAppnavn", "123"))
        statusoppdateringSession.countEvent(UniqueKafkaEventIdentifier("62", "dummyAppnavn", "123"))
        statusoppdateringSession.countEvent(UniqueKafkaEventIdentifier("63", "dummyAppnavn", "123"))
        statusoppdateringSession.countEvent(UniqueKafkaEventIdentifier("64", "dummyAppnavn", "123"))
        statusoppdateringSession.countEvent(UniqueKafkaEventIdentifier("65", "dummyAppnavn", "123"))
        return statusoppdateringSession
    }

    fun giveMeStatusoppdateringInternSessionWithFiveCountedEvent(): TopicMetricsSession {
        val statusoppdateringSession = TopicMetricsSession(EventType.STATUSOPPDATERING_INTERN)
        statusoppdateringSession.countEvent(UniqueKafkaEventIdentifier("61", "dummyAppnavn", "123"))
        statusoppdateringSession.countEvent(UniqueKafkaEventIdentifier("62", "dummyAppnavn", "123"))
        statusoppdateringSession.countEvent(UniqueKafkaEventIdentifier("63", "dummyAppnavn", "123"))
        statusoppdateringSession.countEvent(UniqueKafkaEventIdentifier("64", "dummyAppnavn", "123"))
        statusoppdateringSession.countEvent(UniqueKafkaEventIdentifier("65", "dummyAppnavn", "123"))
        return statusoppdateringSession
    }
}
