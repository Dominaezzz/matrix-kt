package io.github.matrixkt.models.events.contents

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.CommonEnumSerializer

@Serializable
data class CallHangupContent(
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
): Content() {
    @Serializable(Reason.Companion::class)
    enum class Reason {
        ICE_FAILED, INVITE_TIMEOUT;

        companion object : CommonEnumSerializer<Reason>(
            "Reason",
            values(),
            values().map { it.name.toLowerCase() }.toTypedArray()
        )
    }
}
