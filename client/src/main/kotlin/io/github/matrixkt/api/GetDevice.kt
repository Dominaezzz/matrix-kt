package io.github.matrixkt.api

import io.github.matrixkt.models.Device
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Gets information on a single device, by device id.
 */
public class GetDevice(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetDevice.Url, Nothing, Device> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/devices/{deviceId}")
    @Serializable
    public class Url(
        /**
         * The device to retrieve.
         */
        public val deviceId: String
    )
}
