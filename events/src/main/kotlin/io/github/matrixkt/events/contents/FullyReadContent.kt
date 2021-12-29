package io.github.matrixkt.events.contents

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The current location of the user's read marker in a room.
 * This event appears in the user's room account data for the room the marker is applicable for.
 */
@SerialName("m.fully_read")
@Serializable
public data class FullyReadContent(
    /**
     * The event the user's read marker is located at in the room.
     */
    @SerialName("event_id")
    val eventId: String
)
