package io.github.matrixkt.models.events.contents

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomKeyContent(
    /**
     * The encryption algorithm the key in this event is to be used with.
     * Must be 'm.megolm.v1.aes-sha2'.
     */
    val algorithm: String,

    /**
     * The room where the key is used.
     */
    @SerialName("room_id")
    val roomId: String,

    /**
     * The ID of the session that the key is for.
     */
    @SerialName("session_id")
    val sessionId: String,

    /**
     * The key to be exchanged.
     */
    @SerialName("session_key")
    val sessionKey: String
) : Content()
