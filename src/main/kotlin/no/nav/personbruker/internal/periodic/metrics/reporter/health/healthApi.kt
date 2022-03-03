package no.nav.personbruker.internal.periodic.metrics.reporter.health

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.response.*
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.exporter.common.TextFormat

fun Routing.healthApi(healthService: HealthService, activityHealthService: ActivityHealthService) {

    get("/internal/isAlive") {
        if (activityHealthService.assertTopicActivityHealth()) {
            call.respondText(text = "ALIVE", contentType = ContentType.Text.Plain)
        } else {
            call.respondText(text = "DEAD", status = InternalServerError, contentType = ContentType.Text.Plain)
        }
    }

    get("/internal/isReady") {
        if (isReady(healthService)) {
            call.respondText(text = "READY", contentType = ContentType.Text.Plain)
        } else {
            call.respondText(text = "NOTREADY", contentType = ContentType.Text.Plain, status = HttpStatusCode.FailedDependency)
        }
    }

    get("/metrics") {
        val names = call.request.queryParameters.getAll("name")?.toSet() ?: emptySet()
        call.respondTextWriter(ContentType.parse(TextFormat.CONTENT_TYPE_004), HttpStatusCode.OK) {
            TextFormat.write004(this, CollectorRegistry.defaultRegistry.filteredMetricFamilySamples(names))
        }
    }

    get("/internal/selftest") {
        call.buildSelftestPage(healthService)
    }
}

private suspend fun isReady(healthService: HealthService): Boolean {
    val healthChecks = healthService.getHealthChecks()
    return healthChecks
            .filter { healthStatus -> healthStatus.includeInReadiness }
            .all { healthStatus -> Status.OK == healthStatus.status }
}
