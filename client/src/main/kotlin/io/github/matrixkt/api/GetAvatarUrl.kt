package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.github.matrixkt.utils.resource.Resource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Get the user's avatar URL. This API may be used to fetch the user's
 * own avatar URL or to query the URL of other users; either locally or
 * on remote homeservers.
 */
public class GetAvatarUrl(
    public override val url: Url
) : MatrixRpc<RpcMethod.Get, GetAvatarUrl.Url, Any?, GetAvatarUrl.Response> {
    public override val body: Any?
        get() = null

    @Resource("/_matrix/client/r0/profile/{userId}/avatar_url")
    @Serializable
    public class Url(
        /**
         * The user whose avatar URL to get.
         */
        public val userId: String
    )

    @Serializable
    public class Response(
        /**
         * The user's avatar URL if they have set one, otherwise not present.
         */
        @SerialName("avatar_url")
        public val avatarUrl: String? = null
    )
}
