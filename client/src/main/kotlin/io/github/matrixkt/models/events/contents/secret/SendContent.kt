package io.github.matrixkt.models.events.contents.secret

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Sent by a client to share a secret with another device, in response to an `m.secret.request` event.
 * It must be encrypted as an `m.room.encrypted` event, then sent as a to-device event.
 */
@SerialName("m.secret.send")
@Serializable
public data class SendContent(
    /**
     * The ID of the request that this a response to.
     */
    @SerialName("request_id")
    val requestId: String,

    /**
     * The contents of the secret.
     */
    val secret: String
)
