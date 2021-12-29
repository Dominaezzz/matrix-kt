package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlin.String
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
    public override val url: Url,
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, LeaveRoom.Url, LeaveRoom.Body, Unit> {
    @Resource("_matrix/client/r0/rooms/{roomId}/leave")
    @Serializable
    public class Url(
        /**
         * The room identifier to leave.
         */
        public val roomId: String
    )

    @Serializable
    public class Body(
        /**
         * Optional reason to be included as the `reason` on the subsequent
         * membership event.
         */
        public val reason: String? = null
    )
}
