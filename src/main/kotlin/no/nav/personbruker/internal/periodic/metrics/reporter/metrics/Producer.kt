package no.nav.personbruker.internal.periodic.metrics.reporter.metrics

import no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.KafkaEventIdentifier

data class Producer(
    val namespace: String,
    val appName: String
)

val KafkaEventIdentifier.producer get() = Producer(namespace, appnavn)
