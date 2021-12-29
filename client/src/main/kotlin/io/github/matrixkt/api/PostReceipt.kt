package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * This API updates the marker for the given receipt type to the event ID
 * specified.
 */
public class PostReceipt(
    public override val url: Url,
    /**
     * Extra receipt information to attach to ``content`` if any. The
     * server will automatically set the ``ts`` field.
     */
    public override val body: JsonObject
) : MatrixRpc.WithAuth<RpcMethod.Post, PostReceipt.Url, JsonObject, Unit> {
    @Resource("_matrix/client/r0/rooms/{roomId}/receipt/{receiptType}/{eventId}")
    @Serializable
    public class Url(
        /**
         * The room in which to send the event.
         */
        public val roomId: String,
        /**
         * The type of receipt to send.
         */
        public val receiptType: String,
        /**
         * The event ID to acknowledge up to.
         */
        public val eventId: String
    )
}