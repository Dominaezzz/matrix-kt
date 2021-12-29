package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Binds a 3PID to the user's account through the specified identity server.
 *
 * Homeservers should not prevent this request from succeeding if another user
 * has bound the 3PID. Homeservers should simply proxy any errors received by
 * the identity server to the caller.
 *
 * Homeservers should track successful binds so they can be unbound later.
 */
public class Bind3PID(
    public override val url: Url,
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, Bind3PID.Url, Bind3PID.Body, Unit> {
    @Resource("_matrix/client/r0/account/3pid/bind")
    @Serializable
    public class Url

    @Serializable
    public class Body(
        /**
         * The client secret used in the session with the identity server.
         */
        @SerialName("client_secret")
        public val clientSecret: String,
        /**
         * An access token previously registered with the identity server.
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
}
