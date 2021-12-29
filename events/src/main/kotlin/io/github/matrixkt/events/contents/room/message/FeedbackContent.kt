package io.github.matrixkt.events.contents.room.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * **NB: Usage of this event is discouraged in favour of the** `receipts module`_. **Most clients will not recognise this event.** Feedback events are events sent to acknowledge a message in some way. There are two supported acknowledgements: ``delivered`` (sent when the event has been received) and ``read`` (sent when the event has been observed by the end-user).
 * The ``target_event_id`` should reference the ``m.room.message`` event being acknowledged.
 */
@SerialName("m.room.message.feedback")
@Serializable
public data class FeedbackContent(
    /**
     * The event that this feedback is related to.
     */
    @SerialName("target_event_id")
    val targetEventId: String,

    /**
     * The type of feedback.
     * Any of: delivered, read
     */
    val type: String
)
