package no.nav.personbruker.dittnav.metrics.periodic.reporter.config

object ConfigUtil {

    fun isCurrentlyRunningOnNais(): Boolean {
        return System.getenv("NAIS_APP_NAME") != null
    }

}