package io.github.matrixkt.api

import io.github.matrixkt.models.ThirdPartyIdentifier
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Gets a list of the third party identifiers that the homeserver has
 * associated with the user's account.
 *
 * This is *not* the same as the list of third party identifiers bound to
 * the user's Matrix ID in identity servers.
 *
 * Identifiers in this list may be used by the homeserver as, for example,
 * identifiers that it will accept to reset the user's account password.
 */
public class GetAccount3PIDs(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetAccount3PIDs.Url, Nothing, GetAccount3PIDs.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/account/3pid")
    @Serializable
    public class Url

    @Serializable
    public class Response(
        public val threepids: List<ThirdPartyIdentifier> = emptyList()
    )
}
