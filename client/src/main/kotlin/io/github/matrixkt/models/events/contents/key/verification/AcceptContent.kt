package io.github.matrixkt.models.events.contents.key.verification

import io.github.matrixkt.models.events.contents.Content
import io.github.matrixkt.utils.JsonPolymorphicSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(AcceptContent.Serializer::class)
abstract class AcceptContent : Content() {
    /**
     * An opaque identifier for the verification process. Must be the same as the one used for the m.key.verification.start message.
     */
    @SerialName("transaction_id")
    abstract val transactionId: String

    // /**
    //  * The verification method to use.
    //  */
    // abstract val method: String

    @SerialName("m.sas.v1")
    @Serializable
    data class SasV1(
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

    object Serializer : KSerializer<AcceptContent> by JsonPolymorphicSerializer(
        AcceptContent::class, "method")
}
