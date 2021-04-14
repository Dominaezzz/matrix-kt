package io.github.matrixkt.models.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
public data class MatrixEvent(
    /**
     * The type of event.
     * This SHOULD be namespaced similar to Java package naming conventions e.g. 'com.example.subdomain.event.type'
     */
    val type: String,

    /**
     * The fields in this object will vary depending on the type of event.
     * When interacting with the REST API, this is the HTTP body.
     */
    val content: JsonObject,

    /**
     * The globally unique event identifier.
     */
    @SerialName("event_id")
    val eventId: String,

    /**
     * Contains the fully-qualified ID of the user who sent this event.
     */
    val sender: String,

    /**
     * Timestamp in milliseconds on originating homeserver when this event was sent.
     */
    @SerialName("origin_server_ts")
    val originServerTimestamp: Long,

    /**
     * Contains optional extra information about the event.
     */
    val unsigned: JsonObject? = null,

    /**
     * The ID of the room associated with this event.
     * Will not be present on events that arrive through `/sync`,
     * despite being required everywhere else.
     */
    @SerialName("room_id")
    val roomId: String? = null,

    /**
     * A unique key which defines the overwriting semantics for this piece of room state.
     * This value is often a zero-length string.
     * The presence of this key makes this event a State Event.
     * State keys starting with an @ are reserved for referencing user IDs, such as room members.
     * With the exception of a few events, state events set with a given user's ID as the state key MUST only be set by that user.
     */
    @SerialName("state_key")
    val stateKey: String? = null,

    /**
     * The previous content for this event. If there is no previous content, this key will be missing.
     */
    @SerialName("prev_content")
    val prevContent: JsonObject? = null
)
