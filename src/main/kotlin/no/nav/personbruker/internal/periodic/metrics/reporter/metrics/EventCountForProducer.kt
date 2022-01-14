package no.nav.personbruker.internal.periodic.metrics.reporter.metrics

data class EventCountForProducer(
    val namespace: String,
    val appName: String,
    val count: Int
) {
    val producer get() = Producer(namespace, appName)
}
