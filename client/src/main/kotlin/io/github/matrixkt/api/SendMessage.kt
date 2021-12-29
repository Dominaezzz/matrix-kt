package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * This endpoint is used to send a message event to a room. Message events
 * allow access to historical events and pagination, making them suited
 * for "once-off" activity in a room.
 *
 * The body of the request should be the content object of the event; the
 * fields in this object will vary depending on the type of event. See
 * `Room Events`_ for the m. event specification.
 */
public class SendMessage(
    public override val url: Url,
    public override val body: JsonObject
) : MatrixRpc.WithAuth<RpcMethod.Put, SendMessage.Url, JsonObject, SendMessage.Response> {
    @Resource("_matrix/client/r0/rooms/{roomId}/send/{eventType}/{txnId}")
    @Serializable
    public class Url(
        /**
         * The room to send the event to.
         */
        public val roomId: String,
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
    public class Response(
        /**
         * A unique identifier for the event.
         */
        @SerialName("event_id")
        public val eventId: String
    )
}
