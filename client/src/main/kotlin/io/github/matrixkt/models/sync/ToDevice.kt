package io.github.matrixkt.models.sync

import kotlinx.serialization.Serializable

@Serializable
data class ToDevice(
    /**
     * List of send-to-device messages.
     */
    val events: List<Event> = emptyList()
)
