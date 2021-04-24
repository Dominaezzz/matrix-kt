package io.github.matrixkt.api

import io.github.matrixkt.models.Presence
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.github.matrixkt.utils.resource.Resource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Get the given user's presence state.
 */
public class GetPresence(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetPresence.Url, Any?, GetPresence.Response> {
    public override val body: Any?
        get() = null

    @Resource("/_matrix/client/r0/presence/{userId}/status")
    @Serializable
    public class Url(
        /**
         * The user whose presence state to get.
         */
        public val userId: String
    )

    @Serializable
    public class Response(
        /**
         * Whether the user is currently active
         */
        @SerialName("currently_active")
        public val currentlyActive: Boolean? = null,
        /**
         * The length of time in milliseconds since an action was performed
         * by this user.
         */
        @SerialName("last_active_ago")
        public val lastActiveAgo: Long? = null,
        /**
         * This user's presence.
         */
        public val presence: Presence
    )
}
