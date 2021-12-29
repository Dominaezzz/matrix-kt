package io.github.matrixkt.events.contents.call

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This event is sent by the caller when they wish to establish a call.
 */
@SerialName("m.call.invite")
@Serializable
public data class InviteContent(
    /**
     * A unique identifier for the call.
     */
    @SerialName("call_id")
    val callId: String,

    /**
     * The session description object.
     */
    val offer: Offer,

    /**
     * The version of the VoIP specification this message adheres to.
     * This specification is version 0.
     */
    val version: Long,

    /**
     * The time in milliseconds that the invite is valid for.
     * Once the invite age exceeds this value, clients should discard it.
     * They should also no longer show the call as awaiting an answer in the UI.
     */
    val lifetime: Long
) {
    @Serializable
    public data class Offer(
        /**
         * The type of session description. Must be 'offer'.
         */
        val type: String,

        /**
         * The SDP text of the session description.
         */
        val sdp: String
    )
}
