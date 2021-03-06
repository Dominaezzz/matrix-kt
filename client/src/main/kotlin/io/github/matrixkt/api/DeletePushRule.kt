package io.github.matrixkt.api

import io.github.matrixkt.models.push.PushRuleKind
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.github.matrixkt.utils.resource.Resource
import kotlinx.serialization.Serializable

/**
 * This endpoint removes the push rule defined in the path.
 */
public class DeletePushRule(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Delete, DeletePushRule.Url, Any?, Unit> {
    public override val body: Any?
        get() = null

    @Resource("/_matrix/client/r0/pushrules/{scope}/{kind}/{ruleId}")
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
