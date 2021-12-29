package io.github.matrixkt.api

import io.github.matrixkt.models.events.MatrixEvent
import io.github.matrixkt.models.events.StateEvent
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * This API returns a number of events that happened just before and
 * after the specified event. This allows clients to get the context
 * surrounding an event.
 *
 * *Note*: This endpoint supports lazy-loading of room member events. See
 * `Lazy-loading room members <#lazy-loading-room-members>`_ for more information.
 */
public class GetEventContext(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetEventContext.Url, Nothing, GetEventContext.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/rooms/{roomId}/context/{eventId}")
    @Serializable
    public class Url(
        /**
         * The room to get events from.
         */
        public val roomId: String,
        /**
         * The event to get context around.
         */
        public val eventId: String,
        /**
         * The maximum number of events to return. Default: 10.
         */
        public val limit: Long? = null,
        /**
         * A JSON ``RoomEventFilter`` to filter the returned events with. The
         * filter is only applied to ``events_before``, ``events_after``, and
         * ``state``. It is not applied to the ``event`` itself. The filter may
         * be applied before or/and after the ``limit`` parameter - whichever the
         * homeserver prefers.
         *
         * See `Filtering <#filtering>`_ for more information.
         */
        public val filter: String? = null
    )

    @Serializable
    public class Response(
        /**
         * A token that can be used to paginate forwards with.
         */
        public val end: String? = null,
        /**
         * Details of the requested event.
         */
        public val event: MatrixEvent? = null,
        /**
         * A list of room events that happened just after the
         * requested event, in chronological order.
         */
        @SerialName("events_after")
        public val eventsAfter: List<MatrixEvent> = emptyList(),
        /**
         * A list of room events that happened just before the
         * requested event, in reverse-chronological order.
         */
        @SerialName("events_before")
        public val eventsBefore: List<MatrixEvent> = emptyList(),
        /**
         * A token that can be used to paginate backwards with.
         */
        public val start: String? = null,
        /**
         * The state of the room at the last event returned.
         */
        public val state: List<StateEvent<JsonObject, JsonObject>> = emptyList()
    )
}
