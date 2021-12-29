package io.github.matrixkt.events.contents.call

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This event is sent by callers after sending an invite and by the callee after answering.
 * Its purpose is to give the other party additional ICE candidates to try using to communicate.
 */
@SerialName("m.call.candidates")
@Serializable
public data class CandidatesContent(
    /**
     * The ID of the call this event relates to.
     */
    @SerialName("call_id")
    val callId: String,

    /**
     * Array of objects describing the candidates.
     */
    val candidates: List<Candidate>,

    /**
     * The version of the VoIP specification this messages adheres to.
     * This specification is version 0.
     */
    val version: Long
) {
    @Serializable
    public data class Candidate(
        /**
         * The SDP media type this candidate is intended for.
         */
        val sdpMid: String,

        /**
         * The index of the SDP 'm' line this candidate is intended for.
         */
        val sdpMLineIndex: Int,

        /**
         * The SDP 'a' line of the candidate.
         */
        val candidate: String
    )
}
