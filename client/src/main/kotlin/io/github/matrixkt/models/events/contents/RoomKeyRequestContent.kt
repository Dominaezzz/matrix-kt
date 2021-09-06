package io.github.matrixkt.models.events.contents

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * This event type is used to request keys for end-to-end encryption.
 * It is sent as an unencrypted [to-device](https://matrix.org/docs/spec/client_server/r0.6.0#to-device) event.
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialName("m.room_key_request")
@JsonClassDiscriminator("action")
@Serializable
public sealed class RoomKeyRequestContent {
    /**
     * ID of the device requesting the key.
     */
    @SerialName("requesting_device_id")
    public abstract val requestingDeviceId: String

    /**
     * A random string uniquely identifying the request for a key.
     * If the key is requested multiple times, it should be reused.
     * It should also reused in order to cancel a request.
     */
    @SerialName("request_id")
    public abstract val requestId: String

    @SerialName("request")
    @Serializable
    public data class Request(
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
    public data class Cancellation(
        @SerialName("requesting_device_id")
        override val requestingDeviceId: String,

        @SerialName("request_id")
        override val requestId: String
    ) : RoomKeyRequestContent()

    @Serializable
    public data class RequestedKeyInfo(
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
