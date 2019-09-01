package io.github.matrixkt.models.events.contents

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class StrippedState(
    /**
     * The `content` for the event.
     */
    val content: JsonObject,

    /**
     * The `state_key` for the event.
     */
    @SerialName("state_key")
    val stateKey: String,

    /**
     * The `type` for the event.
     */
    val type: String,

    /**
     * The `sender` for the event.
     */
    val sender: String
)
