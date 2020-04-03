package io.github.matrixkt.models.events

object EventTypes {
    /**
     * This event is sent by a homeserver directly to inform of changes to the list of aliases it knows about for that room.
     * The `state_key` for this event is set to the homeserver which owns the room alias.
     * The entire set of known aliases for the room is the union of all the `m.room.aliases` events, one for each homeserver.
     * Clients **should** check the validity of any room alias given in this list before presenting it to the user as trusted fact.
     * The lists given by this event should be considered simply as advice on which aliases might exist,
     * for which the client can perform the lookup to confirm whether it receives the correct room ID.
     */
    const val ROOM_ALIASES = "m.room.aliases"

    /**
     * This event is used to inform the room about which alias should be considered the canonical one.
     * This could be for display purposes or as suggestion to users which alias to use to advertise the room.
     *
     * A room with an `m.room.canonical_alias` event with an absent, null,
     * or empty alias field should be treated the same as a room with no `m.room.canonical_alias` event.
     */
    const val ROOM_CANONICAL_ALIAS = "m.room.canonical_alias"

    /**
     * This is the first event in a room and cannot be changed. It acts as the root of all other events.
     */
    const val ROOM_CREATE = "m.room.create"

    /**
     * A room may be `public` meaning anyone can join the room without any prior action.
     * Alternatively, it can be `invite` meaning that a user who wishes to join the room
     * must first receive an invite to the room from someone already inside of the room.
     * Currently, `knock` and `private` are reserved keywords which are not implemented.
     */
    const val ROOM_JOIN_RULES = "m.room.join_rules"

    /**
     * Adjusts the membership state for a user in a room.
     * It is preferable to use the membership APIs (`/rooms/<room id>/invite` etc) when performing membership
     * actions rather than adjusting the state directly as there are a restricted set of valid transformations.
     * For example, user A cannot force user B to join a room, and trying to force this state change directly will fail.
     *
     * The following membership states are specified:
     * - `invite` - The user has been invited to join a room, but has not yet joined it. They may not participate in the room until they join.
     * - `join` - The user has joined the room (possibly after accepting an invite), and may participate in it.
     * - `leave` - The user was once joined to the room, but has since left (possibly by choice, or possibly by being kicked).
     * - `ban` - The user has been banned from the room, and is no longer allowed to join it until they are un-banned from the room (by having their membership state set to a value other than ban).
     * - `knock` - This is a reserved word, which currently has no meaning.
     *
     * The `third_party_invite` property will be set if this invite is an `invite` event and is the successor of an `m.room.third_party_invite` event, and absent otherwise.
     *
     * This event may also include an `invite_room_state` key inside the event's `unsigned` data.
     * If present, this contains an array of `StrippedState` Events.
     * These events provide information on a subset of state events such as the room name.
     *
     * The user for which a membership applies is represented by the `state_key`.
     * Under some conditions, the `sender` and `state_key` may not match - this may
     * be interpreted as the `sender` affecting the membership state of the `state_key` user.
     *
     * The `membership` for a given user can change over time.
     * The table below represents the various changes over time and how clients and servers must interpret those changes.
     * Previous membership can be retrieved from the `prev_content` object on an event.
     * If not present, the user's previous membership must be assumed as `leave`.
     */
    const val ROOM_MEMBER = "m.room.member"

    /**
     * This event specifies the minimum level a user must have in order to perform a certain action.
     * It also specifies the levels of each user in the room.
     *
     * If a `user_id` is in the `users` list, then that `user_id` has the associated power level.
     * Otherwise they have the default level `users_default`.
     * If `users_default` is not supplied, it is assumed to be 0.
     * If the room contains no `m.room.power_levels` event,
     * the room's creator has a power level of 100,
     * and all other users have a power level of 0.
     *
     * The level required to send a certain event is governed by `events`, `state_default` and `events_default`.
     * If an event type is specified in `events`, then the user must have at least the level specified in order to send that event.
     * If the event type is not supplied, it defaults to `events_default` for Message Events and `state_default` for State Events.
     *
     * If there is no `state_default` in the `m.room.power_levels` event, the `state_default` is 50.
     * If there is no `events_default` in the `m.room.power_levels` event, the `events_default` is 0.
     * If the room contains no `m.room.power_levels` event, both the `state_default` and `events_default` are 0.
     *
     * The power level required to invite a user to the room, kick a user from the room, ban a user from the room,
     * or redact an event, is defined by `invite`, `kick`,`ban`, and `redact`, `respectively`.
     * Each of these levels defaults to 50 if they are not specified in the `m.room.power_levels` event,
     * or if the room contains no `m.room.power_levels` event.
     *
     * Note: As noted above, in the absence of an `m.room.power_levels` event, the `state_default` is 0, and all users are considered to have power level 0.
     * That means that any member of the room can send an `m.room.power_levels` event, changing the permissions in the room.
     * Server implementations should therefore ensure that each room has an `m.room.power_levels` event as soon as it is created.
     * See also the documentation of the `/createRoom` API.
     */
    const val ROOM_POWER_LEVELS = "m.room.power_levels"

