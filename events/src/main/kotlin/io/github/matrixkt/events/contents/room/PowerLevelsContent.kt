package io.github.matrixkt.events.contents.room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
@SerialName("m.room.power_levels")
@Serializable
public data class PowerLevelsContent(
    /**
     * The level required to ban a user.
     */
    val ban: Long = 50,

    /**
     * The level required to send specific event types.
     * This is a mapping from event type to power level required.
     */
    val events: Map<String, Long> = emptyMap(),

    /**
     * The default level required to send message events.
     * Can be overridden by the events key.
     */
    @SerialName("events_default")
    val eventsDefault: Long = 0,

    /**
     * The level required to invite a user.
     */
    val invite: Long = 50,

    /**
     * The level required to kick a user.
     */
    val kick: Long = 50,

    /**
     * The level required to redact an event.
     */
    val redact: Long = 50,

    /**
     * The default level required to send state events.
     * Can be overridden by the events key.
     */
    @SerialName("state_default")
    val stateDefault: Long = 50,

    /**
     * The power levels for specific users.
     * This is a mapping from `user_id` to power level for that user.
     */
    val users: Map<String, Long> = emptyMap(),

    /**
     * The default power level for every user in the room, unless their user_id is mentioned in the users key.
     * Defaults to 0 if unspecified.
     */
    @SerialName("users_default")
    val usersDefault: Long = 0,

    /**
     * The power level requirements for specific notification types.
     * This is a mapping from key to power level for that notifications key.
     */
    val notifications: Notifications? = null
) {
    @Serializable
    public data class Notifications(
        /**
         * The level required to trigger an @room notification.
         * Defaults to 50 if unspecified.
         */
        val room: Long? = null
    )
}
