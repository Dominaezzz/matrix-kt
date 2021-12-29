package io.github.matrixkt.api

import io.github.matrixkt.models.events.MatrixEvent
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * This will listen for new events and return them to the caller. This will
 * block until an event is received, or until the ``timeout`` is reached.
 *
 * This endpoint was deprecated in r0 of this specification. Clients
 * should instead call the |/sync|_ API with a ``since`` parameter. See
 * the `migration guide
 * <https://matrix.org/docs/guides/client-server-migrating-from-v1.html#deprecated-endpoints>`_.
 */
@Suppress("DEPRECATION")
@Deprecated("This endpoint has been deprecated.")
public class GetEvents(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetEvents.Url, Nothing, GetEvents.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/events")
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
        public val timeout: Long? = null
    )

    @Serializable
    public class Response(
        /**
         * An array of events.
         */
        public val chunk: List<MatrixEvent> = emptyList(),
        /**
         * A token which correlates to the last value in ``chunk``. This
         * token should be used in the next request to ``/events``.
         */
        public val end: String? = null,
        /**
         * A token which correlates to the first value in ``chunk``. This
         * is usually the same token supplied to ``from=``.
         */
        public val start: String? = null
    )
}