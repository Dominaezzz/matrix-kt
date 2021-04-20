package io.github.matrixkt.api

import io.github.matrixkt.models.Direction
import io.github.matrixkt.models.events.MatrixEvent
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.github.matrixkt.utils.resource.Resource
import kotlinx.serialization.Serializable

/**
 * This API returns a list of message and state events for a room. It uses
 * pagination query parameters to paginate history in the room.
 *
 * *Note*: This endpoint supports lazy-loading of room member events. See
 * `Lazy-loading room members <#lazy-loading-room-members>`_ for more information.
 */
public class GetRoomEvents(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetRoomEvents.Url, Any?, GetRoomEvents.Response> {
    public override val body: Any?
        get() = null

    @Resource("/_matrix/client/r0/rooms/{roomId}/messages")
    @Serializable
    public class Url(
        /**
         * The room to get events from.
         */
        public val roomId: String,
        /**
         * The token to start returning events from. This token can be obtained
         * from a ``prev_batch`` token returned for each room by the sync API,
         * or from a ``start`` or ``end`` token returned by a previous request
         * to this endpoint.
         */
        public val from: String,
        /**
         * The token to stop returning events at. This token can be obtained from
         * a ``prev_batch`` token returned for each room by the sync endpoint,
         * or from a ``start`` or ``end`` token returned by a previous request to
         * this endpoint.
         */
        public val to: String? = null,
        /**
         * The direction to return events from.
         */
        public val dir: Direction,
        /**
         * The maximum number of events to return. Default: 10.
         */
        public val limit: Long? = null,
        /**
         * A JSON RoomEventFilter to filter returned events with.
         */
        public val filter: String? = null
    )

    @Serializable
    public class Response(
        /**
         * A list of room events. The order depends on the ``dir`` parameter.
         * For ``dir=b`` events will be in reverse-chronological order,
         * for ``dir=f`` in chronological order, so that events start
         * at the ``from`` point.
         */
        public val chunk: List<MatrixEvent> = emptyList(),
        /**
         * The token the pagination ends at. If ``dir=b`` this token should
         * be used again to request even earlier events.
         */
        public val end: String? = null,
        /**
         * The token the pagination starts from. If ``dir=b`` this will be
         * the token supplied in ``from``.
         */
        public val start: String? = null,
        /**
         * A list of state events relevant to showing the ``chunk``. For example, if
         * ``lazy_load_members`` is enabled in the filter then this may contain
         * the membership events for the senders of events in the ``chunk``.
         *
         * Unless ``include_redundant_members`` is ``true``, the server
         * may remove membership events which would have already been
         * sent to the client in prior calls to this endpoint, assuming
         * the membership of those members has not changed.
         */
        public val state: List<MatrixEvent> = emptyList()
    )
}
