package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.github.matrixkt.utils.resource.Resource
import kotlinx.serialization.Serializable

/**
 * Get the user's display name. This API may be used to fetch the user's
 * own displayname or to query the name of other users; either locally or
 * on remote homeservers.
 */
public class GetDisplayName(
    public override val url: Url
) : MatrixRpc<RpcMethod.Get, GetDisplayName.Url, Any?, GetDisplayName.Response> {
    public override val body: Any?
        get() = null

    @Resource("/_matrix/client/r0/profile/{userId}/displayname")
    @Serializable
    public class Url(
        /**
         * The user whose display name to get.
         */
        public val userId: String
    )

    @Serializable
    public class Response(
        /**
         * The user's display name if they have set one, otherwise not present.
         */
        public val displayname: String? = null
    )
}
