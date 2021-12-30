package io.github.matrixkt.clientserver.models.sync

import kotlinx.serialization.Serializable

@Serializable
public data class Presence(
    /**
     * List of events.
     */
    val events: List<Event> = emptyList()
)
