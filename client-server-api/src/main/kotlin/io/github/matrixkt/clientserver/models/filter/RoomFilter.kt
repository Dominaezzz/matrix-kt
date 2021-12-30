package io.github.matrixkt.clientserver.models.filter

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class RoomFilter(
    /**
     * A list of room IDs to exclude.
     * If this list is absent then no rooms are excluded.
     * A matching room will be excluded even if it is listed in the 'rooms' filter.
     * This filter is applied before the filters in [ephemeral], [state], [timeline] or [accountData].
     */
    @SerialName("not_rooms")
    public val notRooms: List<String>? = null,

    /**
     * A list of room IDs to include.
     * If this list is absent then all rooms are included.
     * This filter is applied before the filters in [ephemeral], [state], [timeline] or [accountData].
     */
    public val rooms: List<String>? = null,

    /**
     * The events that aren't recorded in the room history, e.g. typing and receipts, to include for rooms.
     */
    public val ephemeral: RoomEventFilter? = null,

    /**
     * Include rooms that the user has left in the sync, default false.
     */
    @SerialName("include_leave")
    public val includeLeave: Boolean? = null,

    /**
     * The state events to include for rooms.
     */
    public val state: StateFilter? = null,

    /**
     * The message and state update events to include for rooms.
     */
    public val timeline: RoomEventFilter? = null,

    /**
     * The per user account data to include for rooms.
     */
    @SerialName("account_data")
    public val accountData: RoomEventFilter? = null
)
