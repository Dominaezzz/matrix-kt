package io.github.matrixkt.api

import io.github.matrixkt.models.Device
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.github.matrixkt.utils.resource.Resource
import kotlinx.serialization.Serializable

/**
 * Gets information on a single device, by device id.
 */
public class GetDevice(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetDevice.Url, Any?, Device> {
    public override val body: Any?
        get() = null

    @Resource("/_matrix/client/r0/devices/{deviceId}")
    @Serializable
    public class Url(
        /**
         * The device to retrieve.
         */
        public val deviceId: String
    )
}
