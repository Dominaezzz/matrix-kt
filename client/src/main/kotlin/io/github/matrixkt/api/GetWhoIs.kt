package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Gets information about a particular user.
 *
 * This API may be restricted to only be called by the user being looked
 * up, or by a server admin. Server-local administrator privileges are not
 * specified in this document.
 */
public class GetWhoIs(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetWhoIs.Url, Nothing, GetWhoIs.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/admin/whois/{userId}")
    @Serializable
    public class Url(
        /**
         * The user to look up.
         */
        public val userId: String
    )

    @Serializable
    public class ConnectionInfo(
        /**
         * Most recently seen IP address of the session.
         */
        public val ip: String? = null,
        /**
         * Unix timestamp that the session was last active.
         */
        @SerialName("last_seen")
        public val lastSeen: Long? = null,
        /**
         * User agent string last seen in the session.
         */
        @SerialName("user_agent")
        public val userAgent: String? = null
    )

    @Serializable
    public class SessionInfo(
        /**
         * Information particular connections in the session.
         */
        public val connections: List<ConnectionInfo> = emptyList()
    )

    @Serializable
    public class DeviceInfo(
        /**
         * A user's sessions (i.e. what they did with an access token from one login).
         */
        public val sessions: List<SessionInfo> = emptyList()
    )

    @Serializable
    public class Response(
        /**
         * Each key is an identifier for one of the user's devices.
         */
        public val devices: Map<String, DeviceInfo> = emptyMap(),
        /**
         * The Matrix user ID of the user.
         */
        @SerialName("user_id")
        public val userId: String? = null
    )
}
