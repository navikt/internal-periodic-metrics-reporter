package no.nav.personbruker.internal.periodic.metrics.reporter.common.kafka.polling

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.Routing
import io.ktor.routing.get
import no.nav.personbruker.internal.periodic.metrics.reporter.config.ApplicationContext
import no.nav.personbruker.internal.periodic.metrics.reporter.config.KafkaConsumerSetup

fun Routing.consumerApi(appContext: ApplicationContext) {

    get("/internal/consumer/start") {
        val responseText = "Konsumerne har blitt restartet på Aiven."
        KafkaConsumerSetup.restartConsumersAiven(appContext)
        call.respondText(text = responseText, contentType = ContentType.Text.Plain)
    }

    get("/internal/consumer/stop") {
        val responseText = "Stoppet alle konsumere på Aiven."
        KafkaConsumerSetup.stopAllKafkaConsumersAiven(appContext)
        call.respondText(text = responseText, contentType = ContentType.Text.Plain)
    }

    get("/internal/consumer/checker/start") {
        val responseText = "Startet jobben som sjekker om konsumerne kjører."
        appContext.reinitializePeriodicConsumerCheck()
        appContext.periodicConsumerCheck.start()
        call.respondText(text = responseText, contentType = ContentType.Text.Plain)
    }

    get("/internal/consumer/checker/stop") {
        val responseText = "Stoppet jobben som sjekker om konsumerne kjører."
        appContext.periodicConsumerCheck.stop()
        call.respondText(text = responseText, contentType = ContentType.Text.Plain)
    }

}
