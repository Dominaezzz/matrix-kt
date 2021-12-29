package io.github.matrixkt.api

import io.github.matrixkt.models.push.PushRuleKind
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * This endpoint removes the push rule defined in the path.
 */
public class DeletePushRule(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Delete, DeletePushRule.Url, Nothing, Unit> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/pushrules/{scope}/{kind}/{ruleId}")
    @Serializable
    public class Url(
        /**
         * ``global`` to specify global rules.
         */
        public val scope: String,
        /**
         * The kind of rule
         */
        public val kind: PushRuleKind,
        /**
         * The identifier for the rule.
         */
        public val ruleId: String
    )
}