    /**
     * Events can be redacted by either room or server admins.
     * Redacting an event means that all keys not required by the protocol are stripped off,
     * allowing admins to remove offensive or illegal content that may have been attached to any event.
     * This cannot be undone, allowing server owners to physically delete the offending data.
     * There is also a concept of a moderator hiding a message event, which can be undone,
     * but cannot be applied to state events.
     * The event that has been redacted is specified in the `redacts` event level key.
     */
    const val ROOM_REDACTION_EVENT = "m.room.redaction"

    /**
     * This event controls whether a user can see the events that happened in a room from before they joined.
     */
    const val ROOM_HISTORY_VISIBILITY = "m.room.history_visibility"

    /**
     * This event is used when sending messages in a room.
     * Messages are not limited to be text.
     * The `msgtype` key outlines the type of message, e.g. text, audio, image, video, etc.
     * The `body` key is text and MUST be used with every kind of `msgtype` as a fallback mechanism for when a client cannot render a message.
     * This allows clients to display something even if it is just plain text.
     * For more information on `msgtypes`,
     * see [`m.room.message` msgtypes](https://matrix.org/docs/spec/client_server/r0.5.0#m-room-message-msgtypes).
     */
    const val ROOM_MESSAGE = "m.room.message"

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
    const val ROOM_NAME = "m.room.name"

    /**
     * A topic is a short message detailing what is currently being discussed in the room.
     * It can also be used as a way to display extra information about the room, which may not be suitable for the room name.
     * The room topic can also be set when creating a room using `/createRoom` with the `topic` key.
     */
    const val ROOM_TOPIC = "m.room.topic"

    /**
     * A picture that is associated with the room.
     * This can be displayed alongside the room information.
     */
    const val ROOM_AVATAR = "m.room.avatar"

    /**
     * This event is used to "pin" particular events in a room for other participants to review later.
     * The order of the pinned events is guaranteed and based upon the order supplied in the event.
     * Clients should be aware that the current user may not be able to see some of the events pinned due to visibility settings in the room.
     * Clients are responsible for determining if a particular event in the pinned list is displayable,
     * and have the option to not display it if it cannot be pinned in the client.
     */
    const val ROOM_PINNED_EVENTS = "m.room.pinned_events"

    /**
     * This event controls whether guest users are allowed to join rooms.
     * If this event is absent, servers should act as if it is present and has the `guest_access` value "forbidden".
     */
    const val ROOM_GUEST_ACCESS = "m.room.guest_access"

    /**
     * Defines how messages sent in this room should be encrypted.
     */
    const val ROOM_ENCRYPTION = "m.room.encryption"

    /**
     * This event type is used when sending encrypted events.
     * It can be used either within a room (in which case it will have all of the [Room Event fields](https://matrix.org/docs/spec/client_server/r0.6.0#room-event-fields)),
     * or as a [to-device](https://matrix.org/docs/spec/client_server/r0.6.0#to-device) event.
     */
    const val ROOM_ENCRYPTED = "m.room.encrypted"

    /**
     * This event is sent by the caller when they wish to establish a call.
     */
    const val CALL_INVITE = "m.call.invite"

    /**
     * This event is sent by callers after sending an invite and by the callee after answering.
     * Its purpose is to give the other party additional ICE candidates to try using to communicate.
     */
    const val CALL_CANDIDATES = "m.call.candidates"

