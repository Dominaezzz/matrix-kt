package io.github.matrixkt.api

import io.github.matrixkt.models.Presence
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This API sets the given user's presence state. When setting the status,
 * the activity time is updated to reflect that activity; the client does
 * not need to specify the ``last_active_ago`` field. You cannot set the
 * presence state of another user.
 */
public class SetPresence(
    public override val url: Url,
    /**
     * The updated presence state.
     */
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Put, SetPresence.Url, SetPresence.Body, Unit> {
    @Resource("_matrix/client/r0/presence/{userId}/status")
    @Serializable
    public class Url(
        /**
         * The user whose presence state to update.
         */
        public val userId: String
    )

    @Serializable
    public class Body(
        /**
         * The new presence state.
         */
        public val presence: Presence,
        /**
         * The status message to attach to this state.
         */
        @SerialName("status_msg")
        public val statusMsg: String? = null
    )
}
