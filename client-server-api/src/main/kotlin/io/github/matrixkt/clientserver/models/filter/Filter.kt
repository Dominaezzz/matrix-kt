package io.github.matrixkt.clientserver.models.filter

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Filter(
    /**
     * List of event fields to include.
     * If this list is absent then all fields are included.
     * The entries may include '.' characters to indicate sub-fields.
     * So ['content.body'] will include the 'body' field of the 'content' object.
     * A literal '.' character in a field name may be escaped using a '\'.
     * A server may include more fields than were requested.
     */
    @SerialName("event_fields")
    val eventFields: List<String>? = null,

    /**
     * The format to use for events.
     * 'client' will return the events in a format suitable for clients.
     * 'federation' will return the raw event as received over federation.
     * The default is 'client'.
     * One of: ["client", "federation"]
     */
    val eventFormat: String? = null,

    /**
     * The presence updates to include.
     */
    val presence: EventFilter? = null,

    /**
     * The user account data that isn't associated with rooms to include.
     */
    @SerialName("account_data")
    val accountData: EventFilter? = null,

    /**
     * Filters to be applied to room data.
     */
    val room: RoomFilter? = null
)