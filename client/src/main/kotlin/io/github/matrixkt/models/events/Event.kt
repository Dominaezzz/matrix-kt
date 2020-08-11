@file:Suppress("UNCHECKED_CAST")

package io.github.matrixkt.models.events

import io.github.matrixkt.models.events.contents.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlin.reflect.KClass

@Serializable(EventSerializer::class)
sealed class Event<out T : Content> {
    /**
     * The fields in this object will vary depending on the type of event.
     * When interacting with the REST API, this is the HTTP body.
     */
    abstract val content: T

    /**
     * The type of event.
     * This SHOULD be namespaced similar to Java package naming conventions e.g. 'com.example.subdomain.event.type'
     */
    abstract val type: String
}

class EventSerializer<T : Content>(
    private val contentSerializer: KSerializer<T>
) : JsonContentPolymorphicSerializer<Event<T>>(Event::class as KClass<Event<T>>) {
    override fun selectDeserializer(content: JsonElement): DeserializationStrategy<out Event<T>> {
        require(content is JsonObject)
        return if ("sender" in content) {
            if ("roomId" in content) {
                RoomEvent.serializer(contentSerializer)
            } else {
                EphemeralEvent.serializer(contentSerializer)
            }
        } else {
            AccountEvent.serializer(contentSerializer)
        }
    }
}

@Serializable(RoomEventSerializer::class)
sealed class RoomEvent<out T : Content> : Event<T>() {
    /**
     * The globally unique event identifier.
     */
    @SerialName("event_id")
    abstract val eventId: String

    /**
     * Contains the fully-qualified ID of the user who sent this event.
     */
    abstract val sender: String

    /**
     * Timestamp in milliseconds on originating homeserver when this event was sent.
     */
    @SerialName("origin_server_ts")
    abstract val originServerTimestamp: Long

    /**
     * Contains optional extra information about the event.
     */
    abstract val unsigned: UnsignedData?

    /**
     * The ID of the room associated with this event.
     * Will not be present on events that arrive through `/sync`,
     * despite being required everywhere else.
     */
    @SerialName("room_id")
    abstract val roomId: String
}

class RoomEventSerializer<T : Content>(
    private val contentSerializer: KSerializer<T>
) : JsonContentPolymorphicSerializer<RoomEvent<T>>(RoomEvent::class as KClass<RoomEvent<T>>) {
    override fun selectDeserializer(content: JsonElement): DeserializationStrategy<out RoomEvent<T>> {
        require(content is JsonObject)
        return if (content.containsKey("stateKey")) {
            StateEvent.serializer(contentSerializer)
        } else {
            MessageEvent.serializer(contentSerializer)
        }
    }
}

@Serializable
data class MessageEvent<out T : Content>(
    override val type: String,

    override val content: T,

    @SerialName("event_id")
    override val eventId: String,

    override val sender: String,

    @SerialName("origin_server_ts")
    override val originServerTimestamp: Long,

    override val unsigned: UnsignedData? = null,

    @SerialName("room_id")
    override val roomId: String
) : RoomEvent<T>()

@Serializable
data class StateEvent<out T : Content>(
    override val type: String,

    override val content: T,

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
    val prevContent: T? = null
) : RoomEvent<T>()

@Serializable
data class EphemeralEvent<out T : Content>(
    override val type: String,

    override val content: T,

    /**
     * Contains the fully-qualified ID of the user who sent this event.
     */
    val sender: String
) : Event<T>()

@Serializable
data class AccountEvent<out T : Content>(
    override val type: String,

    override val content: T
) : Event<T>()
