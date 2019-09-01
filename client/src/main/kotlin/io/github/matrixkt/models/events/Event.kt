package io.github.matrixkt.models.events

import io.github.matrixkt.models.events.contents.*
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
    EventTypes.ROOM_ALIASES to RoomAliasesContent::class,
    EventTypes.ROOM_CANONICAL_ALIAS to RoomCanonicalAliasContent::class,
    EventTypes.ROOM_CREATE to RoomCreateContent::class,
    EventTypes.ROOM_JOIN_RULES to RoomJoinRulesContent::class,
    EventTypes.ROOM_MEMBER to RoomMemberContent::class,
    EventTypes.ROOM_POWER_LEVELS to RoomPowerLevelsContent::class,
    EventTypes.ROOM_REDACTION_EVENT to RoomRedactionContent::class,
    EventTypes.ROOM_HISTORY_VISIBILITY to RoomHistoryVisibilityContent::class,
    EventTypes.ROOM_MESSAGE to RoomMessageContent::class,
    EventTypes.ROOM_NAME to RoomNameContent::class,
    EventTypes.ROOM_TOPIC to RoomTopicContent::class,
    EventTypes.ROOM_AVATAR to RoomAvatarContent::class,
    EventTypes.ROOM_PINNED_EVENTS to RoomPinnedEventsContent::class,
    EventTypes.ROOM_GUEST_ACCESS to RoomGuestAccessContent::class,
    EventTypes.CALL_INVITE to CallInviteContent::class,
    EventTypes.CALL_CANDIDATES to CallCandidatesContent::class,
    EventTypes.CALL_ANSWER to CallAnswerContent::class,
    EventTypes.CALL_HANGUP to CallHangupContent::class,
    EventTypes.TYPING to TypingContent::class,
    EventTypes.RECEIPT to ReceiptContent::class,
    EventTypes.FULLY_READ to FullyReadContent::class,
    EventTypes.PRESENCE to PresenceContent::class
)
