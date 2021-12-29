package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Invalidates an existing access token, so that it can no longer be used for
 * authorization. The device associated with the access token is also deleted.
 * `Device keys <#device-keys>`_ for the device are deleted alongside the device.
 */
public class Logout(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Post, Logout.Url, Nothing, Unit> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/logout")
    @Serializable
    public class Url
}
