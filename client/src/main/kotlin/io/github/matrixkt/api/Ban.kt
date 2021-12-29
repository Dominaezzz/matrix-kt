package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Ban a user in the room. If the user is currently in the room, also kick them.
 *
 * When a user is banned from a room, they may not join it or be invited to it until they are
 * unbanned.
 *
 * The caller must have the required power level in order to perform this operation.
 */
public class Ban(
    public override val url: Url,
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, Ban.Url, Ban.Body, Unit> {
    @Resource("_matrix/client/r0/rooms/{roomId}/ban")
    @Serializable
    public class Url(
        /**
         * The room identifier (not alias) from which the user should be banned.
         */
        public val roomId: String
    )

    @Serializable
    public class Body(
        /**
         * The reason the user has been banned. This will be supplied as the ``reason`` on the
         * target's updated `m.room.member`_ event.
         */
        public val reason: String? = null,
        /**
         * The fully qualified user ID of the user being banned.
         */
        @SerialName("user_id")
        public val userId: String
    )
}
