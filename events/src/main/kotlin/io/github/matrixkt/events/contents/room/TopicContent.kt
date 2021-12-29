package io.github.matrixkt.events.contents.room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A topic is a short message detailing what is currently being discussed in the room.
 * It can also be used as a way to display extra information about the room, which may not be suitable for the room name.
 * The room topic can also be set when creating a room using `/createRoom` with the `topic` key.
 */
@SerialName("m.room.topic")
@Serializable
public data class TopicContent(
    /**
     * The topic text.
     */
    val topic: String
)
