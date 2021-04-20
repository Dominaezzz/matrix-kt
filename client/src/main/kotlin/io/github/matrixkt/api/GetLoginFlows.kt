package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.github.matrixkt.utils.resource.Resource
import kotlinx.serialization.Serializable

/**
 * Gets the homeserver's supported login types to authenticate users. Clients
 * should pick one of these and supply it as the ``type`` when logging in.
 */
public class GetLoginFlows(
    public override val url: Url
) : MatrixRpc<RpcMethod.Get, GetLoginFlows.Url, Any?, GetLoginFlows.Response> {
    public override val body: Any?
        get() = null

    @Resource("/_matrix/client/r0/login")
    @Serializable
    public class Url

    @Serializable
    public class LoginFlow(
        /**
         * The login type. This is supplied as the ``type`` when
         * logging in.
         */
        public val type: String? = null
    )

    @Serializable
    public class Response(
        /**
         * The homeserver's supported login types
         */
        public val flows: List<LoginFlow> = emptyList()
    )
}
