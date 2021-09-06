package io.github.matrixkt.models.events.contents.secret

import kotlinx.serialization.*
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * Sent by a client to request a secret from another device or to cancel a previous request.
 * It is sent as an unencrypted to-device event.
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialName("m.secret.request")
@JsonClassDiscriminator("action")
@Serializable
public sealed class RequestContent {
    /**
     * The ID of the device requesting the secret.
     */
    @SerialName("requesting_device_id")
    public abstract val requestingDeviceId: String

    /**
     * A random string uniquely identifying (with respect to the requester and the target) the target for a secret.
     * If the secret is requested from multiple devices at the same time, the same ID may be used for every target.
     * The same ID is also used in order to cancel a previous request.
     */
    @SerialName("request_id")
    public abstract val requestId: String

    @SerialName("request")
    @Serializable
    public data class Request(
        /**
         * The name of the secret that is being requested.
         */
        val name: String,

        @SerialName("requesting_device_id")
        override val requestingDeviceId: String,

        @SerialName("request_id")
        override val requestId: String
    ) : RequestContent()

    @SerialName("request_cancellation")
    @Serializable
    public data class Cancellation(
        @SerialName("requesting_device_id")
        override val requestingDeviceId: String,

        @SerialName("request_id")
        override val requestId: String
    ) : RequestContent()
}
