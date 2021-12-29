package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Strips all information out of an event which isn't critical to the
 * integrity of the server-side representation of the room.
 *
 * This cannot be undone.
 *
 * Any user with a power level greater than or equal to the `m.room.redaction`
 * event power level may send redaction events in the room. If the user's power
 * level greater is also greater than or equal to the `redact` power level
 * of the room, the user may redact events sent by other users.
 *
 * Server administrators may redact events sent by users on their server.
 */
public class RedactEvent(
    public override val url: Url,
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Put, RedactEvent.Url, RedactEvent.Body, RedactEvent.Response> {
    @Resource("_matrix/client/r0/rooms/{roomId}/redact/{eventId}/{txnId}")
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
