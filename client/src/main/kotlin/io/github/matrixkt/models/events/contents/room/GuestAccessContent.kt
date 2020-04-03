package io.github.matrixkt.models.events.contents.room

import io.github.matrixkt.models.events.GuestAccess
import io.github.matrixkt.models.events.contents.Content
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This event controls whether guest users are allowed to join rooms.
 * If this event is absent, servers should act as if it is present and has the `guest_access` value "forbidden".
 */
@SerialName("m.room.guest_access")
@Serializable
data class GuestAccessContent(
    /**
     * Whether guests can join the room.
     */
    @SerialName("guest_access")
    val guestAccess: GuestAccess
) : Content()
