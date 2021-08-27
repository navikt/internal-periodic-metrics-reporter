package no.nav.personbruker.internal.periodic.metrics.reporter.metrics.kafka.topic.events

interface EventId

data class EventIdPlainText (
        val stringValue: String
): EventId

data class EventIdUuid (
        val lowBits: Long,
        val highBits: Long
): EventId

data class EventIdPrefixedUuid (
        val prefix: Char,
        val lowBits: Long,
        val highBits: Long
): EventId

data class EventIdUlid (
        val lowBits: Long,
        val highBits: Long
): EventId