package io.github.matrixkt.models.events

import io.github.matrixkt.models.events.contents.*
import io.github.matrixkt.models.events.contents.call.AnswerContent
import io.github.matrixkt.models.events.contents.call.CandidatesContent
import io.github.matrixkt.models.events.contents.call.HangupContent
import io.github.matrixkt.models.events.contents.call.InviteContent
import io.github.matrixkt.models.events.contents.room.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonParametricSerializer

@Serializable(EventSerializer::class)
sealed class Event {
    /**
     * The fields in this object will vary depending on the type of event.
     * When interacting with the REST API, this is the HTTP body.
     */
    abstract val content: Any

    /**
     * The type of event.
     * This SHOULD be namespaced similar to Java package naming conventions e.g. 'com.example.subdomain.event.type'
     */
    abstract val type: String
}

object EventSerializer : JsonParametricSerializer<Event>(Event::class) {
    override fun selectSerializer(element: JsonElement): KSerializer<out Event> {
        require(element is JsonObject)
        return if ("sender" in element) {
            if ("roomId" in element) {
                RoomEvent.serializer()
            } else {
                EphemeralEvent.serializer()
            }
        } else {
            AccountEvent.serializer()
        }
    }
}

@Serializable(RoomEventSerializer::class)
sealed class RoomEvent : Event() {
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

object RoomEventSerializer : JsonParametricSerializer<RoomEvent>(RoomEvent::class) {
    override fun selectSerializer(element: JsonElement): KSerializer<out RoomEvent> {
        require(element is JsonObject)
        return if (element.containsKey("stateKey")) {
            StateEvent.serializer()
        } else {
            MessageEvent.serializer()
        }
    }
}

@Serializable
data class MessageEvent(
    override val type: String,

    override val content: JsonObject,

    @SerialName("event_id")
    override val eventId: String,

    override val sender: String,

    @SerialName("origin_server_ts")
    override val originServerTimestamp: Long,

    override val unsigned: UnsignedData? = null,

    @SerialName("room_id")
    override val roomId: String
) : RoomEvent()

@Serializable
data class StateEvent(
    override val type: String,

    override val content: JsonObject,

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
    val prevContent: JsonObject? = null
) : RoomEvent()

@Serializable
data class EphemeralEvent(
    override val type: String,

    override val content: JsonObject,

    /**
     * Contains the fully-qualified ID of the user who sent this event.
     */
    val sender: String
) : Event()

@Serializable
data class AccountEvent(
    override val type: String,

    override val content: JsonObject
) : Event()

private val temp = mapOf(
    EventTypes.ROOM_ALIASES to AliasesContent::class,
    EventTypes.ROOM_CANONICAL_ALIAS to CanonicalAliasContent::class,
    EventTypes.ROOM_CREATE to CreateContent::class,
    EventTypes.ROOM_JOIN_RULES to JoinRulesContent::class,
    EventTypes.ROOM_MEMBER to MemberContent::class,
    EventTypes.ROOM_POWER_LEVELS to PowerLevelsContent::class,
    EventTypes.ROOM_REDACTION_EVENT to RedactionContent::class,
    EventTypes.ROOM_HISTORY_VISIBILITY to HistoryVisibilityContent::class,
    EventTypes.ROOM_MESSAGE to MessageContent::class,
    EventTypes.ROOM_NAME to NameContent::class,
    EventTypes.ROOM_TOPIC to TopicContent::class,
    EventTypes.ROOM_AVATAR to AvatarContent::class,
    EventTypes.ROOM_PINNED_EVENTS to PinnedEventsContent::class,
    EventTypes.ROOM_GUEST_ACCESS to GuestAccessContent::class,
    EventTypes.ROOM_ENCRYPTION to EncryptionContent::class,
    EventTypes.ROOM_ENCRYPTED to EncryptedContent::class,
    EventTypes.CALL_INVITE to InviteContent::class,
    EventTypes.CALL_CANDIDATES to CandidatesContent::class,
    EventTypes.CALL_ANSWER to AnswerContent::class,
    EventTypes.CALL_HANGUP to HangupContent::class,
    EventTypes.TYPING to TypingContent::class,
    EventTypes.RECEIPT to ReceiptContent::class,
    EventTypes.FULLY_READ to FullyReadContent::class,
    EventTypes.PRESENCE to PresenceContent::class,
    EventTypes.ROOM_KEY to RoomKeyContent::class,
    EventTypes.ROOM_KEY_REQUEST to RoomKeyRequestContent::class,
    EventTypes.FORWARDED_ROOM_KEY to ForwardedRoomKeyContent::class,
    EventTypes.DUMMY to DummyContent::class
)
