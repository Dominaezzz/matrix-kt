package io.github.matrixkt.api

import io.github.matrixkt.models.AuthenticationData
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Changes the password for an account on this homeserver.
 *
 * This API endpoint uses the `User-Interactive Authentication API`_ to
 * ensure the user changing the password is actually the owner of the
 * account.
 *
 * An access token should be submitted to this endpoint if the client has
 * an active session.
 *
 * The homeserver may change the flows available depending on whether a
 * valid access token is provided. The homeserver SHOULD NOT revoke the
 * access token provided in the request. Whether other access tokens for
 * the user are revoked depends on the request parameters.
 */
public class ChangePassword(
    public override val url: Url,
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, ChangePassword.Url, ChangePassword.Body, Unit> {
    @Resource("_matrix/client/r0/account/password")
    @Serializable
    public class Url

    @Serializable
    public class Body(
        /**
         * Additional authentication information for the user-interactive authentication API.
         */
        public val auth: AuthenticationData? = null,
        /**
         * Whether the user's other access tokens, and their associated devices, should be
         * revoked if the request succeeds. Defaults to true.
         *
         * When ``false``, the server can still take advantage of `the soft logout method
         * <#soft-logout>`_
         * for the user's remaining devices.
         */
        @SerialName("logout_devices")
        public val logoutDevices: Boolean? = null,
        /**
         * The new password for the account.
         */
        @SerialName("new_password")
        public val newPassword: String
    )
}
