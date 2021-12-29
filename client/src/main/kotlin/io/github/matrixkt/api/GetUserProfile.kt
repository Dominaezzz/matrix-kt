package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Get the combined profile information for this user. This API may be used
 * to fetch the user's own profile information or other users; either
 * locally or on remote homeservers. This API may return keys which are not
 * limited to ``displayname`` or ``avatar_url``.
 */
public class GetUserProfile(
    public override val url: Url
) : MatrixRpc<RpcMethod.Get, GetUserProfile.Url, Nothing, GetUserProfile.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/profile/{userId}")
    @Serializable
    public class Url(
        /**
         * The user whose profile information to get.
         */
        public val userId: String
    )

    @Serializable
    public class Response(
        /**
         * The user's avatar URL if they have set one, otherwise not present.
         */
        @SerialName("avatar_url")
        public val avatarUrl: String? = null,
        /**
         * The user's display name if they have set one, otherwise not present.
         */
        public val displayname: String? = null
    )
}
