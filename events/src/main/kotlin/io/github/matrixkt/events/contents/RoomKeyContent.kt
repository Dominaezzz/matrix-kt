package io.github.matrixkt.events.contents

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This event type is used to exchange keys for end-to-end encryption.
 * Typically it is encrypted as an `m.room.encrypted` event, then sent as a [to-device](https://matrix.org/docs/spec/client_server/r0.6.0#to-device) event.
 */
@SerialName("m.room_key")
@Serializable
public data class RoomKeyContent(
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
)
