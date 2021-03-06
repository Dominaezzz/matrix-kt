package io.github.matrixkt.api

import io.github.matrixkt.models.Device
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.github.matrixkt.utils.resource.Resource
import kotlinx.serialization.Serializable

/**
 * Gets information about all devices for the current user.
 */
public class GetDevices(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetDevices.Url, Any?, GetDevices.Response> {
    public override val body: Any?
        get() = null

    @Resource("/_matrix/client/r0/devices")
    @Serializable
    public class Url

    @Serializable
    public class Response(
        /**
         * A list of all registered devices for this user.
         */
        public val devices: List<Device>? = null
    )
}