package no.nav.personbruker.internal.periodic.metrics.reporter.metrics

import no.nav.personbruker.dittnav.common.metrics.MetricsReporter
import no.nav.personbruker.dittnav.common.metrics.StubMetricsReporter
import no.nav.personbruker.dittnav.common.metrics.influxdb.InfluxConfig
import no.nav.personbruker.dittnav.common.metrics.influxdb.InfluxMetricsReporter
import no.nav.personbruker.internal.periodic.metrics.reporter.config.Environment

fun resolveMetricsReporter(environment: Environment): MetricsReporter {
    return if (environment.influxdbHost == "" || environment.influxdbHost == "stub") {
        StubMetricsReporter()
    } else {
        val sensuConfig = createSensuConfig(environment)
        InfluxMetricsReporter(sensuConfig)
    }
}

private fun createSensuConfig(environment: Environment) = InfluxConfig(
        applicationName = "internal-periodic-metrics-reporter",
        hostName = environment.influxdbHost,
        hostPort = environment.influxdbPort,
        databaseName = environment.influxdbName,
        retentionPolicyName = environment.influxdbRetentionPolicy,
        clusterName = environment.clusterName,
        namespace = environment.namespace,
        userName = environment.influxdbUser,
        password = environment.influxdbPassword
)
