package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.github.matrixkt.utils.resource.Resource
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * .. For backwards compatibility with older links...
 * .. _`get-matrix-client-unstable-rooms-roomid-state-eventtype`:
 *
 * Looks up the contents of a state event in a room. If the user is
 * joined to the room then the state is taken from the current
 * state of the room. If the user has left the room then the state is
 * taken from the state of the room when they left.
 */
public class GetRoomStateWithKey(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetRoomStateWithKey.Url, Any?, JsonObject> {
    public override val body: Any?
        get() = null

    @Resource("/_matrix/client/r0/rooms/{roomId}/state/{eventType}/{stateKey}")
    @Serializable
    public class Url(
        /**
         * The room to look up the state in.
         */
        public val roomId: String,
        /**
         * The type of state to look up.
         */
        public val eventType: String,
        /**
         * The key of the state to look up. Defaults to an empty string. When
         * an empty string, the trailing slash on this endpoint is optional.
         */
        public val stateKey: String
    )
}
