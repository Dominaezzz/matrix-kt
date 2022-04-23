@file:Suppress("UNCHECKED_CAST")

package io.github.matrixkt.models.events

import kotlinx.serialization.*

public sealed class Event<out Content> {
    /**
     * The fields in this object will vary depending on the type of event.
     * When interacting with the REST API, this is the HTTP body.
     */
    public abstract val content: Content

    /**
     * The type of event.
     * This SHOULD be namespaced similar to Java package naming conventions e.g. 'com.example.subdomain.event.type'
     */
    public abstract val type: String
}

public fun <Content : Any, UnsignedData : Any> RoomEvent(
    type: String,
    content: Content,
    eventId: String,
    sender: String,
    originServerTimestamp: Long,
    unsigned: UnsignedData? = null,
    roomId: String,
    stateKey: String? = null,
    prevContent: Content? = null
): RoomEvent<Content, UnsignedData> {
    return if (stateKey != null) {
        StateEvent(type, content, eventId, sender, originServerTimestamp, unsigned, roomId, stateKey, prevContent)
    } else {
        MessageEvent(type, content, eventId, sender, originServerTimestamp, unsigned, roomId)
    }
}

internal data class MessageEvent<Content, UnsignedData>(
    override val type: String,

    override val content: Content,

    @SerialName("event_id")
    override val eventId: String,

    override val sender: String,

    @SerialName("origin_server_ts")
    override val originServerTimestamp: Long,

    override val unsigned: UnsignedData? = null,

    @SerialName("room_id")
    override val roomId: String
) : RoomEvent<Content, UnsignedData>() {
    @SerialName("state_key")
    override val stateKey: String? get() = null

    @SerialName("prev_content")
    override val prevContent: Content? get() = null
}

@Serializable
public data class EphemeralEvent<Content>(
    override val type: String,

    override val content: Content,

    /**
     * Contains the fully-qualified ID of the user who sent this event.
     */
    val sender: String
) : Event<Content>()

// @Serializable
public sealed class AccountEvent<out Content> : Event<Content>() {
    @SerialName("room_id")
    public abstract val roomId: String?
}

@Serializable
public data class GlobalAccountEvent<out Content>(
    override val type: String,

    override val content: Content
) : AccountEvent<Content>() {
    @SerialName("room_id")
    override val roomId: String? get() = null
}

@Serializable
public data class RoomAccountEvent<out Content>(
    override val type: String,

    override val content: Content,

    @SerialName("room_id")
    override val roomId: String
) : AccountEvent<Content>()
