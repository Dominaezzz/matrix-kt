package io.github.matrixkt.api

import io.github.matrixkt.models.events.MatrixEvent
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.github.matrixkt.utils.resource.Resource
import kotlinx.serialization.Serializable

/**
 * Get the state events for the current state of a room.
 */
public class GetRoomState(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetRoomState.Url, Any?, List<MatrixEvent>> {
    public override val body: Any?
        get() = null

    @Resource("/_matrix/client/r0/rooms/{roomId}/state")
    @Serializable
    public class Url(
        /**
         * The room to look up the state for.
         */
        public val roomId: String
    )
}
