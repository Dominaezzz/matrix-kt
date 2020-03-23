package io.github.matrixkt.models.sync

import kotlinx.serialization.Serializable

@Serializable
data class InviteState(
    /**
     * The StrippedState events that form the invite state.
     */
    val events: List<StrippedState>? = null
)
