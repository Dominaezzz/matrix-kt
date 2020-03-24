package io.github.matrixkt.models.events.contents

import kotlinx.serialization.*
import kotlinx.serialization.json.JsonInput
import kotlinx.serialization.json.content

@Serializable(RoomKeyRequestContent.Serializer::class)
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

    object Serializer : KSerializer<RoomKeyRequestContent> {
        override val descriptor = SerialDescriptor("RoomKeyRequestContent", PolymorphicKind.SEALED)

        override fun deserialize(decoder: Decoder): RoomKeyRequestContent {
            require(decoder is JsonInput)
            val jsonObj = decoder.decodeJson().jsonObject

            val action = jsonObj.getValue("action").content
            val serializer = when (action) {
                "request" -> Request.serializer()
                "request_cancellation" -> Cancellation.serializer()
                else -> throw SerializationException("No serializer for 'action' = '$action'")
            }

            return decoder.json.fromJson(serializer, jsonObj)
        }

        override fun serialize(encoder: Encoder, value: RoomKeyRequestContent) {
            val (action, serializer) = when (value) {
                is Request -> "request" to Request.serializer()
                is Cancellation -> "request_cancellation" to Cancellation.serializer()
            }
            encoder.encodeStructure(descriptor) {
                encodeStringElement(descriptor, 0, action)
                @Suppress("UNCHECKED_CAST")
                encodeSerializableElement(descriptor, 1, serializer as KSerializer<RoomKeyRequestContent>, value)
            }
        }
    }

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
