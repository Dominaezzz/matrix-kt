package io.github.matrixkt.models.events.contents.key.verification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Sends the MAC of a device's key to the partner device.
 * Typically sent as a [to-device](https://matrix.org/docs/spec/client_server/r0.6.0#to-device) event.
 */
public sealed class MacContent {
    /**
     * A map of the key ID to the MAC of the key, using the algorithm in the verification process.
     * The MAC is encoded as unpadded base64.
     */
    public abstract val mac: Map<String, String>

    /**
     * The MAC of the comma-separated, sorted, list of key IDs given in the mac property, encoded as unpadded base64.
     */
    public abstract val keys: String

    @SerialName("m.key.verification.mac")
    @Serializable
    public data class ToDevice(
        /**
         * The opaque identifier for the verification process/request.
         */
        @SerialName("transaction_id")
        val transactionId: String,

        override val mac: Map<String, String>,

        override val keys: String
    ) : MacContent()

    @SerialName("m.key.verification.mac")
    @Serializable
    public data class InRoom(
        /**
         * Indicates the `m.key.verification.request` that this message is related to.
         * Note that for encrypted messages, this property should be in the unencrypted portion of the event.
         */
        @SerialName("m.relates_to")
        val relatesTo: VerificationRelatesTo,

        override val mac: Map<String, String>,

        override val keys: String
    ) : MacContent()
}
