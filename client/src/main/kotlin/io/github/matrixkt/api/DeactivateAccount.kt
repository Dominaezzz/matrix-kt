package io.github.matrixkt.api

import io.github.matrixkt.models.AuthenticationData
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Deactivate the user's account, removing all ability for the user to
 * login again.
 *
 * This API endpoint uses the `User-Interactive Authentication API`_.
 *
 * An access token should be submitted to this endpoint if the client has
 * an active session.
 *
 * The homeserver may change the flows available depending on whether a
 * valid access token is provided.
 *
 * Unlike other endpoints, this endpoint does not take an ``id_access_token``
 * parameter because the homeserver is expected to sign the request to the
 * identity server instead.
 */
public class DeactivateAccount(
    public override val url: Url,
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, DeactivateAccount.Url, DeactivateAccount.Body, DeactivateAccount.Response> {
    @Resource("_matrix/client/r0/account/deactivate")
    @Serializable
    public class Url

    @Serializable
    public class Body(
        /**
         * Additional authentication information for the user-interactive authentication API.
         */
        public val auth: AuthenticationData? = null,
        /**
         * The identity server to unbind all of the user's 3PIDs from.
         * If not provided, the homeserver MUST use the ``id_server``
         * that was originally use to bind each identifier. If the
         * homeserver does not know which ``id_server`` that was,
         * it must return an ``id_server_unbind_result`` of
         * ``no-support``.
         */
        @SerialName("id_server")
        public val idServer: String? = null
    )

    @Serializable
    public class Response(
        /**
         * An indicator as to whether or not the homeserver was able to unbind
         * the user's 3PIDs from the identity server(s). ``success`` indicates
         * that all identifiers have been unbound from the identity server while
         * ``no-support`` indicates that one or more identifiers failed to unbind
         * due to the identity server refusing the request or the homeserver
         * being unable to determine an identity server to unbind from. This
         * must be ``success`` if the homeserver has no identifiers to unbind
         * for the user.
         */
        @SerialName("id_server_unbind_result")
        public val idServerUnbindResult: String
    )
}
