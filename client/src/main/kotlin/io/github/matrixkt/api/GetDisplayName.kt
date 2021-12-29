package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Get the user's display name. This API may be used to fetch the user's
 * own displayname or to query the name of other users; either locally or
 * on remote homeservers.
 */
public class GetDisplayName(
    public override val url: Url
) : MatrixRpc<RpcMethod.Get, GetDisplayName.Url, Nothing, GetDisplayName.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/profile/{userId}/displayname")
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
