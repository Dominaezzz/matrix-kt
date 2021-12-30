package io.github.matrixkt.clientserver.models.sync

import kotlinx.serialization.Serializable

@Serializable
public data class AccountData(
    /**
     * List of events.
     */
    public val events: List<Event> = emptyList()
)
