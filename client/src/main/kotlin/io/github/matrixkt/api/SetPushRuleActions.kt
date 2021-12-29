package io.github.matrixkt.api

import io.github.matrixkt.models.push.PushRuleKind
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * This endpoint allows clients to change the actions of a push rule.
 * This can be used to change the actions of builtin rules.
 */
public class SetPushRuleActions(
    public override val url: Url,
    /**
     * The action(s) to perform when the conditions for this rule are met.
     */
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Put, SetPushRuleActions.Url, SetPushRuleActions.Body, Unit>
        {
            @Resource("_matrix/client/r0/pushrules/{scope}/{kind}/{ruleId}/actions")
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

    @Serializable
    public class Body(
        /**
         * The action(s) to perform for this rule.
         */
        public val actions: List<String>
    )
}