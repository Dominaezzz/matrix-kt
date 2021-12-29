package io.github.matrixkt.api

import io.github.matrixkt.models.push.PushRuleKind
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * This endpoint get the actions for the specified push rule.
 */
public class GetPushRuleActions(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetPushRuleActions.Url, Nothing, GetPushRuleActions.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/pushrules/{scope}/{kind}/{ruleId}/actions")
    @Serializable
    public class Url(
        /**
         * Either ``global`` or ``device/<profile_tag>`` to specify global
         * rules or device rules for the given ``profile_tag``.
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

    @Serializable
    public class Response(
        /**
         * The action(s) to perform for this rule.
         */
        public val actions: List<String>
    )
}
