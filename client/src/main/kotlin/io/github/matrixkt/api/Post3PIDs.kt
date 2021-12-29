package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Adds contact information to the user's account.
 *
 * This endpoint is deprecated in favour of the more specific ``/3pid/add``
 * and ``/3pid/bind`` endpoints.
 *
 * .. Note::
 *    Previously this endpoint supported a ``bind`` parameter. This parameter
 *    has been removed, making this endpoint behave as though it was ``false``.
 *    This results in this endpoint being an equivalent to ``/3pid/bind`` rather
 *    than dual-purpose.
 */
@Suppress("DEPRECATION")
@Deprecated("This endpoint has been deprecated.")
public class Post3PIDs(
    public override val url: Url,
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, Post3PIDs.Url, Post3PIDs.Body, Post3PIDs.Response> {
    @Resource("_matrix/client/r0/account/3pid")
    @Serializable
    public class Url

    @Serializable
    public class ThreePidCredentials(
        /**
         * The client secret used in the session with the identity server.
         */
        @SerialName("client_secret")
        public val clientSecret: String,
        /**
         * An access token previously registered with the identity server. Servers
         * can treat this as optional to distinguish between r0.5-compatible clients
         * and this specification version.
         */
        @SerialName("id_access_token")
        public val idAccessToken: String,
        /**
         * The identity server to use.
         */
        @SerialName("id_server")
        public val idServer: String,
        /**
         * The session identifier given by the identity server.
         */
        public val sid: String
    )

    @Serializable
    public class Body(
        /**
         * The third party credentials to associate with the account.
         */
        @SerialName("three_pid_creds")
        public val threePidCreds: ThreePidCredentials
    )

    @Serializable
    public class Response(
        /**
         * An optional field containing a URL where the client must
         * submit the validation token to, with identical parameters
         * to the Identity Service API's ``POST
         * /validate/email/submitToken`` endpoint (without the requirement
         * for an access token). The homeserver must send this token to the
         * user (if applicable), who should then be prompted to provide it
         * to the client.
         *
         * If this field is not present, the client can assume that
         * verification will happen without the client's involvement
         * provided the homeserver advertises this specification version
         * in the ``/versions`` response (ie: r0.5.0).
         */
        @SerialName("submit_url")
        public val submitUrl: String? = null
    )
}
