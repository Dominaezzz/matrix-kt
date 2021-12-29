package io.github.matrixkt.events.contents.room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Indication that the room has been upgraded.
 *
 * A state event signifying that a room has been upgraded to a different room version, and that clients should go there.
 */
@SerialName("m.room.tombstone")
@Serializable
public data class TombstoneContent(
    /**
     * A server-defined message.
     */
    val body: String,

    /**
     * The new room the client should be visiting.
     */
    @SerialName("replacement_room")
    val replacementRoom: String
)
