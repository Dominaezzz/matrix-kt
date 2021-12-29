package io.github.matrixkt.api

import io.github.matrixkt.models.push.PushRule
import io.github.matrixkt.models.push.PushRuleKind
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Retrieve a single specified push rule.
 */
public class GetPushRule(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetPushRule.Url, Nothing, PushRule> {
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
