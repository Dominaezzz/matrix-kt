package io.github.matrixkt.models.events.contents

import io.github.matrixkt.utils.JsonPolymorphicSerializer
import kotlinx.serialization.*
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.*

@Serializable(RoomEncryptedContent.Serializer::class)
abstract class RoomEncryptedContent : Content() {
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
    abstract val ciphertext: Any

    /**
     * The Curve25519 key of the sender.
     */
    @SerialName("sender_key")
    abstract val senderKey: String

    @SerialName("m.olm.v1.curve25519-aes-sha2")
    @Serializable
    data class OlmV1(
        override val ciphertext: Map<String, CiphertextInfo>,

        @SerialName("sender_key")
        override val senderKey: String
    ) : RoomEncryptedContent()

    @SerialName("m.megolm.v1.aes-sha2")
    @Serializable
    data class MegolmV1(
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
    ) : RoomEncryptedContent()

    @Serializable
    data class Unknown(
        /**
         * The encryption algorithm used to encrypt this event.
         * The value of this field determines which other properties will be present.
         */
        val algorithm: String,

        override val ciphertext: Map<String, CiphertextInfo>,

        @SerialName("sender_key")
        override val senderKey: String
    ) : RoomEncryptedContent()

    object Serializer : KSerializer<RoomEncryptedContent> by JsonPolymorphicSerializer(RoomEncryptedContent::class, "algorithm", Unknown.serializer())

    @Serializable
    data class CiphertextInfo(
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
