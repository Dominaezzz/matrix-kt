package io.github.matrixkt.models.events.contents.room

import io.github.matrixkt.utils.DiscriminatorChanger
import kotlinx.serialization.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * This event type is used when sending encrypted events.
 * It can be used either within a room (in which case it will have all of the [Room Event fields](https://matrix.org/docs/spec/client_server/r0.6.0#room-event-fields)),
 * or as a [to-device](https://matrix.org/docs/spec/client_server/r0.6.0#to-device) event.
 */
@SerialName("m.room.encrypted")
@Serializable(EncryptedContent.TheSerializer::class)
public abstract class EncryptedContent {
    // /**
    //  * The encryption algorithm used to encrypt this event.
    //  * The value of this field determines which other properties will be present.
    //  * One of: ["m.olm.v1.curve25519-aes-sha2", "m.megolm.v1.aes-sha2"]
    //  */
    // abstract val algorithm: String

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

    @OptIn(ExperimentalSerializationApi::class)
    @Serializer(forClass = EncryptedContent::class)
    public object TheSerializer : KSerializer<EncryptedContent> {
        private val firstDelegate = PolymorphicSerializer(EncryptedContent::class)
        private val secondDelegate = DiscriminatorChanger(firstDelegate, "algorithm")

        override fun deserialize(decoder: Decoder): EncryptedContent {
            return decoder.decodeSerializableValue(secondDelegate)
        }

        override fun serialize(encoder: Encoder, value: EncryptedContent) {
            encoder.encodeSerializableValue(secondDelegate, value)
        }
    }

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
