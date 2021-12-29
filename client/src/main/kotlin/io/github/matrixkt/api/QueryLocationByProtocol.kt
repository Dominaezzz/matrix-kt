package io.github.matrixkt.api

import io.github.matrixkt.models.thirdparty.Location
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Requesting this endpoint with a valid protocol name results in a list
 * of successful mapping results in a JSON array. Each result contains
 * objects to represent the Matrix room or rooms that represent a portal
 * to this third party network. Each has the Matrix room alias string,
 * an identifier for the particular third party network protocol, and an
 * object containing the network-specific fields that comprise this
 * identifier. It should attempt to canonicalise the identifier as much
 * as reasonably possible given the network type.
 */
public class QueryLocationByProtocol(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, QueryLocationByProtocol.Url, Nothing, List<Location>> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/thirdparty/location/{protocol}")
    @Serializable
    public class Url(
        /**
         * The protocol used to communicate to the third party network.
         */
        public val protocol: String,
        /**
         * One or more custom fields to help identify the third party
         * location.
         */
        public val searchFields: String? = null
    )
}
