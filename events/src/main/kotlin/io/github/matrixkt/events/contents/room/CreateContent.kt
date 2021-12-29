package io.github.matrixkt.events.contents.room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This is the first event in a room and cannot be changed. It acts as the root of all other events.
 */
@SerialName("m.room.create")
@Serializable
public data class CreateContent(
    /**
     * The `user_id` of the room creator.
     * This is set by the homeserver.
     */
    val creator: String,

    /**
     * Whether users on other servers can join this room.
     * Defaults to true if key does not exist.
     */
    @SerialName("m.federate")
    val mFederate: Boolean? = null,

    /**
     * The version of the room.
     * Defaults to "1" if the key does not exist.
     */
    @SerialName("room_version")
    val roomVersion: String? = null,

    /**
     * A reference to the room this room replaces, if the previous room was upgraded.
     */
    val predecessor: PreviousRoom? = null
) {
    @Serializable
    public data class PreviousRoom(
        /**
         * The ID of the old room.
         */
        @SerialName("room_id")
        val roomId: String,

        /**
         * The event ID of the last known event in the old room.
         */
        @SerialName("event_id")
        val eventId: String
    )
}
