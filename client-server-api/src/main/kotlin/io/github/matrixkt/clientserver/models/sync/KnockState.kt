package io.github.matrixkt.clientserver.models.sync

import io.github.matrixkt.events.StrippedState
import kotlinx.serialization.Serializable

@Serializable
public data class KnockState(
    /**
     * The StrippedState events that form the knock state.
     */
    val events: List<StrippedState> = emptyList()
)
