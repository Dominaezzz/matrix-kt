package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.github.matrixkt.utils.resource.Resource
import kotlinx.serialization.Serializable

/**
 * Invalidates an existing access token, so that it can no longer be used for
 * authorization. The device associated with the access token is also deleted.
 * `Device keys <#device-keys>`_ for the device are deleted alongside the device.
 */
public class Logout(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Post, Logout.Url, Any?, Unit> {
    public override val body: Any?
        get() = null

    @Resource("/_matrix/client/r0/logout")
    @Serializable
    public class Url
}
