package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.github.matrixkt.utils.resource.Resource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Gets the versions of the specification supported by the server.
 *
 * Values will take the form ``rX.Y.Z``.
 *
 * Only the latest ``Z`` value will be reported for each supported ``X.Y`` value.
 * i.e. if the server implements ``r0.0.0``, ``r0.0.1``, and ``r1.2.0``, it will report ``r0.0.1``
 * and ``r1.2.0``.
 *
 * The server may additionally advertise experimental features it supports
 * through ``unstable_features``. These features should be namespaced and
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
) : MatrixRpc<RpcMethod.Get, GetVersions.Url, Any?, GetVersions.Response> {
    public override val body: Any?
        get() = null

    @Resource("/_matrix/client/versions")
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
