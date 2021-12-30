package io.github.matrixkt.clientserver.models.search

import io.github.matrixkt.clientserver.models.filter.RoomEventFilter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public class RoomEventsCriteria(
    /**
     * The string to search events for.
     */
    @SerialName("search_term")
    public val searchTerm: String,

    /**
     * The keys to search.
     * Defaults to all.
     * One of: ["content.body", "content.name", "content.topic"]
     */
    public val keys: List<String> = emptyList(),

    /**
     * This takes a filter.
     */
    public val filter: RoomEventFilter? = null,

    /**
     * The order in which to search for results.
     * By default, this is "rank".
     * One of: ["recent", "rank"]
     */
    @SerialName("order_by")
    public val orderBy: Ordering? = null,

    /**
     * Configures whether any context for the events returned are included in the response.
     */
    @SerialName("event_context")
    public val eventContext: IncludeEventContext? = null,

    /**
     * Requests the server return the current state for each room returned.
     */
    @SerialName("include_state")
    public val includeState: Boolean? = null,

    /**
     * Requests that the server partitions the result set based on the provided list of keys.
     */
    public val groupings: Groupings
)
