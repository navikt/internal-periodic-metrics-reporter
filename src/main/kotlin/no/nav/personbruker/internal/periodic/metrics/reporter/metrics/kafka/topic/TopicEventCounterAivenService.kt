package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.config.isOtherEnvironmentThanProd
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.CountingMetricsSessions

class TopicEventCounterAivenService<K>(
    val beskjedCounter: TopicEventTypeCounter<K>,
    val innboksCounter: TopicEventTypeCounter<K>,
    val oppgaveCounter: TopicEventTypeCounter<K>,
    val statusoppdateringCounter: TopicEventTypeCounter<K>,
    val doneCounter: TopicEventTypeCounter<K>
    ) {

        suspend fun countAllEventTypesAsync(): CountingMetricsSessions = coroutineScope {

            val beskjeder = beskjedCounter.countEventsAsync()
            val oppgaver = oppgaveCounter.countEventsAsync()
            val done = doneCounter.countEventsAsync()

            val innboks = if (isOtherEnvironmentThanProd()) {
                innboksCounter.countEventsAsync()
            } else {
                async { TopicMetricsSession(EventType.INNBOKS_INTERN) }
            }

            val statusoppdateringer = if(isOtherEnvironmentThanProd()) {
                statusoppdateringCounter.countEventsAsync()
            } else {
                async { TopicMetricsSession(EventType.STATUSOPPDATERING_INTERN) }
            }

            val sessions = CountingMetricsSessions()

            sessions.put(EventType.BESKJED_INTERN, beskjeder.await())
            sessions.put(EventType.DONE__INTERN, done.await())
            sessions.put(EventType.INNBOKS_INTERN, innboks.await())
            sessions.put(EventType.OPPGAVE_INTERN, oppgaver.await())
            sessions.put(EventType.STATUSOPPDATERING_INTERN, statusoppdateringer.await())

            sessions
        }
}
