package io.github.matrixkt.models.notifications

import io.github.matrixkt.models.events.MatrixEvent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
class Notification(
    /**
     * The action(s) to perform when the conditions for this rule are met.
     * See [Push Rules: API](https://matrix.org/docs/spec/client_server/r0.6.0#push-rules-api).
     */
    val actions: List<JsonElement>,

    /**
     * The Event object for the event that triggered the notification.
     */
    val event: MatrixEvent,

    /**
     * The profile tag of the rule that matched this event.
     */
    @SerialName("profile_tag")
    val profileTag: String? = null,

    /**
     * Indicates whether the user has sent a read receipt indicating that they have read this message.
     */
    val read: Boolean,

    /**
     * The ID of the room in which the event was posted.
     */
    @SerialName("room_id")
    val roomId: String,

    /**
     * The unix timestamp at which the event notification was sent, in milliseconds.
     */
    val ts: Long
)