    /**
     * This event is sent by the callee when they wish to answer the call.
     */
    const val CALL_ANSWER = "m.call.answer"

    /**
     * Sent by either party to signal their termination of the call.
     * This can be sent either once the call has has been established or before to abort the call.
     */
    const val CALL_HANGUP = "m.call.hangup"

    /**
     * The list of user IDs typing in this room, if any.
     */
    const val TYPING = "m.typing"

    /**
     * Informs the client of new receipts.
     */
    const val RECEIPT = "m.receipt"

    /**
     * The current location of the user's read marker in a room.
     * This event appears in the user's room account data for the room the marker is applicable for.
     */
    const val FULLY_READ = "m.fully_read"

    /**
     * Informs the client of a user's presence state change.
     */
    const val PRESENCE = "m.presence"

    /**
     * This event type is used to exchange keys for end-to-end encryption.
     * Typically it is encrypted as an `m.room.encrypted` event, then sent as a [to-device](https://matrix.org/docs/spec/client_server/r0.6.0#to-device) event.
     */
    const val ROOM_KEY = "m.room_key"

    /**
     * This event type is used to request keys for end-to-end encryption.
     * It is sent as an unencrypted [to-device](https://matrix.org/docs/spec/client_server/r0.6.0#to-device) event.
     */
    const val ROOM_KEY_REQUEST = "m.room_key_request"

    /**
     * This event type is used to forward keys for end-to-end encryption.
     * Typically it is encrypted as an `m.room.encrypted` event, then sent as a [to-device](https://matrix.org/docs/spec/client_server/r0.6.0#to-device) event.
     */
    const val FORWARDED_ROOM_KEY = "m.forwarded_room_key"

    /**
     * This event type is used to indicate new Olm sessions for end-to-end encryption.
     * Typically it is encrypted as an m.room.encrypted event, then sent as a [to-device](https://matrix.org/docs/spec/client_server/r0.6.0#to-device) event.
     * The event does not have any content associated with it.
     * The sending client is expected to send a key share request shortly after this message,
     * causing the receiving client to process this `m.dummy` event as the most recent event and using the keyshare request to set up the session.
     * The keyshare request and `m.dummy` combination should result in the original sending client receiving keys over the newly established session.
     */
    const val DUMMY = "m.dummy"

    /**
     * Informs the client of tags on a room.
     */
    const val TAG = "m.tag"

    /**
     * A list of terms URLs the user has previously accepted.
     * Clients SHOULD use this to avoid presenting the user with terms they have already agreed to.
     */
    const val ACCEPTED_TERMS = "m.accepted_terms"

    /**
     * Requests a key verification with another user's devices.
     * Typically sent as a [to-device](https://matrix.org/docs/spec/client_server/r0.6.0#to-device) event.
     */
    const val KEY_VERIFICATION_REQUEST = "m.key.verification.request"

    /**
     * Begins a key verification process.
     * Typically sent as a [to-device](https://matrix.org/docs/spec/client_server/r0.6.0#to-device) event.
     * The [method] field determines the type of verification.
     * The fields in the event will differ depending on the [method].
     * This definition includes fields that are in common among all variants.
     */
    const val KEY_VERIFICATION_START = "m.key.verification.start"

    /**
     * Cancels a key verification process/request.
     * Typically sent as a [to-device](https://matrix.org/docs/spec/client_server/r0.6.0#to-device) event.
     */
    const val KEY_VERIFICATION_CANCEL = "m.key.verification.cancel"

    /**
     * Accepts a previously sent `m.key.verification.start` message.
     * Typically sent as a [to-device](https://matrix.org/docs/spec/client_server/r0.6.0#to-device) event.
     */
    const val KEY_VERIFICATION_ACCEPT = "m.key.verification.accept"

    /**
     * Sends the ephemeral public key for a device to the partner device.
     * Typically sent as a [to-device](https://matrix.org/docs/spec/client_server/r0.6.0#to-device) event.
     */
    const val KEY_VERIFICATION_KEY = "m.key.verification.key"

    /**
     * Sends the MAC of a device's key to the partner device.
     * Typically sent as a [to-device](https://matrix.org/docs/spec/client_server/r0.6.0#to-device) event.
     */
    const val KEY_VERIFICATION_MAC = "m.key.verification.mac"
}
