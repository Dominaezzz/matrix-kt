package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Gets information about the owner of a given access token.
 *
 * Note that, as with the rest of the Client-Server API,
 * Application Services may masquerade as users within their
 * namespace by giving a `user_id` query parameter. In this
 * situation, the server should verify that the given `user_id`
 * is registered by the appservice, and return it in the response
 * body.
 */
public class GetTokenOwner(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetTokenOwner.Url, Nothing, GetTokenOwner.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/account/whoami")
    @Serializable
    public class Url

    @Serializable
    public class Response(
        /**
         * Device ID associated with the access token. If no device
         * is associated with the access token (such as in the case
         * of application services) then this field can be omitted.
         * Otherwise this is required.
         */
        @SerialName("device_id")
        public val deviceId: String? = null,

        /**
         * The user ID that owns the access token.
         */
        @SerialName("user_id")
        public val userId: String
    )
}
