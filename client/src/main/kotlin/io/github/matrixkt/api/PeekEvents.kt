package io.github.matrixkt.api

import io.github.matrixkt.models.events.MatrixEvent
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This will listen for new events related to a particular room and return
 * them to the caller. This will block until an event is received, or until
 * the `timeout` is reached.
 *
 * This API is the same as the normal `/events` endpoint, but can be
 * called by users who have not joined the room.
 *
 * Note that the normal `/events` endpoint has been deprecated. This
 * API will also be deprecated at some point, but its replacement is not
 * yet known.
 */
public class PeekEvents(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, PeekEvents.Url, Nothing, PeekEvents.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/events ")
    @Serializable
    public class Url(
        /**
         * The token to stream from. This token is either from a previous
         * request to this API or from the initial sync API.
         */
        public val from: String? = null,
        /**
         * The maximum time in milliseconds to wait for an event.
         */
        public val timeout: Long? = null,
        /**
         * The room ID for which events should be returned.
         */
        @SerialName("room_id")
        public val roomId: String? = null
    )

    @Serializable
    public class Response(
        /**
         * An array of events.
         */
        public val chunk: List<MatrixEvent>? = null,
        /**
         * A token which correlates to the last value in `chunk`. This
         * token should be used in the next request to `/events`.
         */
        public val end: String? = null,
        /**
         * A token which correlates to the first value in `chunk`. This
         * is usually the same token supplied to `from=`.
         */
        public val start: String? = null
    )
}
