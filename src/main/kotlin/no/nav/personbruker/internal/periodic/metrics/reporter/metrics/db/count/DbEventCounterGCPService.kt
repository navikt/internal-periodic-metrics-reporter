package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.db.count

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import no.nav.personbruker.internal.periodic.metrics.reporter.common.HandlerConsumer
import no.nav.personbruker.internal.periodic.metrics.reporter.common.exceptions.CountException
import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.config.isOtherEnvironmentThanProd
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.CountingMetricsSessions
import org.slf4j.LoggerFactory

class DbEventCounterGCPService(
        private val metricsProbe: DbCountingMetricsProbe,
        private val handlerConsumer: HandlerConsumer
) {

    private val log = LoggerFactory.getLogger(DbEventCounterGCPService::class.java)

    suspend fun countAllEventTypesAsync(): CountingMetricsSessions = withContext(Dispatchers.IO) {
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

        val sessions = CountingMetricsSessions()
        sessions.put(EventType.BESKJED, beskjeder.await())
        sessions.put(EventType.DONE, done.await())
        sessions.put(EventType.INNBOKS, innboks.await())
        sessions.put(EventType.OPPGAVE, oppgave.await())
        sessions.put(EventType.STATUSOPPDATERING, statusoppdatering.await())
        return@withContext sessions
    }

    suspend fun countBeskjeder(): DbCountingMetricsSession {
        val eventType = EventType.BESKJED
        return try {
            metricsProbe.runWithMetrics(eventType) {

                val grupperPerProdusent = handlerConsumer.getEventCount(eventType)
                addEventsByProducer(grupperPerProdusent)
            }

        } catch (e: Exception) {
            throw CountException("Klarte ikke å hente antall beskjed-eventer fra handler.", e)
        }
    }

    suspend fun countInnboksEventer(): DbCountingMetricsSession {
        val eventType = EventType.INNBOKS
        return if (isOtherEnvironmentThanProd()) {
            try {
                metricsProbe.runWithMetrics(eventType) {
                    val grupperPerProdusent = handlerConsumer.getEventCount(eventType)
                    addEventsByProducer(grupperPerProdusent)
                }

            } catch (e: Exception) {
                throw CountException("Klarte ikke å hente antall innboks-eventer fra handler.", e)
            }
        } else {
            DbCountingMetricsSession(eventType)
        }
    }

    suspend fun countStatusoppdateringer(): DbCountingMetricsSession {
        val eventType = EventType.STATUSOPPDATERING
        return if (isOtherEnvironmentThanProd()) {
            try {
                metricsProbe.runWithMetrics(eventType) {
                    val grupperPerProdusent = handlerConsumer.getEventCount(eventType)
                    addEventsByProducer(grupperPerProdusent)
                }

            } catch (e: Exception) {
                throw CountException("Klarte ikke å hente antall statusoppdatering-eventer fra handler.", e)
            }
        } else {
            DbCountingMetricsSession(eventType)
        }
    }

    suspend fun countOppgaver(): DbCountingMetricsSession {
        val eventType = EventType.OPPGAVE
        return try {
            metricsProbe.runWithMetrics(eventType) {
                val grupperPerProdusent = handlerConsumer.getEventCount(eventType)
                addEventsByProducer(grupperPerProdusent)
            }

        } catch (e: Exception) {
            throw CountException("Klarte ikke å hente antall oppgave-eventer fra  handler.", e)
        }
    }

    suspend fun countDoneEvents(): DbCountingMetricsSession {
        val eventType = EventType.DONE
        return try {
            metricsProbe.runWithMetrics(eventType) {
                val grupperPerProdusent = handlerConsumer.getEventCount(eventType)
                addEventsByProducer(grupperPerProdusent)
            }

        } catch (e: Exception) {
            throw CountException("Klarte ikke å hente antall done-eventer fra handler.", e)
        }
    }

}
