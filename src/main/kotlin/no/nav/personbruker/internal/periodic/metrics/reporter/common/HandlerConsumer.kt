package no.nav.personbruker.internal.periodic.metrics.reporter.common

import io.ktor.client.*
import no.nav.personbruker.internal.periodic.metrics.reporter.config.EventType
import no.nav.personbruker.internal.periodic.metrics.reporter.config.get
import java.net.URL

class HandlerConsumer(private val client: HttpClient, eventHandlerBaseURL: URL) {

    private val pathToEndpoint = "${eventHandlerBaseURL.path}/fetch/grouped/systemuser"

    suspend fun getEventCount(eventtype: EventType): Map<String, Int> {
        try {
            return client.get(URL("$pathToEndpoint/${eventtype.eventType}"))
        } catch (e: Exception) {
            return mapOf("appnavn_unavailable" to 0)
        }
    }

}