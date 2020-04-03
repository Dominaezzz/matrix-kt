package io.github.matrixkt.models.events.contents.call

import io.github.matrixkt.models.events.contents.Content
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This event is sent by the callee when they wish to answer the call.
 */
@SerialName("m.call.answer")
@Serializable
data class AnswerContent(
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
