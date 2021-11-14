package io.github.matrixkt.api

import io.github.matrixkt.models.Medium
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.github.matrixkt.utils.resource.Resource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Removes a third party identifier from the user's account. This might not
 * cause an unbind of the identifier from the identity server.
 *
 * Unlike other endpoints, this endpoint does not take an ``id_access_token``
 * parameter because the homeserver is expected to sign the request to the
 * identity server instead.
 */
public class Delete3pidFromAccount(
    public override val url: Url,
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, Delete3pidFromAccount.Url, Delete3pidFromAccount.Body,
        Delete3pidFromAccount.Response> {
    @Resource("/_matrix/client/r0/account/3pid/delete")
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
         * An indicator as to whether or not the homeserver was able to unbind
         * the 3PID from the identity server. ``success`` indicates that the
         * indentity server has unbound the identifier whereas ``no-support``
         * indicates that the identity server refuses to support the request
         * or the homeserver was not able to determine an identity server to
         * unbind from.
         */
        @SerialName("id_server_unbind_result")
        public val idServerUnbindResult: String
    )
}