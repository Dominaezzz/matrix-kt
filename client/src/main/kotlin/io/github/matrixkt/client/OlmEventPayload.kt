package io.github.matrixkt.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
public class OlmEventPayload(
    /**
     * Type of the plaintext event
     */
    public val type: String,

    /**
     * Content for the plaintext event
     */
    public val content: JsonObject,

    /**
     * User id of sender.
     */
    public val sender: String,

    /**
     * User id of recipient.
     */
    public val recipient: String,

    /**
     * Our public signing keys.
     */
    @SerialName("recipient_keys")
    public val recipientKeys: Map<String, String>,

    /**
     * Sender's public signing keys
     */
    @SerialName("keys")
    public val senderKeys: Map<String, String>
)
