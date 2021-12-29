package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This API sets the given user's avatar URL. You must have permission to
 * set this user's avatar URL, e.g. you need to have their ``access_token``.
 */
public class SetAvatarUrl(
    public override val url: Url,
    /**
     * The avatar url info.
     */
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Put, SetAvatarUrl.Url, SetAvatarUrl.Body, Unit> {
    @Resource("_matrix/client/r0/profile/{userId}/avatar_url")
    @Serializable
    public class Url(
        /**
         * The user whose avatar URL to set.
         */
        public val userId: String
    )

    @Serializable
    public class Body(
        /**
         * The new avatar URL for this user.
         */
        @SerialName("avatar_url")
        public val avatarUrl: String? = null
    )
}
