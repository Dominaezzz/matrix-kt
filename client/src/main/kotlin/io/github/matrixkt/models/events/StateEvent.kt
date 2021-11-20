package io.github.matrixkt.models.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class StateEvent<Content, UnsignedData>(
    override val type: String,

    override val content: Content,

    @SerialName("event_id")
    override val eventId: String,

    override val sender: String,

    @SerialName("origin_server_ts")
    override val originServerTimestamp: Long,

    override val unsigned: UnsignedData? = null,

    @SerialName("room_id")
    override val roomId: String,

    @SerialName("state_key")
    override val stateKey: String,

    @SerialName("prev_content")
    override val prevContent: Content? = null
) : RoomEvent<Content, UnsignedData>()
