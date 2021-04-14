package io.github.matrixkt.models.events.contents.key.verification

import io.github.matrixkt.utils.DiscriminatorChanger
import kotlinx.serialization.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Accepts a previously sent `m.key.verification.start` message.
 * Typically sent as a [to-device](https://matrix.org/docs/spec/client_server/r0.6.0#to-device) event.
 */
@SerialName("m.key.verification.accept")
@Serializable(AcceptContent.TheSerializer::class)
public abstract class AcceptContent {
    /**
     * An opaque identifier for the verification process. Must be the same as the one used for the m.key.verification.start message.
     */
    @SerialName("transaction_id")
    public abstract val transactionId: String

    // /**
    //  * The verification method to use.
    //  */
    // abstract val method: String

    @SerialName("m.sas.v1")
    @Serializable
    public data class SasV1(
        @SerialName("transaction_id")
        override val transactionId: String,

        /**
         * The key agreement protocol the device is choosing to use, out of the options in the `m.key.verification.start` message.
         */
        @SerialName("key_agreement_protocol")
        val keyAgreementProtocol: String,

        /**
         * The hash method the device is choosing to use, out of the options in the `m.key.verification.start` message.
         */
        val hash: String,

        /**
         * The message authentication code the device is choosing to use, out of the options in the `m.key.verification.start` message.
         */
        @SerialName("message_authentication_code")
        val messageAuthenticationCode: String,

        /**
         * The SAS methods both devices involved in the verification process understand.
         * Must be a subset of the options in the `m.key.verification.start` message.
         * One of: ["decimal", "emoji"]
         */
        @SerialName("short_authentication_string")
        val shortAuthenticationString: List<String>,

        /**
         * The hash (encoded as unpadded base64) of the concatenation of the device's ephemeral public key (encoded as unpadded base64)
         * and the canonical JSON representation of the `m.key.verification.start` message.
         */
        val commitment: String
    ) : AcceptContent()

    @OptIn(ExperimentalSerializationApi::class)
    @Serializer(forClass = AcceptContent::class)
    public object TheSerializer : KSerializer<AcceptContent> {
        private val firstDelegate = PolymorphicSerializer(AcceptContent::class)
        private val secondDelegate = DiscriminatorChanger(firstDelegate, "method")

        override fun deserialize(decoder: Decoder): AcceptContent {
            return decoder.decodeSerializableValue(secondDelegate)
        }

        override fun serialize(encoder: Encoder, value: AcceptContent) {
            encoder.encodeSerializableValue(secondDelegate, value)
        }
    }
}
