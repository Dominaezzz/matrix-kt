package io.github.matrixkt.clientserver.models.sync

import io.github.matrixkt.clientserver.models.events.SyncStateEvent
import kotlinx.serialization.Serializable

@Serializable
public data class State(
    /**
     * List of events.
     */
    val events: List<SyncStateEvent> = emptyList()
)
