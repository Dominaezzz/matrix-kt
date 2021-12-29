package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * This endpoint is used to send send-to-device events to a set of
 * client devices.
 */
public class SendToDevice(
    public override val url: Url,
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Put, SendToDevice.Url, SendToDevice.Body, Unit> {
    @Resource("_matrix/client/r0/sendToDevice/{eventType}/{txnId}")
    @Serializable
    public class Url(
        /**
         * The type of event to send.
         */
        public val eventType: String,
        /**
         * The transaction ID for this event. Clients should generate an
         * ID unique across requests with the same access token; it will be
         * used by the server to ensure idempotency of requests.
         */
        public val txnId: String
    )

    @Serializable
    public class Body(
        /**
         * The messages to send. A map from user ID, to a map from
         * device ID to message body. The device ID may also be `*`,
         * meaning all known devices for the user.
         */
        public val messages: Map<String, Map<String, JsonObject>>
    )
}
