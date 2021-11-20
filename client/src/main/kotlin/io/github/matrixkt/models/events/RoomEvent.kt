package io.github.matrixkt.models.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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

    /**
     * A unique key which defines the overwriting semantics for this piece of room state.
     * This value is often a zero-length string.
     * The presence of this key makes this event a State Event.
     * State keys starting with an @ are reserved for referencing user IDs, such as room members.
     * With the exception of a few events, state events set with a given user's ID as the state key MUST only be set by that user.
     */
    @SerialName("state_key")
    public abstract val stateKey: String?

    /**
     * The previous content for this event. If there is no previous content, this key will be missing.
     */
    @SerialName("prev_content")
    public abstract val prevContent: Content?
}
