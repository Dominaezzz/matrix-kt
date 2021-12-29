package io.github.matrixkt.api

import io.github.matrixkt.models.Medium
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Removes a user's third party identifier from the provided identity server
 * without removing it from the homeserver.
 *
 * Unlike other endpoints, this endpoint does not take an ``id_access_token``
 * parameter because the homeserver is expected to sign the request to the
 * identity server instead.
 */
public class Unbind3pidFromAccount(
    public override val url: Url,
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, Unbind3pidFromAccount.Url, Unbind3pidFromAccount.Body,
        Unbind3pidFromAccount.Response> {
    @Resource("_matrix/client/r0/account/3pid/unbind")
    @Serializable
    public class Url

    @Serializable
    public class Body(
        /**
         * The third party address being removed.
         */
        public val address: String,
        /**
         * The identity server to unbind from. If not provided, the homeserver
         * MUST use the ``id_server`` the identifier was added through. If the
         * homeserver does not know the original ``id_server``, it MUST return
         * a ``id_server_unbind_result`` of ``no-support``.
         */
        @SerialName("id_server")
        public val idServer: String? = null,
        /**
         * The medium of the third party identifier being removed.
         */
        public val medium: Medium
    )

    @Serializable
    public class Response(
        /**
         * An indicator as to whether or not the identity server was able to unbind
         * the 3PID. ``success`` indicates that the identity server has unbound the
         * identifier whereas ``no-support`` indicates that the identity server
         * refuses to support the request or the homeserver was not able to determine
         * an identity server to unbind from.
         */
        @SerialName("id_server_unbind_result")
        public val idServerUnbindResult: String
    )

    // @Serializable
    // public enum class IdServerUnbindResult {
    //     @SerialName("no-support")
    //     NO_SUPPORT,
    //     @SerialName("success")
    //     SUCCESS;
    // }
}
