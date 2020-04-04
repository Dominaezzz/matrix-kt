package io.github.matrixkt.models.events.contents.room

import io.github.matrixkt.models.events.contents.Content
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Indication that the room has been upgraded.
 *
 * A state event signifying that a room has been upgraded to a different room version, and that clients should go there.
 */
@SerialName("m.room.tombstone")
@Serializable
data class TombstoneContent(
    /**
     * A server-defined message.
     */
    val body: String,

    /**
     * The new room the client should be visiting.
     */
    @SerialName("replacement_room")
    val replacementRoom: String
) : Content()
