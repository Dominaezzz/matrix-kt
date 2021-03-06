package io.github.matrixkt.api

import io.github.matrixkt.models.events.MatrixEvent
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.github.matrixkt.utils.resource.Resource
import kotlinx.serialization.Serializable

/**
 * Get a single event based on ``roomId/eventId``. You must have permission to
 * retrieve this event e.g. by being a member in the room for this event.
 */
public class GetOneRoomEvent(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetOneRoomEvent.Url, Any?, MatrixEvent> {
    public override val body: Any?
        get() = null

    @Resource("/_matrix/client/r0/rooms/{roomId}/event/{eventId}")
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
