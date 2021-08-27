package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.submitter

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import no.nav.personbruker.internal.periodic.metrics.reporter.config.ApplicationContext

fun Routing.metricsSubmitterApi(appContext: ApplicationContext) {

    get("/internal/metrics/submitter/start") {
        val responseText = "Starter periodisk innrapportering av metrikker."
        appContext.reinitializePeriodicMetricsSubmitter()
        appContext.periodicMetricsSubmitter.start()
        call.respondText(text = responseText, contentType = ContentType.Text.Plain)
    }

    get("/internal/metrics/submitter/stop") {
        val responseText = "Stoppet periodisk innrapportering av metrikker."
        appContext.periodicMetricsSubmitter.stop()
        call.respondText(text = responseText, contentType = ContentType.Text.Plain)
    }
}
