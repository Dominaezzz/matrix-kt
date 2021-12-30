package io.github.matrixkt.clientserver.api

import io.github.matrixkt.clientserver.models.thirdparty.Protocol
import io.github.matrixkt.clientserver.MatrixRpc
import io.github.matrixkt.clientserver.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Fetches the metadata from the homeserver about a particular third party protocol.
 */
public class GetProtocolMetadata(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetProtocolMetadata.Url, Nothing, Protocol> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/thirdparty/protocol/{protocol}")
    @Serializable
    public class Url(
        /**
         * The name of the protocol.
         */
        public val protocol: String
    )
}