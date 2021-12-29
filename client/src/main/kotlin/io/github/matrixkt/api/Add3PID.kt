package io.github.matrixkt.api

import io.github.matrixkt.models.AuthenticationData
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This API endpoint uses the `User-Interactive Authentication API`_.
 *
 * Adds contact information to the user's account. Homeservers should use 3PIDs added
 * through this endpoint for password resets instead of relying on the identity server.
 *
 * Homeservers should prevent the caller from adding a 3PID to their account if it has
 * already been added to another user's account on the homeserver.
 */
public class Add3PID(
    public override val url: Url,
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, Add3PID.Url, Add3PID.Body, Unit> {
    @Resource("_matrix/client/r0/account/3pid/add")
    @Serializable
    public class Url

    @Serializable
    public class Body(
        /**
         * Additional authentication information for the
         * user-interactive authentication API.
         */
        public val auth: AuthenticationData? = null,
        /**
         * The client secret used in the session with the homeserver.
         */
        @SerialName("client_secret")
        public val clientSecret: String,
        /**
         * The session identifier given by the homeserver.
         */
        public val sid: String
    )
}
