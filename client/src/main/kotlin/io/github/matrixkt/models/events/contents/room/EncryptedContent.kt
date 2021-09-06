package io.github.matrixkt.models.events.contents.room

import kotlinx.serialization.*
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * This event type is used when sending encrypted events.
 * It can be used either within a room (in which case it will have all of the [Room Event fields](https://matrix.org/docs/spec/client_server/r0.6.0#room-event-fields)),
 * or as a [to-device](https://matrix.org/docs/spec/client_server/r0.6.0#to-device) event.
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialName("m.room.encrypted")
@JsonClassDiscriminator("algorithm" /* The encryption algorithm used to encrypt this event. */)
@Serializable
public abstract class EncryptedContent {
    /**
     * The encrypted content of the event.
     * Either the encrypted payload itself, in the case of a Megolm event,
     * or a map from the recipient Curve25519 identity key to ciphertext information,
     * in the case of an Olm event.
     * For more details, see [Messaging Algorithms](https://matrix.org/docs/spec/client_server/r0.6.0#messaging-algorithms).
     */
    public abstract val ciphertext: Any

    /**
     * The Curve25519 key of the sender.
     */
    @SerialName("sender_key")
    public abstract val senderKey: String

    @SerialName("m.olm.v1.curve25519-aes-sha2")
    @Serializable
    public data class OlmV1(
        override val ciphertext: Map<String, CiphertextInfo>,

        @SerialName("sender_key")
        override val senderKey: String
    ) : EncryptedContent()

    @SerialName("m.megolm.v1.aes-sha2")
    @Serializable
    public data class MegolmV1(
        override val ciphertext: String,

        @SerialName("sender_key")
        override val senderKey: String,

        /**
         * The ID of the sending device.
         */
        @SerialName("device_id")
        val deviceId: String,

        /**
         * The ID of the session used to encrypt the message.
         */
        @SerialName("session_id")
        val sessionId: String
    ) : EncryptedContent()

    @Serializable
    public data class CiphertextInfo(
        /**
         * The encrypted payload.
         */
        val body: String? = null,

        /**
         * The Olm message type.
         */
        val type: Int? = null
    )
}
