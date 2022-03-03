package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.cache.count

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import no.nav.personbruker.internal.periodic.metrics.reporter.common.HandlerConsumer
import no.nav.personbruker.internal.periodic.metrics.reporter.common.exceptions.CountException
import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.config.isOtherEnvironmentThanProd
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.CacheCountingMetricsSessions
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.CountingMetricsSessions
import org.slf4j.LoggerFactory

class CacheEventCounterService(
        private val metricsProbe: CacheCountingMetricsProbe,
        private val handlerConsumer: HandlerConsumer
) {

    private val log = LoggerFactory.getLogger(CacheEventCounterService::class.java)

    suspend fun countAllEventTypesAsync(): CacheCountingMetricsSessions = withContext(Dispatchers.IO) {
        val beskjeder = async {
            countBeskjeder()
        }
        val innboks = async {
            countInnboksEventer()
        }
        val oppgave = async {
            countOppgaver()
        }
        val statusoppdatering = async {
            countStatusoppdateringer()
        }
        val done = async {
            countDoneEvents()
        }

        val sessions = CacheCountingMetricsSessions()
        sessions.put(EventType.BESKJED_INTERN, beskjeder.await())
        sessions.put(EventType.DONE_INTERN, done.await())
        sessions.put(EventType.INNBOKS_INTERN, innboks.await())
        sessions.put(EventType.OPPGAVE_INTERN, oppgave.await())
        sessions.put(EventType.STATUSOPPDATERING_INTERN, statusoppdatering.await())
        return@withContext sessions
    }

    suspend fun countBeskjeder(): CacheCountingMetricsSession {
        val eventType = EventType.BESKJED_INTERN
        return try {
            metricsProbe.runWithMetrics(eventType) {

                val grupperPerProdusent = handlerConsumer.getEventCount(eventType)
                addEventsByProducer(grupperPerProdusent)
            }

        } catch (e: Exception) {
            throw CountException("Klarte ikke å hente antall ${eventType.eventType} fra handler.", e)
        }
    }

    suspend fun countInnboksEventer(): CacheCountingMetricsSession {
        val eventType = EventType.INNBOKS_INTERN
        return try {
            metricsProbe.runWithMetrics(eventType) {
                val grupperPerProdusent = handlerConsumer.getEventCount(eventType)
                addEventsByProducer(grupperPerProdusent)
            }

        } catch (e: Exception) {
            throw CountException("Klarte ikke å hente antall ${eventType.eventType} fra  handler.", e)
        }
    }

    suspend fun countStatusoppdateringer(): CacheCountingMetricsSession {
        val eventType = EventType.STATUSOPPDATERING_INTERN
        return if (isOtherEnvironmentThanProd()) {
            try {
                metricsProbe.runWithMetrics(eventType) {
                    val grupperPerProdusent = handlerConsumer.getEventCount(eventType)
                    addEventsByProducer(grupperPerProdusent)
                }

            } catch (e: Exception) {
                throw CountException("Klarte ikke å hente antall ${eventType.eventType} fra handler.", e)
            }
        } else {
            CacheCountingMetricsSession(eventType)
        }
    }

    suspend fun countOppgaver(): CacheCountingMetricsSession {
        val eventType = EventType.OPPGAVE_INTERN
        return try {
            metricsProbe.runWithMetrics(eventType) {
                val grupperPerProdusent = handlerConsumer.getEventCount(eventType)
                addEventsByProducer(grupperPerProdusent)
            }

        } catch (e: Exception) {
            throw CountException("Klarte ikke å hente antall ${eventType.eventType} fra  handler.", e)
        }
    }

    suspend fun countDoneEvents(): CacheCountingMetricsSession {
        val eventType = EventType.DONE_INTERN
        return try {
            metricsProbe.runWithMetrics(eventType) {
                val grupperPerProdusent = handlerConsumer.getEventCount(eventType)
                addEventsByProducer(grupperPerProdusent)
            }

        } catch (e: Exception) {
            throw CountException("Klarte ikke å hente antall ${eventType.eventType} fra handler.", e)
        }
    }

}
