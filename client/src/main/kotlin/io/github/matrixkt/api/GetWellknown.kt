package io.github.matrixkt.api

import io.github.matrixkt.models.wellknown.DiscoveryInformation
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Gets discovery information about the domain. The file may include
 * additional keys, which MUST follow the Java package naming convention,
 * e.g. ``com.example.myapp.property``. This ensures property names are
 * suitably namespaced for each application and reduces the risk of
 * clashes.
 *
 * Note that this endpoint is not necessarily handled by the homeserver,
 * but by another webserver, to be used for discovering the homeserver URL.
 */
public class GetWellknown(
    public override val url: Url
) : MatrixRpc<RpcMethod.Get, GetWellknown.Url, Nothing, DiscoveryInformation> {
    public override val body: Nothing
        get() = TODO()

    @Resource(".well-known/matrix/client")
    @Serializable
    public class Url
}
