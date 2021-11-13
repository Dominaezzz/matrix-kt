package io.github.matrixkt.models.sync

import io.github.matrixkt.models.events.StrippedState
import kotlinx.serialization.Serializable

@Serializable
public data class KnockState(
    /**
     * The StrippedState events that form the knock state.
     */
    val events: List<StrippedState> = emptyList()
)
