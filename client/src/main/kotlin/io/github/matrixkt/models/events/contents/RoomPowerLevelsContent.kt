package io.github.matrixkt.models.events.contents

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomPowerLevelsContent(
    /**
     * The level required to ban a user. Defaults to 50 if unspecified.
     */
    val ban: Long? = null,

    /**
     * The level required to send specific event types.
     * This is a mapping from event type to power level required.
     */
    val events: Map<String, Long>? = null,

    /**
     * The default level required to send message events.
     * Can be overridden by the events key.
     * Defaults to 0 if unspecified.
     */
    @SerialName("events_default")
    val eventsDefault: Long? = null,

    /**
     * The level required to invite a user. Defaults to 50 if unspecified.
     */
    val invite: Long? = null,

    /**
     * The level required to kick a user. Defaults to 50 if unspecified.
     */
    val kick: Long? = null,

    /**
     * The level required to redact an event. Defaults to 50 if unspecified.
     */
    val redact: Long? = null,

    /**
     * The default level required to send state events.
     * Can be overridden by the events key.
     * Defaults to 50 if unspecified.
     */
    @SerialName("state_default")
    val stateDefault: Long? = null,

    /**
     * The power levels for specific users.
     * This is a mapping from `user_id` to power level for that user.
     */
    val users: Map<String, Long>? = null,

    /**
     * The default power level for every user in the room, unless their user_id is mentioned in the users key.
     * Defaults to 0 if unspecified.
     */
    @SerialName("users_default")
    val usersDefault: Long? = null,

    /**
     * The power level requirements for specific notification types.
     * This is a mapping from key to power level for that notifications key.
     */
    val notifications: Notifications? = null
) : Content() {
    @Serializable
    data class Notifications(
        /**
         * The level required to trigger an @room notification.
         * Defaults to 50 if unspecified.
         */
        val room: Long? = null
    )
}
