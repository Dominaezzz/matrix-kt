package io.github.matrixkt.models.events.contents.key.verification

import io.github.matrixkt.utils.DiscriminatorChanger
import kotlinx.serialization.*

/**
 * Begins a key verification process.
 * Typically sent as a [to-device](https://matrix.org/docs/spec/client_server/r0.6.0#to-device) event.
 * The [method] field determines the type of verification.
 * The fields in the event will differ depending on the [method].
 * This definition includes fields that are in common among all variants.
 */
public interface StartContent {
    /**
     * The device ID which is initiating the process.
     */
    @SerialName("from_device")
    public val fromDevice: String

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
    public val nextMethod: String?

    @SerialName("m.key.verification.start")
    @Serializable(ToDevice.TheSerializer::class)
    public abstract class ToDevice : StartContent {
        /**
         * An opaque identifier for the verification process.
         * Must be unique with respect to the devices involved.
         * Must be the same as the `transaction_id` given in the `m.key.verification.request` if this process is originating from a request.
         */
        @SerialName("transaction_id")
        public abstract val transactionId: String

        public object TheSerializer : KSerializer<ToDevice> by DiscriminatorChanger(PolymorphicSerializer(ToDevice::class), "method")
    }

    @SerialName("m.key.verification.start")
    @Serializable(InRoom.TheSerializer::class)
    public abstract class InRoom : StartContent {
        /**
         * Indicates the `m.key.verification.request` that this message is related to.
         * Note that for encrypted messages, this property should be in the unencrypted portion of the event.
         */
        @SerialName("m.relates_to")
        public abstract val relatesTo: VerificationRelatesTo

        public object TheSerializer : KSerializer<InRoom> by DiscriminatorChanger(PolymorphicSerializer(InRoom::class), "method")
    }

    public sealed interface SasV1 : StartContent {
        @SerialName("next_method")
        public override val nextMethod: String? get() = null

        /**
         * The key agreement protocols the sending device understands. Must include at least curve25519.
         */
        @SerialName("key_agreement_protocols")
        public val keyAgreementProtocols: List<String>

        /**
         * The hash methods the sending device understands. Must include at least sha256.
         */
        public val hashes: List<String>

        /**
         * The message authentication codes that the sending device understands. Must include at least hkdf-hmac-sha256.
         */
        @SerialName("message_authentication_codes")
        public val messageAuthenticationCodes: List<String>

        /**
         * The SAS methods the sending device (and the sending device's user) understands.
         * Must include at least decimal. Optionally can include emoji. One of: ["decimal", "emoji"]
         */
        @SerialName("short_authentication_string")
        public val shortAuthenticationString: List<String>

        @SerialName("m.sas.v1")
        @Serializable
        public data class ToDevice(
            @SerialName("from_device")
            override val fromDevice: String,

            @SerialName("transaction_id")
            override val transactionId: String,

            @SerialName("key_agreement_protocols")
            override val keyAgreementProtocols: List<String>,

            override val hashes: List<String>,

            @SerialName("message_authentication_codes")
            override val messageAuthenticationCodes: List<String>,

            @SerialName("short_authentication_string")
            override val shortAuthenticationString: List<String>
        ) : SasV1, StartContent.ToDevice()

        @SerialName("m.sas.v1")
        @Serializable
        public data class InRoom(
            @SerialName("from_device")
            override val fromDevice: String,

            @SerialName("m.relates_to")
            override val relatesTo: VerificationRelatesTo,

            @SerialName("key_agreement_protocols")
            override val keyAgreementProtocols: List<String>,

            override val hashes: List<String>,

            @SerialName("message_authentication_codes")
            override val messageAuthenticationCodes: List<String>,

            @SerialName("short_authentication_string")
            override val shortAuthenticationString: List<String>
        ) : SasV1, StartContent.InRoom()
    }
}
