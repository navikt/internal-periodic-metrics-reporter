package no.nav.personbruker.internal.periodic.metrics.reporter.config

enum class EventType(val originalType: String) {

    OPPGAVE_INTERN("oppgave"),

    BESKJED_INTERN("beskjed"),
    INNBOKS_INTERN("innboks"),
    STATUSOPPDATERING_INTERN("statusoppdatering"),
    DONE_INTERN("done");

    val eventType = "${originalType}_intern"
}
