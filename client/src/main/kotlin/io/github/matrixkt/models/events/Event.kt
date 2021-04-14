@file:Suppress("UNCHECKED_CAST")

package io.github.matrixkt.models.events

import kotlinx.serialization.*
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlin.reflect.KClass

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

@Serializable(RoomEventSerializer::class)
public sealed class RoomEvent<out Content, out UnsignedData> : Event<Content>() {
    /**
     * The globally unique event identifier.
     */
    @SerialName("event_id")
    public abstract val eventId: String

    /**
     * Contains the fully-qualified ID of the user who sent this event.
     */
    public abstract val sender: String

    /**
     * Timestamp in milliseconds on originating homeserver when this event was sent.
     */
    @SerialName("origin_server_ts")
    public abstract val originServerTimestamp: Long

    /**
     * Contains optional extra information about the event.
     */
    public abstract val unsigned: UnsignedData?

    /**
     * The ID of the room associated with this event.
     * Will not be present on events that arrive through `/sync`,
     * despite being required everywhere else.
     */
    @SerialName("room_id")
    public abstract val roomId: String
}

public class RoomEventSerializer<Content, UnsignedData>(
    private val contentSerializer: KSerializer<Content>,
    private val unsignedDataSerializer: KSerializer<UnsignedData>
) : JsonContentPolymorphicSerializer<RoomEvent<Content, UnsignedData>>(RoomEvent::class as KClass<RoomEvent<Content, UnsignedData>>) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out RoomEvent<Content, UnsignedData>> {
        require(element is JsonObject)
        return if (element.containsKey("stateKey")) {
            StateEvent.serializer(contentSerializer, unsignedDataSerializer)
        } else {
            MessageEvent.serializer(contentSerializer, unsignedDataSerializer)
        }
    }
}

@Serializable
public data class MessageEvent<out Content, out UnsignedData>(
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
) : RoomEvent<Content, UnsignedData>()

@Serializable
public data class StateEvent<out Content, out UnsignedData>(
    override val type: String,

    override val content: Content,

    @SerialName("event_id")
    override val eventId: String,

    override val sender: String,

    @SerialName("origin_server_ts")
    override val originServerTimestamp: Long,

    override val unsigned: UnsignedData? = null,

    @SerialName("room_id")
    override val roomId: String,

    /**
     * A unique key which defines the overwriting semantics for this piece of room state.
     * This value is often a zero-length string.
     * The presence of this key makes this event a State Event.
     * State keys starting with an @ are reserved for referencing user IDs, such as room members.
     * With the exception of a few events, state events set with a given user's ID as the state key MUST only be set by that user.
     */
    @SerialName("state_key")
    val stateKey: String,

    /**
     * The previous content for this event. If there is no previous content, this key will be missing.
     */
    @SerialName("prev_content")
    val prevContent: Content? = null
) : RoomEvent<Content, UnsignedData>()

@Serializable
public data class EphemeralEvent<out Content>(
    override val type: String,

    override val content: Content,

    /**
     * Contains the fully-qualified ID of the user who sent this event.
     */
    val sender: String
) : Event<Content>()

@Serializable
public data class AccountEvent<out Content>(
    override val type: String,

    override val content: Content
) : Event<Content>()
