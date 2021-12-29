package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Gets information about the server's supported feature set
 * and other relevant capabilities.
 */
public class GetCapabilities(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetCapabilities.Url, Nothing, GetCapabilities.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/capabilities")
    @Serializable
    public class Url

    @Serializable
    public class Response(
        /**
         * The custom capabilities the server supports, using the
         * Java package naming convention.
         */
        public val capabilities: Map<String, JsonObject>
    )
}
