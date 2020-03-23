package io.github.matrixkt.models.sync

import kotlinx.serialization.Serializable

@Serializable
data class Ephemeral(
    /**
     * List of events.
     */
    val events: List<Event> = emptyList()
)
