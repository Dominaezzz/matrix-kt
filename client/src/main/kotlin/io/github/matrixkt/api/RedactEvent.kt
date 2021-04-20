package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.github.matrixkt.utils.resource.Resource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Strips all information out of an event which isn't critical to the
 * integrity of the server-side representation of the room.
 *
 * This cannot be undone.
 *
 * Users may redact their own events, and any user with a power level
 * greater than or equal to the ``redact`` power level of the room may
 * redact events there.
 */
public class RedactEvent(
    public override val url: Url,
    public override val body: Body? = null
) : MatrixRpc.WithAuth<RpcMethod.Put, RedactEvent.Url, RedactEvent.Body?, RedactEvent.Response> {
    @Resource("/_matrix/client/r0/rooms/{roomId}/redact/{eventId}/{txnId}")
    @Serializable
    public class Url(
        /**
         * The room from which to redact the event.
         */
        public val roomId: String,
        /**
         * The ID of the event to redact
         */
        public val eventId: String,
        /**
         * The transaction ID for this event. Clients should generate a
         * unique ID; it will be used by the server to ensure idempotency of requests.
         */
        public val txnId: String
    )

    @Serializable
    public class Body(
        /**
         * The reason for the event being redacted.
         */
        public val reason: String? = null
    )

    @Serializable
    public class Response(
        /**
         * A unique identifier for the event.
         */
        @SerialName("event_id")
        public val eventId: String? = null
    )
}
