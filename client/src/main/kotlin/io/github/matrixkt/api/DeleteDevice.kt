package io.github.matrixkt.api

import io.github.matrixkt.models.AuthenticationData
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * This API endpoint uses the `User-Interactive Authentication API`_.
 *
 * Deletes the given device, and invalidates any access token associated with it.
 */
public class DeleteDevice(
    public override val url: Url,
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Delete, DeleteDevice.Url, DeleteDevice.Body, Unit> {
    @Resource("_matrix/client/r0/devices/{deviceId}")
    @Serializable
    public class Url(
        /**
         * The device to delete.
         */
        public val deviceId: String
    )

    @Serializable
    public class Body(
        /**
         * Additional authentication information for the
         * user-interactive authentication API.
         */
        public val auth: AuthenticationData? = null
    )
}
