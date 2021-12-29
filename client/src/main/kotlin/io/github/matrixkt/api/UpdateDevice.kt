package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Updates the metadata on the given device.
 */
public class UpdateDevice(
    public override val url: Url,
    /**
     * New information for the device.
     */
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Put, UpdateDevice.Url, UpdateDevice.Body, Unit> {
    @Resource("_matrix/client/r0/devices/{deviceId}")
    @Serializable
    public class Url(
        /**
         * The device to update.
         */
        public val deviceId: String
    )

    @Serializable
    public class Body(
        /**
         * The new display name for this device. If not given, the
         * display name is unchanged.
         */
        @SerialName("display_name")
        public val displayName: String? = null
    )
}
