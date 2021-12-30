package io.github.matrixkt.clientserver.models.filter

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class StateFilter(
    /**
     * The maximum number of events to return.
     */
    public val limit: Long? = null,

    /**
     * A list of sender IDs to exclude.
     * If this list is absent then no senders are excluded.
     * A matching sender will be excluded even if it is listed in the [senders] filter.
     */
    @SerialName("not_senders")
    public val notSenders: List<String>? = null,

    /**
     * A list of event types to exclude.
     * If this list is absent then no event types are excluded.
     * A matching type will be excluded even if it is listed in the [types] filter.
     * A '*' can be used as a wildcard to match any sequence of characters.
     */
    @SerialName("not_types")
    public val notTypes: List<String>? = null,

    /**
     * A list of senders IDs to include.
     * If this list is absent then all senders are included.
     */
    public val senders: List<String>? = null,

    /**
     * A list of event types to include.
     * If this list is absent then all event types are included.
     * A '*' can be used as a wildcard to match any sequence of characters.
     */
    public val types: List<String>? = null,

    /**
     * If true, enables lazy-loading of membership events.
     * See [Lazy-loading room members](https://matrix.org/docs/spec/client_server/r0.5.0#lazy-loading-room-members) for more information.
     * Defaults to false.
     */
    @SerialName("lazy_load_members")
    public val lazyLoadMembers: Boolean? = null,

    /**
     * If true, sends all membership events for all events, even if they have already been sent to the client.
     * Does not apply unless [lazyLoadMembers] is true.
     * See [Lazy-loading room members](https://matrix.org/docs/spec/client_server/r0.5.0#lazy-loading-room-members) for more information.
     * Defaults to false.
     */
    @SerialName("include_redundant_members")
    public val includeRedundantMembers: Boolean? = null,

    /**
     * A list of room IDs to exclude.
     * If this list is absent then no rooms are excluded.
     * A matching room will be excluded even if it is listed in the [rooms] filter.
     */
    @SerialName("not_rooms")
    public val notRooms: List<String>? = null,

    /**
     * A list of room IDs to include. If this list is absent then all rooms are included.
     */
    public val rooms: List<String>? = null,

    /**
     * If true, includes only events with a url key in their content.
     * If false, excludes those events.
     * If omitted, `url` key is not considered for filtering.
     */
    @SerialName("contains_url")
    public val containsUrl: Boolean? = null
)