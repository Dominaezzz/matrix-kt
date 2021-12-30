package io.github.matrixkt.clientserver.models.sync

import io.github.matrixkt.clientserver.models.events.SyncEvent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Timeline(
    /**
     * List of events.
     */
    val events: List<SyncEvent> = emptyList(),

    /**
     * 	True if the number of events returned was limited by the `limit` on the filter.
     */
    val limited: Boolean? = null,

    /**
     * A token that can be supplied to the `from` parameter of the rooms/{roomId}/messages endpoint.
     */
    @SerialName("prev_batch")
    val prevBatch: String? = null
)
