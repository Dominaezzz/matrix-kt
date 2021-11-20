package io.github.matrixkt.models.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable(SyncEventSerializer::class)
public sealed class SyncEvent {
    /**
     * The type of event.
     * This SHOULD be namespaced similar to Java package naming conventions e.g. 'com.example.subdomain.event.type'
     */
    public abstract val type: String

    /**
     * The fields in this object will vary depending on the type of event.
     * When interacting with the REST API, this is the HTTP body.
     */
    public abstract val content: JsonObject

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
    public abstract val unsigned: JsonObject?

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
    public abstract val prevContent: JsonObject?
}

private data class SyncMessageEvent(
    override val type: String,

    override val content: JsonObject,

    @SerialName("event_id")
    override val eventId: String,

    override val sender: String,

    @SerialName("origin_server_ts")
    override val originServerTimestamp: Long,

    override val unsigned: JsonObject? = null,
) : SyncEvent() {
    @SerialName("state_key")
    override val stateKey: String? get() = null

    @SerialName("prev_content")
    override val prevContent: JsonObject? get() = null
}

public fun SyncEvent(
    type: String,
    content: JsonObject,
    eventId: String,
    sender: String,
    originServerTimestamp: Long,
    unsigned: JsonObject? = null,
    stateKey: String? = null,
    prevContent: JsonObject? = null
): SyncEvent {
    return if (stateKey != null) {
        SyncStateEvent(type, content, eventId, sender, originServerTimestamp, unsigned, stateKey, prevContent)
    } else {
        SyncMessageEvent(type, content, eventId, sender, originServerTimestamp, unsigned)
    }
}
