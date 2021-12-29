package io.github.matrixkt.events.contents.call

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Sent by either party to signal their termination of the call.
 * This can be sent either once the call has has been established or before to abort the call.
 */
@SerialName("m.call.hangup")
@Serializable
public data class HangupContent(
    /**
     * The ID of the call this event relates to.
     */
    @SerialName("call_id")
    val callId: String,

    /**
     * The version of the VoIP specification this messages adheres to.
     * This specification is version 0.
     */
    val version: Long,

    /**
     * Optional error reason for the hangup.
     * This should not be provided when the user naturally ends or rejects the call.
     * When there was an error in the call negotiation, this should be `ice_failed` for when ICE
     * negotiation fails or `invite_timeout` for when the other party did not answer in time.
     * One of: ["ice_failed", "invite_timeout"]
     */
    val reason: Reason? = null
) {
    @Serializable
    public enum class Reason {
        @SerialName("ice_failed")
        ICE_FAILED,

        @SerialName("invite_timeout")
        INVITE_TIMEOUT;
    }
}
