package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Gets an OpenID token object that the requester may supply to another
 * service to verify their identity in Matrix. The generated token is only
 * valid for exchanging for user information from the federation API for
 * OpenID.
 *
 * The access token generated is only valid for the OpenID API. It cannot
 * be used to request another OpenID access token or call ``/sync``, for
 * example.
 */
public class RequestOpenIdToken(
    public override val url: Url,
    /**
     * An empty object. Reserved for future expansion.
     */
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, RequestOpenIdToken.Url, RequestOpenIdToken.Body, RequestOpenIdToken.Response>
        {
            @Resource("_matrix/client/r0/user/{userId}/openid/request_token")
    @Serializable
    public class Url(
        /**
         * The user to request and OpenID token for. Should be the user who
         * is authenticated for the request.
         */
        public val userId: String
    )

    @Serializable
    public class Body

    @Serializable
    public class Response(
        /**
         * An access token the consumer may use to verify the identity of
         * the person who generated the token. This is given to the federation
         * API ``GET /openid/userinfo`` to verify the user's identity.
         */
        @SerialName("access_token")
        public val accessToken: String,
        /**
         * The number of seconds before this token expires and a new one must
         * be generated.
         */
        @SerialName("expires_in")
        public val expiresIn: Long,
        /**
         * The homeserver domain the consumer should use when attempting to
         * verify the user's identity.
         */
        @SerialName("matrix_server_name")
        public val matrixServerName: String,
        /**
         * The string ``Bearer``.
         */
        @SerialName("token_type")
        public val tokenType: String
    )
}
