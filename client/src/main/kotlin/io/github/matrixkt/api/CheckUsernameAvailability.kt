package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Checks to see if a username is available, and valid, for the server.
 *
 * The server should check to ensure that, at the time of the request, the
 * username requested is available for use. This includes verifying that an
 * application service has not claimed the username and that the username
 * fits the server's desired requirements (for example, a server could dictate
 * that it does not permit usernames with underscores).
 *
 * Matrix clients may wish to use this API prior to attempting registration,
 * however the clients must also be aware that using this API does not normally
 * reserve the username. This can mean that the username becomes unavailable
 * between checking its availability and attempting to register it.
 */
public class CheckUsernameAvailability(
    public override val url: Url
) : MatrixRpc<RpcMethod.Get, CheckUsernameAvailability.Url, Nothing, CheckUsernameAvailability.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/register/available")
    @Serializable
    public class Url(
        /**
         * The username to check the availability of.
         */
        public val username: String
    )

    @Serializable
    public class Response(
        /**
         * A flag to indicate that the username is available. This should always
         * be ``true`` when the server replies with 200 OK.
         */
        public val available: Boolean? = null
    )
}
