package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.github.matrixkt.utils.resource.Resource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Gets information about the owner of a given access token.
 *
 * Note that, as with the rest of the Client-Server API,
 * Application Services may masquerade as users within their
 * namespace by giving a ``user_id`` query parameter. In this
 * situation, the server should verify that the given ``user_id``
 * is registered by the appservice, and return it in the response
 * body.
 */
public class GetTokenOwner(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetTokenOwner.Url, Any?, GetTokenOwner.Response> {
    public override val body: Any?
        get() = null

    @Resource("/_matrix/client/r0/account/whoami")
    @Serializable
    public class Url

    @Serializable
    public class Response(
        /**
         * The user id that owns the access token.
         */
        @SerialName("user_id")
        public val userId: String
    )
}
