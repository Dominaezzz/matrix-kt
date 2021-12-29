package io.github.matrixkt.events.contents.room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This event controls whether guest users are allowed to join rooms.
 * If this event is absent, servers should act as if it is present and has the `guest_access` value "forbidden".
 */
@SerialName("m.room.guest_access")
@Serializable
public data class GuestAccessContent(
    /**
     * Whether guests can join the room.
     */
    @SerialName("guest_access")
    val guestAccess: GuestAccess
)

@Serializable
public enum class GuestAccess {
    @SerialName("can_join")
    CAN_JOIN,

    @SerialName("forbidden")
    FORBIDDEN;
}
