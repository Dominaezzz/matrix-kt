package io.github.matrixkt.clientserver.models.notifications

import io.github.matrixkt.clientserver.models.events.MatrixEvent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
public class Notification(
    /**
     * The action(s) to perform when the conditions for this rule are met.
     * See [Push Rules: API](https://matrix.org/docs/spec/client_server/r0.6.0#push-rules-api).
     */
    public val actions: List<JsonElement>,

    /**
     * The Event object for the event that triggered the notification.
     */
    public val event: MatrixEvent,

    /**
     * The profile tag of the rule that matched this event.
     */
    @SerialName("profile_tag")
    public val profileTag: String? = null,

    /**
     * Indicates whether the user has sent a read receipt indicating that they have read this message.
     */
    public val read: Boolean,

    /**
     * The ID of the room in which the event was posted.
     */
    @SerialName("room_id")
    public val roomId: String,

    /**
     * The unix timestamp at which the event notification was sent, in milliseconds.
     */
    public val ts: Long
)
