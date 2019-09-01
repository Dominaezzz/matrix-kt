package io.github.matrixkt.models.events.contents

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CallAnswerContent(
    /**
     * The ID of the call this event relates to.
     */
    @SerialName("call_id")
    val callId: String,

    /**
     * The session description object.
     */
    val answer: Answer,

    /**
     *
     */
    val version: Long
) : Content() {
    @Serializable
    data class Answer(
        /**
         * The type of session description. Must be 'answer'.
         */
        val type: String,

        /**
         * The SDP text of the session description.
         */
        val sdp: String
    )
}
