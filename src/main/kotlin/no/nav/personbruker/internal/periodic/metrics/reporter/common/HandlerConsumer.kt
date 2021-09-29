package no.nav.personbruker.internal.periodic.metrics.reporter.common

import io.ktor.client.*
import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.config.get
import org.slf4j.LoggerFactory
import java.net.URL

class HandlerConsumer(private val client: HttpClient, private val azureTokenFetcher: AzureTokenFetcher, private val eventHandlerBaseURL: URL) {
    private val log = LoggerFactory.getLogger(HandlerConsumer::class.java)

    suspend fun getEventCount(eventtype: EventType): Map<String, Int> {
        try {
            val pathToEndpoint = URL("${eventHandlerBaseURL.path}/fetch/grouped/systemuser/${eventtype.eventType}")
            return client.get(pathToEndpoint, azureTokenFetcher)
        } catch (e: Exception) {
            log.error("Får ikke kontakt med dittnav-event-handler. Setter derfor produsentnavnet til <appnavn_unavailable> og antall event = 0.")
            return mapOf("appnavn_unavailable" to 0)
        }
    }

}