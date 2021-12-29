package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * This API stops a user remembering about a particular room.
 *
 * In general, history is a first class citizen in Matrix. After this API
 * is called, however, a user will no longer be able to retrieve history
 * for this room. If all users on a homeserver forget a room, the room is
 * eligible for deletion from that homeserver.
 *
 * If the user is currently joined to the room, they must leave the room
 * before calling this API.
 */
public class ForgetRoom(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Post, ForgetRoom.Url, Nothing, Unit> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/rooms/{roomId}/forget")
    @Serializable
    public class Url(
        /**
         * The room identifier to forget.
         */
        public val roomId: String
    )
}
