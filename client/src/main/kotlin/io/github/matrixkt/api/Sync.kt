package io.github.matrixkt.api

import io.github.matrixkt.models.Presence
import io.github.matrixkt.models.sync.SyncResponse
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Synchronise the client's state with the latest state on the server.
 * Clients use this API when they first log in to get an initial snapshot
 * of the state on the server, and then continue to call this API to get
 * incremental deltas to the state, and to receive new messages.
 *
 * *Note*: This endpoint supports lazy-loading. See `Filtering <#filtering>`_
 * for more information. Lazy-loading members is only supported on a ``StateFilter``
 * for this endpoint. When lazy-loading is enabled, servers MUST include the
 * syncing user's own membership event when they join a room, or when the
 * full state of rooms is requested, to aid discovering the user's avatar &
 * displayname.
 *
 * Like other members, the user's own membership event is eligible
 * for being considered redundant by the server. When a sync is ``limited``,
 * the server MUST return membership events for events in the gap
 * (between ``since`` and the start of the returned timeline), regardless
 * as to whether or not they are redundant.  This ensures that joins/leaves
 * and profile changes which occur during the gap are not lost.
 */
public class Sync(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, Sync.Url, Nothing, SyncResponse> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/sync")
    @Serializable
    public class Url(
        /**
         * The ID of a filter created using the filter API or a filter JSON
         * object encoded as a string. The server will detect whether it is
         * an ID or a JSON object by whether the first character is a ``"{"``
         * open brace. Passing the JSON inline is best suited to one off
         * requests. Creating a filter using the filter API is recommended for
         * clients that reuse the same filter multiple times, for example in
         * long poll requests.
         *
         * See `Filtering <#filtering>`_ for more information.
         */
        public val filter: String? = null,
        /**
         * A point in time to continue a sync from.
         */
        public val since: String? = null,
        /**
         * Controls whether to include the full state for all rooms the user
         * is a member of.
         *
         * If this is set to ``true``, then all state events will be returned,
         * even if ``since`` is non-empty. The timeline will still be limited
         * by the ``since`` parameter. In this case, the ``timeout`` parameter
         * will be ignored and the query will return immediately, possibly with
         * an empty timeline.
         *
         * If ``false``, and ``since`` is non-empty, only state which has
         * changed since the point indicated by ``since`` will be returned.
         *
         * By default, this is ``false``.
         */
        @SerialName("full_state")
        public val fullState: Boolean? = null,
        /**
         * Controls whether the client is automatically marked as online by
         * polling this API. If this parameter is omitted then the client is
         * automatically marked as online when it uses this API. Otherwise if
         * the parameter is set to "offline" then the client is not marked as
         * being online when it uses this API. When set to "unavailable", the
         * client is marked as being idle.
         */
        @SerialName("set_presence")
        public val setPresence: Presence? = null,
        /**
         * The maximum time to wait, in milliseconds, before returning this
         * request. If no events (or other data) become available before this
         * time elapses, the server will return a response with empty fields.
         *
         * By default, this is ``0``, so the server will return immediately
         * even if the response is empty.
         */
        public val timeout: Long? = null
    )
}
