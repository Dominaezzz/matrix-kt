package io.github.matrixkt.models.sync

import io.github.matrixkt.events.StrippedState
import kotlinx.serialization.Serializable

@Serializable
public data class InviteState(
    /**
     * The StrippedState events that form the invite state.
     */
    val events: List<StrippedState> = emptyList()
)
