package io.github.matrixkt.models.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
class OlmEventPayload(
    /**
     * Type of the plaintext event
     */
    val type: String,

    /**
     * Content for the plaintext event
     */
    val content: JsonObject,

    /**
     * User id of sender.
     */
    val sender: String,

    /**
     * User id of recipient.
     */
    val recipient: String,

    /**
     * Our public signing keys.
     */
    @SerialName("recipient_keys")
    val recipientKeys: Map<String, String>,

    /**
     * Sender's public signing keys
     */
    @SerialName("keys")
    val senderKeys: Map<String, String>
)
