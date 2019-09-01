package io.github.matrixkt.models.events.contents

import io.github.matrixkt.models.events.GuestAccess
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomGuestAccessContent(
    /**
     * Whether guests can join the room.
     */
    @SerialName("guest_access")
    val guestAccess: GuestAccess
) : Content()
