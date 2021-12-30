package io.github.matrixkt.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
public data class MegolmEventPayload(
    val type: String,

    val content: JsonObject,

    @SerialName("room_id")
    val roomId: String
)
