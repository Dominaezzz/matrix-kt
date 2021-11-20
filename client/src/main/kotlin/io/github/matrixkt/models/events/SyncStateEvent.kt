package io.github.matrixkt.models.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
public data class SyncStateEvent(
    override val type: String,

    override val content: JsonObject,

    @SerialName("event_id")
    override val eventId: String,

    override val sender: String,

    @SerialName("origin_server_ts")
    override val originServerTimestamp: Long,

    override val unsigned: JsonObject? = null,

    @SerialName("state_key")
    override val stateKey: String,

    @SerialName("prev_content")
    override val prevContent: JsonObject? = null
) : SyncEvent()
