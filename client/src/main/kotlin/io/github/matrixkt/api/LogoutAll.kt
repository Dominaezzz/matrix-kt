package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Invalidates all access tokens for a user, so that they can no longer be used for
 * authorization. This includes the access token that made this request. All devices
 * for the user are also deleted. `Device keys <#device-keys>`_ for the device are
 * deleted alongside the device.
 *
 * This endpoint does not require UI authorization because UI authorization is
 * designed to protect against attacks where the someone gets hold of a single access
 * token then takes over the account. This endpoint invalidates all access tokens for
 * the user, including the token used in the request, and therefore the attacker is
 * unable to take over the account in this way.
 */
public class LogoutAll(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Post, LogoutAll.Url, Nothing, Unit> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/logout/all")
    @Serializable
    public class Url
}
