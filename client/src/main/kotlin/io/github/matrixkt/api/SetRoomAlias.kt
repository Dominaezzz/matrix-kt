package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Create a new mapping from room alias to room ID.
 */
public class SetRoomAlias(
    public override val url: Url,
    /**
     * Information about this room alias.
     */
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Put, SetRoomAlias.Url, SetRoomAlias.Body, Unit> {
    @Resource("_matrix/client/r0/directory/room/{roomAlias}")
    @Serializable
    public class Url(
        /**
         * The room alias to set.
         */
        public val roomAlias: String
    )

    @Serializable
    public class Body(
        /**
         * The room ID to set.
         */
        @SerialName("room_id")
        public val roomId: String
    )
}
