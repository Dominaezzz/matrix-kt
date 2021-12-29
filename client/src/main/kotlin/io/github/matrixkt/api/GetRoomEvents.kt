package io.github.matrixkt.api

import io.github.matrixkt.models.Direction
import io.github.matrixkt.models.events.MatrixEvent
import io.github.matrixkt.models.events.StateEvent
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * This API returns a list of message and state events for a room. It uses
 * pagination query parameters to paginate history in the room.
 *
 * *Note*: This endpoint supports lazy-loading of room member events. See
 * [Lazy-loading room members](/client-server-api/#lazy-loading-room-members) for more information.
 */
public class GetRoomEvents(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetRoomEvents.Url, Nothing, GetRoomEvents.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/rooms/{roomId}/messages")
    @Serializable
    public class Url(
        /**
         * The room to get events from.
         */
        public val roomId: String,
        /**
         * The token to start returning events from. This token can be obtained
         * from a `prev_batch` or `next_batch` token returned by the `/sync` endpoint,
         * or from an `end` token returned by a previous request to this endpoint.
         *
         * This endpoint can also accept a value returned as a `start` token
         * by a previous request to this endpoint, though servers are not
         * required to support this. Clients should not rely on the behaviour.
         */
        public val from: String,
        /**
         * The token to stop returning events at. This token can be obtained from
         * a `prev_batch` or `next_batch` token returned by the `/sync` endpoint,
         * or from an `end` token returned by a previous request to this endpoint.
         */
        public val to: String? = null,
        /**
         * The direction to return events from. If this is set to `f`, events
         * will be returned in chronological order starting at `from`. If it
         * is set to `b`, events will be returned in *reverse* chronological
         * order, again starting at `from`.
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
         * A list of room events. The order depends on the `dir` parameter.
         * For `dir=b` events will be in reverse-chronological order,
         * for `dir=f` in chronological order. (The exact definition of `chronological`
         * is dependent on the server implementation.)
         *
         * Note that an empty `chunk` does not *necessarily* imply that no more events
         * are available. Clients should continue to paginate until no `end` property
         * is returned.
         */
        public val chunk: List<MatrixEvent>,
        /**
         * A token corresponding to the end of `chunk`. This token can be passed
         * back to this endpoint to request further events.
         *
         * If no further events are available (either because we have
         * reached the start of the timeline, or because the user does
         * not have permission to see any more events), this property
         * is omitted from the response.
         */
        public val end: String? = null,
        /**
         * A token corresponding to the start of `chunk`. This will be the same as
         * the value given in `from`.
         */
        public val start: String,
        /**
         * A list of state events relevant to showing the `chunk`. For example, if
         * `lazy_load_members` is enabled in the filter then this may contain
         * the membership events for the senders of events in the `chunk`.
         *
         * Unless `include_redundant_members` is `true`, the server
         * may remove membership events which would have already been
         * sent to the client in prior calls to this endpoint, assuming
         * the membership of those members has not changed.
         */
        public val state: List<StateEvent<JsonObject, JsonObject>> = emptyList()
    )
}
