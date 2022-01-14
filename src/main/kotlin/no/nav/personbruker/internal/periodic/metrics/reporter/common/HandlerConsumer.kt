package no.nav.personbruker.internal.periodic.metrics.reporter.common

import io.ktor.client.*
import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.config.get
import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.EventCountForProducer
import org.slf4j.LoggerFactory
import java.net.URL

class HandlerConsumer(private val client: HttpClient, private val azureTokenFetcher: AzureTokenFetcher, private val eventHandlerBaseURL: String) {
    private val log = LoggerFactory.getLogger(HandlerConsumer::class.java)

    suspend fun getEventCount(eventtype: EventType): List<EventCountForProducer> {
        try {
            val pathToEndpoint = URL("$eventHandlerBaseURL/fetch/grouped/produsent/${eventtype.originalType}")

            return client.get(pathToEndpoint, azureTokenFetcher)
        } catch (e: Exception) {
            log.error("Får ikke kontakt med dittnav-event-handler.", e)
            return emptyList()
        }
    }
}

