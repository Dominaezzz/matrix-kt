package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Unban a user from the room. This allows them to be invited to the room,
 * and join if they would otherwise be allowed to join according to its join rules.
 *
 * The caller must have the required power level in order to perform this operation.
 */
public class Unban(
    public override val url: Url,
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, Unban.Url, Unban.Body, Unit> {
    @Resource("_matrix/client/r0/rooms/{roomId}/unban")
    @Serializable
    public class Url(
        /**
         * The room identifier (not alias) from which the user should be unbanned.
         */
        public val roomId: String
    )

    @Serializable
    public class Body(
        /**
         * Optional reason to be included as the `reason` on the subsequent
         * membership event.
         */
        public val reason: String? = null,
        /**
         * The fully qualified user ID of the user being unbanned.
         */
        @SerialName("user_id")
        public val userId: String
    )
}
