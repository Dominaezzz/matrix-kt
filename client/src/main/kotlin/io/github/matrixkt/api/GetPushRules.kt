package io.github.matrixkt.api

import io.github.matrixkt.models.push.Ruleset
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Retrieve all push rulesets for this user. Clients can "drill-down" on
 * the rulesets by suffixing a ``scope`` to this path e.g.
 * ``/pushrules/global/``. This will return a subset of this data under the
 * specified key e.g. the ``global`` key.
 */
public class GetPushRules(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetPushRules.Url, Nothing, GetPushRules.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/pushrules/")
    @Serializable
    public class Url

    @Serializable
    public class Response(
        /**
         * The global ruleset.
         */
        public val global: Ruleset
    )
}
