package io.github.matrixkt.api

import io.github.matrixkt.models.events.MatrixEvent
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Get a single event based on ``roomId/eventId``. You must have permission to
 * retrieve this event e.g. by being a member in the room for this event.
 */
public class GetOneRoomEvent(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetOneRoomEvent.Url, Nothing, MatrixEvent> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/rooms/{roomId}/event/{eventId}")
    @Serializable
    public class Url(
        /**
         * The ID of the room the event is in.
         */
        public val roomId: String,
        /**
         * The event ID to get.
         */
        public val eventId: String
    )
}
