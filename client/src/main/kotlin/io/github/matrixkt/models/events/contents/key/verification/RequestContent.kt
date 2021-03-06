package io.github.matrixkt.models.events.contents.key.verification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Requests a key verification with another user's devices.
 * Typically sent as a [to-device](https://matrix.org/docs/spec/client_server/r0.6.0#to-device) event.
 */
@SerialName("m.key.verification.request")
@Serializable
public data class RequestContent(
    /**
     * The device ID which is initiating the request.
     */
    @SerialName("from_device")
    val fromDevice: String,

    /**
     * An opaque identifier for the verification request. Must be unique with respect to the devices involved.
     */
    @SerialName("transaction_id")
    val transactionId: String,

    /**
     * The verification methods supported by the sender.
     */
    val methods: List<String>,

    /**
     * The POSIX timestamp in milliseconds for when the request was made.
     * If the request is in the future by more than 5 minutes or more than 10 minutes in the past, the message should be ignored by the receiver.
     */
    val timestamp: Long
)
