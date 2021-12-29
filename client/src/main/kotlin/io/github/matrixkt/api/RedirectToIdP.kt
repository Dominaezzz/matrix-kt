package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * This endpoint is the same as `/login/sso/redirect`, though with an
 * IdP ID from the original `identity_providers` array to inform the
 * server of which IdP the client/user would like to continue with.
 *
 * The server MUST respond with an HTTP redirect to the SSO interface
 * for that IdP.
 */
public class RedirectToIdP(
    public override val url: Url
) : MatrixRpc<RpcMethod.Get, RedirectToIdP.Url, Nothing, Nothing> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/login/sso/redirect/{idpId}")
    @Serializable
    public class Url(
        /**
         * The `id` of the IdP from the `m.login.sso` `identity_providers`
         * array denoting the user's selection.
         */
        public val idpId: String,
        /**
         * URI to which the user will be redirected after the homeserver has
         * authenticated the user with SSO.
         */
        public val redirectUrl: String
    )
}
