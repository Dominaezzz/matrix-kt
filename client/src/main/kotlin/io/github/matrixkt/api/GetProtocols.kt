package io.github.matrixkt.api

import io.github.matrixkt.models.thirdparty.Protocol
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Fetches the overall metadata about protocols supported by the
 * homeserver. Includes both the available protocols and all fields
 * required for queries against each protocol.
 */
public class GetProtocols(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetProtocols.Url, Nothing, Map<String, Protocol>> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/thirdparty/protocols")
    @Serializable
    public class Url
}
