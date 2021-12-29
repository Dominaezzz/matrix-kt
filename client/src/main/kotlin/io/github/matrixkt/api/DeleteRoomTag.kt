package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Remove a tag from the room.
 */
public class DeleteRoomTag(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Delete, DeleteRoomTag.Url, Nothing, Unit> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/user/{userId}/rooms/{roomId}/tags/{tag}")
    @Serializable
    public class Url(
        /**
         * The id of the user to remove a tag for. The access token must be
         * authorized to make requests for this user ID.
         */
        public val userId: String,
        /**
         * The ID of the room to remove a tag from.
         */
        public val roomId: String,
        /**
         * The tag to remove.
         */
        public val tag: String
    )
}
