package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Gets the versions of the specification supported by the server.
 *
 * Values will take the form `vX.Y` or `rX.Y.Z` in historical cases. See
 * [the Specification Versioning](../#specification-versions) for more
 * information.
 *
 * The server may additionally advertise experimental features it supports
 * through `unstable_features`. These features should be namespaced and
 * may optionally include version information within their name if desired.
 * Features listed here are not for optionally toggling parts of the Matrix
 * specification and should only be used to advertise support for a feature
 * which has not yet landed in the spec. For example, a feature currently
 * undergoing the proposal process may appear here and eventually be taken
 * off this list once the feature lands in the spec and the server deems it
 * reasonable to do so. Servers may wish to keep advertising features here
 * after they've been released into the spec to give clients a chance to
 * upgrade appropriately. Additionally, clients should avoid using unstable
 * features in their stable releases.
 */
public class GetVersions(
    public override val url: Url
) : MatrixRpc<RpcMethod.Get, GetVersions.Url, Nothing, GetVersions.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/versions")
    @Serializable
    public class Url

    @Serializable
    public class Response(
        /**
         * Experimental features the server supports. Features not listed here,
         * or the lack of this property all together, indicate that a feature is
         * not supported.
         */
        @SerialName("unstable_features")
        public val unstableFeatures: Map<String, Boolean>? = null,
        /**
         * The supported versions.
         */
        public val versions: List<String>
    )
}
