package io.github.matrixkt.models.events.contents.room

import io.github.matrixkt.models.events.GuestAccess
import io.github.matrixkt.models.events.contents.Content
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GuestAccessContent(
    /**
     * Whether guests can join the room.
     */
    @SerialName("guest_access")
    val guestAccess: GuestAccess
) : Content()
