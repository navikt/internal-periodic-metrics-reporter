package no.nav.personbruker.internal.periodic.metrics.reporter.config

object ConfigUtil {

    fun isCurrentlyRunningOnNais(): Boolean {
        return System.getenv("NAIS_APP_NAME") != null
    }

}