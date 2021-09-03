package no.nav.personbruker.internal.periodic.metrics.reporter.common

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.http.*
import no.nav.personbruker.internal.periodic.metrics.reporter.config.buildJsonSerializer

fun getClientWith(result: Map<String, Int>): HttpClient {
    return HttpClient(MockEngine) {
        val localserializer = buildJsonSerializer()
        engine {
            addHandler { request ->
                if (request.url.host.contains("event-handler")) {
                    respond(result.toString(), headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()))
                } else {
                    respondError(HttpStatusCode.BadRequest)
                }
            }
        }
        install(JsonFeature) {
            serializer = buildJsonSerializer()
        }
    }
}
