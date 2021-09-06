package io.github.matrixkt.models.events.contents.key.verification

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * Accepts a previously sent `m.key.verification.start` message.
 */
public interface AcceptContent {
    @OptIn(ExperimentalSerializationApi::class)
    @SerialName("m.key.verification.accept")
    @JsonClassDiscriminator("method")
    @Serializable
    public abstract class ToDevice : AcceptContent {
        /**
         * An opaque identifier for the verification process.
         * Must be the same as the one used for the `m.key.verification.start` message.
         */
        @SerialName("transaction_id")
        public abstract val transactionId: String
    }

    @OptIn(ExperimentalSerializationApi::class)
    @SerialName("m.key.verification.start")
    @JsonClassDiscriminator("method")
    @Serializable
    public abstract class InRoom : AcceptContent {
        /**
         * Indicates the `m.key.verification.request` that this message is related to.
         * Note that for encrypted messages, this property should be in the unencrypted portion of the event.
         */
        @SerialName("m.relates_to")
        public abstract val relatesTo: VerificationRelatesTo
    }

    public sealed interface SasV1 : AcceptContent {
        /**
         * The key agreement protocol the device is choosing to use, out of the options in the `m.key.verification.start` message.
         */
        @SerialName("key_agreement_protocol")
        public val keyAgreementProtocol: String

        /**
         * The hash method the device is choosing to use, out of the options in the `m.key.verification.start` message.
         */
        public val hash: String

        /**
         * The message authentication code the device is choosing to use, out of the options in the `m.key.verification.start` message.
         */
        @SerialName("message_authentication_code")
        public val messageAuthenticationCode: String

        /**
         * The SAS methods both devices involved in the verification process understand.
         * Must be a subset of the options in the `m.key.verification.start` message.
         * One of: ["decimal", "emoji"]
         */
        @SerialName("short_authentication_string")
        public val shortAuthenticationString: List<String>

        /**
         * The hash (encoded as unpadded base64) of the concatenation of the device's ephemeral public key (encoded as unpadded base64)
         * and the canonical JSON representation of the `m.key.verification.start` message.
         */
        public val commitment: String

        @SerialName("m.sas.v1")
        @Serializable
        public data class ToDevice(
            @SerialName("transaction_id")
            override val transactionId: String,

            @SerialName("key_agreement_protocol")
            override val keyAgreementProtocol: String,

            override val hash: String,

            @SerialName("message_authentication_code")
            override val messageAuthenticationCode: String,

            @SerialName("short_authentication_string")
            override val shortAuthenticationString: List<String>,

            override val commitment: String
        ) : SasV1, AcceptContent.ToDevice()

        @SerialName("m.sas.v1")
        @Serializable
        public data class InRoom(
            @SerialName("m.relates_to")
            override val relatesTo: VerificationRelatesTo,

            @SerialName("key_agreement_protocol")
            override val keyAgreementProtocol: String,

            override val hash: String,

            @SerialName("message_authentication_code")
            override val messageAuthenticationCode: String,

            @SerialName("short_authentication_string")
            override val shortAuthenticationString: List<String>,

            override val commitment: String
        ) : SasV1, AcceptContent.InRoom()
    }
}
