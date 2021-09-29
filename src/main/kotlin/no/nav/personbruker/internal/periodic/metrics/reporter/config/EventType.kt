package no.nav.personbruker.internal.periodic.metrics.reporter.config

enum class EventType(val eventType: String) {
    OPPGAVE_INTERN("oppgave_intern"),
    BESKJED_INTERN("beskjed_intern"),
    INNBOKS_INTERN("innboks_intern"),
    STATUSOPPDATERING_INTERN("statusoppdatering_intern"),
    DONE_INTERN("done_intern")
}
