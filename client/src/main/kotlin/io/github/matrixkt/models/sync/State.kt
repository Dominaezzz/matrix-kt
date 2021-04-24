package io.github.matrixkt.models.sync

import io.github.matrixkt.models.events.SyncEvent
import kotlinx.serialization.Serializable

@Serializable
public data class State(
    /**
     * List of events.
     */
    val events: List<SyncEvent> = emptyList()
)
