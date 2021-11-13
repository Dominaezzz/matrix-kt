package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.github.matrixkt.utils.resource.Resource
import kotlinx.serialization.Serializable

/**
 * A web-based Matrix client should instruct the user's browser to
 * navigate to this endpoint in order to log in via SSO.
 *
 * The server MUST respond with an HTTP redirect to the SSO interface.
 */
public class RedirectToSSO(
    public override val url: Url
) : MatrixRpc<RpcMethod.Get, RedirectToSSO.Url, Nothing, Nothing> {
    public override val body: Nothing
        get() = TODO()

    @Resource("/_matrix/client/r0/login/sso/redirect")
    @Serializable
    public class Url(
        /**
         * URI to which the user will be redirected after the homeserver has
         * authenticated the user with SSO.
         */
        public val redirectUrl: String
    )
}
