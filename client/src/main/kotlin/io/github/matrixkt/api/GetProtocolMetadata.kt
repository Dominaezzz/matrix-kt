package io.github.matrixkt.api

import io.github.matrixkt.models.thirdparty.Protocol
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.github.matrixkt.utils.resource.Resource
import kotlinx.serialization.Serializable

/**
 * Fetches the metadata from the homeserver about a particular third party protocol.
 */
public class GetProtocolMetadata(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetProtocolMetadata.Url, Any?, Protocol> {
    public override val body: Any?
        get() = null

    @Resource("/_matrix/client/r0/thirdparty/protocol/{protocol}")
    @Serializable
    public class Url(
        /**
         * The name of the protocol.
         */
        public val protocol: String
    )
}