package io.github.matrixkt.models.events.contents.key.verification

import io.github.matrixkt.models.events.contents.Content
import io.github.matrixkt.utils.DiscriminatorChanger
import kotlinx.serialization.*

/**
 * Begins a key verification process.
 * Typically sent as a [to-device](https://matrix.org/docs/spec/client_server/r0.6.0#to-device) event.
 * The [method] field determines the type of verification.
 * The fields in the event will differ depending on the [method].
 * This definition includes fields that are in common among all variants.
 */
@SerialName("m.key.verification.start")
@Serializable(StartContent.TheSerializer::class)
abstract class StartContent : Content() {
    /**
     * The device ID which is initiating the process.
     */
    @SerialName("from_device")
    abstract val fromDevice: String

    /**
     * An opaque identifier for the verification process.
     * Must be unique with respect to the devices involved.
     * Must be the same as the transaction_id given in the `m.key.verification.request` if this process is originating from a request.
     */
    @SerialName("transaction_id")
    abstract val transactionId: String

    // /**
    //  * The verification method to use.
    //  */
    // abstract val method: String

    /**
     * Optional method to use to verify the other user's key with.
     * Applicable when the [method] chosen only verifies one user's key.
     * This field will never be present if the [method] verifies keys both ways.
     */
    @SerialName("next_method")
    open val nextMethod: String? get() = null

    @SerialName("m.sas.v1")
    @Serializable
    data class SasV1(
        @SerialName("from_device")
        override val fromDevice: String,

        @SerialName("transaction_id")
        override val transactionId: String,

        /**
         * The key agreement protocols the sending device understands. Must include at least curve25519.
         */
        @SerialName("key_agreement_protocols")
        val keyAgreementProtocols: List<String>,

        /**
         * The hash methods the sending device understands. Must include at least sha256.
         */
        val hashes: List<String>,

        /**
         * The message authentication codes that the sending device understands. Must include at least hkdf-hmac-sha256.
         */
        @SerialName("message_authentication_codes")
        val messageAuthenticationCodes: List<String>,

        /**
         * The SAS methods the sending device (and the sending device's user) understands.
         * Must include at least decimal. Optionally can include emoji. One of: ["decimal", "emoji"]
         */
        @SerialName("short_authentication_string")
        val shortAuthenticationString: List<String>
    ) : StartContent()

    @Serializer(forClass = StartContent::class)
    object TheSerializer : KSerializer<StartContent> {
        private val firstDelegate = PolymorphicSerializer(StartContent::class)
        private val secondDelegate = DiscriminatorChanger(firstDelegate, "method")

        override fun deserialize(decoder: Decoder): StartContent {
            return decoder.decode(secondDelegate)
        }

        override fun serialize(encoder: Encoder, value: StartContent) {
            encoder.encode(secondDelegate, value)
        }
    }
}
