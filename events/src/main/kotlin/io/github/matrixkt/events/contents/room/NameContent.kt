package io.github.matrixkt.events.contents.room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A room has an opaque room ID which is not human-friendly to read.
 * A room alias is human-friendly, but not all rooms have room aliases.
 * The room name is a human-friendly string designed to be displayed to the end-user.
 * The room name is not unique, as multiple rooms can have the same room name set.
 *
 * A room with an `m.room.name` event with an absent, null, or empty `name` field
 * should be treated the same as a room with no `m.room.name` event.
 *
 * An event of this type is automatically created when creating a room using `/createRoom` with the `name` key.
 */
@SerialName("m.room.name")
@Serializable
public data class NameContent(
    /**
     * The name of the room. This MUST NOT exceed 255 bytes.
     */
    val name: String
)
