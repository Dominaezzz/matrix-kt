package io.github.matrixkt.api

import io.github.matrixkt.models.events.MatrixEvent
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Get a single event based on ``event_id``. You must have permission to
 * retrieve this event e.g. by being a member in the room for this event.
 *
 * This endpoint was deprecated in r0 of this specification. Clients
 * should instead call the |/rooms/{roomId}/event/{eventId}|_ API
 * or the |/rooms/{roomId}/context/{eventId}|_ API.
 */
@Suppress("DEPRECATION")
@Deprecated("This endpoint has been deprecated.")
public class GetOneEvent(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetOneEvent.Url, Nothing, MatrixEvent> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/events/{eventId}")
    @Serializable
    public class Url(
        /**
         * The event ID to get.
         */
        public val eventId: String
    )
}
