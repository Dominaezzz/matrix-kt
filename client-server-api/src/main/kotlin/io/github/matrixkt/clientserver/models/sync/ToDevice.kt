package io.github.matrixkt.clientserver.models.sync

import kotlinx.serialization.Serializable

@Serializable
public data class ToDevice(
    /**
     * List of send-to-device messages.
     */
    val events: List<Event> = emptyList()
)
