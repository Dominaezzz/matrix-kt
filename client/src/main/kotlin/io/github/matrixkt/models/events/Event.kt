package io.github.matrixkt.models.events

import io.github.matrixkt.models.events.contents.*
import io.github.matrixkt.models.events.contents.call.AnswerContent
import io.github.matrixkt.models.events.contents.call.CandidatesContent
import io.github.matrixkt.models.events.contents.call.HangupContent
import io.github.matrixkt.models.events.contents.call.InviteContent
import io.github.matrixkt.models.events.contents.room.*
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.Serializable

@Serializable(PolymorphicSerializer::class)
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

abstract class RoomEvent : Event()

abstract class MessageEvent : RoomEvent()

abstract class StateEvent : RoomEvent()

abstract class EphemeralEvent : Event()

abstract class AccountEvent : Event()

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
