package io.github.matrixkt.api

import io.github.matrixkt.models.AuthenticationData
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * This API endpoint uses the `User-Interactive Authentication API`_.
 *
 * Deletes the given devices, and invalidates any access token associated with them.
 */
public class DeleteDevices(
    public override val url: Url,
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, DeleteDevices.Url, DeleteDevices.Body, Unit> {
    @Resource("_matrix/client/r0/delete_devices")
    @Serializable
    public class Url

    @Serializable
    public class Body(
        /**
         * Additional authentication information for the
         * user-interactive authentication API.
         */
        public val auth: AuthenticationData? = null,
        /**
         * The list of device IDs to delete.
         */
        public val devices: List<String>
    )
}
