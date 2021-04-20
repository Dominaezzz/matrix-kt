package io.github.matrixkt.api

import io.github.matrixkt.models.thirdparty.Location
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.github.matrixkt.utils.resource.Resource
import kotlinx.serialization.Serializable

/**
 * Retrieve an array of third party network locations from a Matrix room
 * alias.
 */
public class QueryLocationByAlias(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, QueryLocationByAlias.Url, Any?, List<Location>> {
    public override val body: Any?
        get() = null

    @Resource("/_matrix/client/r0/thirdparty/location")
    @Serializable
    public class Url(
        /**
         * The Matrix room alias to look up.
         */
        public val alias: String
    )
}
