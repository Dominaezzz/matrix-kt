package io.github.matrixkt.models.events.contents

import io.github.matrixkt.utils.DiscriminatorChanger
import kotlinx.serialization.*

/**
 * This event type is used to request keys for end-to-end encryption.
 * It is sent as an unencrypted [to-device](https://matrix.org/docs/spec/client_server/r0.6.0#to-device) event.
 */
@SerialName("m.room_key_request")
@Serializable(RoomKeyRequestContent.TheSerializer::class)
sealed class RoomKeyRequestContent : Content() {
    // /**
    //  * One of: ["request", "request_cancellation"]
    //  */
    // val action: String

    /**
     * ID of the device requesting the key.
     */
    @SerialName("requesting_device_id")
    abstract val requestingDeviceId: String

    /**
     * A random string uniquely identifying the request for a key.
     * If the key is requested multiple times, it should be reused.
     * It should also reused in order to cancel a request.
     */
    @SerialName("request_id")
    abstract val requestId: String

    @Serializer(forClass = RoomKeyRequestContent::class)
    object TheSerializer : KSerializer<RoomKeyRequestContent> {
        @OptIn(InternalSerializationApi::class)
        private val firstDelegate = SealedClassSerializer(
            "N/A", RoomKeyRequestContent::class,
            arrayOf(Request::class, Cancellation::class),
            arrayOf(Request.serializer(), Cancellation.serializer())
        )
        private val secondDelegate = DiscriminatorChanger(firstDelegate, "action")

        override fun deserialize(decoder: Decoder): RoomKeyRequestContent {
            return decoder.decode(secondDelegate)
        }

        override fun serialize(encoder: Encoder, value: RoomKeyRequestContent) {
            encoder.encode(secondDelegate, value)
        }
    }

    @SerialName("request")
    @Serializable
    data class Request(
        /**
         * Information about the requested key.
         */
        val body: RequestedKeyInfo,

        @SerialName("requesting_device_id")
        override val requestingDeviceId: String,

        @SerialName("request_id")
        override val requestId: String
    ) : RoomKeyRequestContent()

    @SerialName("request_cancellation")
    @Serializable
    data class Cancellation(
        @SerialName("requesting_device_id")
        override val requestingDeviceId: String,

        @SerialName("request_id")
        override val requestId: String
    ) : RoomKeyRequestContent()

    @Serializable
    data class RequestedKeyInfo(
        /**
         * The encryption algorithm the requested key in this event is to be used with.
         */
        val algorithm: String,

        /**
         * The room where the key is used.
         */
        @SerialName("room_id")
        val roomId: String,

        /**
         * The Curve25519 key of the device which initiated the session originally.
         */
        @SerialName("sender_key")
        val senderKey: String,

        /**
         * The ID of the session that the key is for.
         */
        @SerialName("session_id")
        val sessionId: String
    )
}
