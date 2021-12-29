package io.github.matrixkt.api

import io.github.matrixkt.models.events.StateEvent
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Get the state events for the current state of a room.
 */
public class GetRoomState(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetRoomState.Url, Nothing, List<StateEvent<JsonObject, JsonObject>>> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/rooms/{roomId}/state")
    @Serializable
    public class Url(
        /**
         * The room to look up the state for.
         */
        public val roomId: String
    )
}
