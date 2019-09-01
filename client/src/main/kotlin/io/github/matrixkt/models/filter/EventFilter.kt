package io.github.matrixkt.models.filter

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventFilter(
    /**
     * The maximum number of events to return.
     */
    val limit: Long? = null,

    /**
     * A list of sender IDs to exclude.
     * If this list is absent then no senders are excluded.
     * A matching sender will be excluded even if it is listed in the [senders] filter.
     */
    @SerialName("not_senders")
    val notSenders: List<String>? = null,

    /**
     * A list of event types to exclude.
     * If this list is absent then no event types are excluded.
     * A matching type will be excluded even if it is listed in the [types] filter.
     * A '*' can be used as a wildcard to match any sequence of characters.
     */
    @SerialName("not_types")
    val notTypes: List<String>? = null,

    /**
     * A list of senders IDs to include.
     * If this list is absent then all senders are included.
     */
    val senders: List<String>? = null,

    /**
     * A list of event types to include.
     * If this list is absent then all event types are included.
     * A '*' can be used as a wildcard to match any sequence of characters.
     */
    val types: List<String>? = null
)
