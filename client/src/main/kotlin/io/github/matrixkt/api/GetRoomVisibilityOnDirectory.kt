package io.github.matrixkt.api

import io.github.matrixkt.models.RoomVisibility
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.github.matrixkt.utils.resource.Resource
import kotlinx.serialization.Serializable

/**
 * Gets the visibility of a given room on the server's public room directory.
 */
public class GetRoomVisibilityOnDirectory(
    public override val url: Url
) : MatrixRpc<RpcMethod.Get, GetRoomVisibilityOnDirectory.Url, Any?, GetRoomVisibilityOnDirectory.Response> {
    public override val body: Any?
        get() = null

    @Resource("/_matrix/client/r0/directory/list/room/{roomId}")
    @Serializable
    public class Url(
        /**
         * The room ID.
         */
        public val roomId: String
    )

    @Serializable
    public class Response(
        /**
         * The visibility of the room in the directory.
         */
        public val visibility: RoomVisibility? = null
    )
}
