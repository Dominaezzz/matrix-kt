package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.github.matrixkt.utils.resource.Resource
import kotlinx.serialization.Serializable

/**
 * This API provides credentials for the client to use when initiating
 * calls.
 */
public class GetTurnServer(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetTurnServer.Url, Any?, GetTurnServer.Response> {
    public override val body: Any?
        get() = null

    @Resource("/_matrix/client/r0/voip/turnServer")
    @Serializable
    public class Url

    @Serializable
    public class Response(
        /**
         * The password to use.
         */
        public val password: String,
        /**
         * The time-to-live in seconds
         */
        public val ttl: Long,
        /**
         * A list of TURN URIs
         */
        public val uris: List<String>,
        /**
         * The username to use.
         */
        public val username: String
    )
}
