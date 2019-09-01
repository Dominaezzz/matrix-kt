package io.github.matrixkt.models.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnsignedData(
    /**
     * The time in milliseconds that has elapsed since the event was sent.
     * This field is generated by the local homeserver,
     * and may be incorrect if the local time on at least one of the two servers is out of sync,
     * which can cause the age to either be negative or greater than it actually is.
     */
    val age: Long? = null,

    /**
     * Optional. The event that redacted this event, if any.
     */
    @SerialName("redacted_because")
    val redactedBecause: MatrixEvent? = null,

    /**
     * The client-supplied transaction ID,
     * if the client being given the event is the same one which sent it.
     */
    @SerialName("transaction_id")
    val transactionId: String? = null
)
