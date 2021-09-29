package no.nav.personbruker.internal.periodic.metrics.reporter.common

import io.ktor.client.*
import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.config.get
import java.net.URL

class HandlerConsumer(private val client: HttpClient, private val azureTokenFetcher: AzureTokenFetcher, private val eventHandlerBaseURL: URL) {

    suspend fun getEventCount(eventtype: EventType): Map<String, Int> {
        try {
            val pathToEndpoint = URL("${eventHandlerBaseURL.path}/fetch/grouped/systemuser/${eventtype.eventType}")
            return client.get(pathToEndpoint, azureTokenFetcher)
        } catch (e: Exception) {
            return mapOf("appnavn_unavailable" to 0)
        }
    }

}