package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.github.matrixkt.utils.resource.Resource
import kotlinx.serialization.Serializable

/**
 * This API stops a user participating in a particular room.
 *
 * If the user was already in the room, they will no longer be able to see
 * new events in the room. If the room requires an invite to join, they
 * will need to be re-invited before they can re-join.
 *
 * If the user was invited to the room, but had not joined, this call
 * serves to reject the invite.
 *
 * The user will still be allowed to retrieve history from the room which
 * they were previously allowed to see.
 */
public class LeaveRoom(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Post, LeaveRoom.Url, Nothing, Unit> {
    public override val body: Nothing
        get() = TODO()

    @Resource("/_matrix/client/r0/rooms/{roomId}/leave")
    @Serializable
    public class Url(
        /**
         * The room identifier to leave.
         */
        public val roomId: String
    )
}
