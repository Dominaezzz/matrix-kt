package io.github.matrixkt.models.events.contents.key.verification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Requests a key verification with another user's devices.
 */
public sealed class RequestContent {
    /**
     * The device ID which is initiating the request.
     */
    @SerialName("from_device")
    public abstract val fromDevice: String

    /**
     * The verification methods supported by the sender.
     */
    public abstract val methods: List<String>

    @SerialName("m.key.verification.request")
    @Serializable
    public data class ToDevice(
        @SerialName("from_device")
        override val fromDevice: String,

        override val methods: List<String>,

        /**
         * An opaque identifier for the verification request. Must be unique with respect to the devices involved.
         */
        @SerialName("transaction_id")
        val transactionId: String,

        /**
         * The POSIX timestamp in milliseconds for when the request was made.
         * If the request is in the future by more than 5 minutes or more than 10 minutes in the past, the message should be ignored by the receiver.
         */
        val timestamp: Long
    ) : RequestContent()

    @SerialName("m.key.verification.request")
    @Serializable
    public data class InRoom(
        @SerialName("from_device")
        override val fromDevice: String,

        override val methods: List<String>
    ) : RequestContent()
}
