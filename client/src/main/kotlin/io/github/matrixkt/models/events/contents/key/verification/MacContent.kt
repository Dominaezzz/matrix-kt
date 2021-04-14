package io.github.matrixkt.models.events.contents.key.verification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Sends the MAC of a device's key to the partner device.
 * Typically sent as a [to-device](https://matrix.org/docs/spec/client_server/r0.6.0#to-device) event.
 */
@SerialName("m.key.verification.mac")
@Serializable
public data class MacContent(
    /**
     * An opaque identifier for the verification process.
     * Must be the same as the one used for the `m.key.verification.start` message.
     */
    @SerialName("transaction_id")
    val transactionId: String,

    /**
     * A map of the key ID to the MAC of the key, using the algorithm in the verification process.
     * The MAC is encoded as unpadded base64.
     */
    val mac: Map<String, String>,

    /**
     * The MAC of the comma-separated, sorted, list of key IDs given in the mac property, encoded as unpadded base64.
     */
    val keys: String
)
