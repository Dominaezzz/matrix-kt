package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Kick a user from the room.
 *
 * The caller must have the required power level in order to perform this operation.
 *
 * Kicking a user adjusts the target member's membership state to be ``leave`` with an
 * optional ``reason``. Like with other membership changes, a user can directly adjust
 * the target member's state by making a request to ``/rooms/<room id>/state/m.room.member/<user
 * id>``.
 */
public class Kick(
    public override val url: Url,
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, Kick.Url, Kick.Body, Unit> {
    @Resource("_matrix/client/r0/rooms/{roomId}/kick")
    @Serializable
    public class Url(
        /**
         * The room identifier (not alias) from which the user should be kicked.
         */
        public val roomId: String
    )

    @Serializable
    public class Body(
        /**
         * The reason the user has been kicked. This will be supplied as the
         * ``reason`` on the target's updated `m.room.member`_ event.
         */
        public val reason: String? = null,
        /**
         * The fully qualified user ID of the user being kicked.
         */
        @SerialName("user_id")
        public val userId: String
    )
}
