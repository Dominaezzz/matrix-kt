package io.github.matrixkt.api

import io.github.matrixkt.models.push.PushRuleKind
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * This endpoint allows clients to enable or disable the specified push rule.
 */
public class SetPushRuleEnabled(
    public override val url: Url,
    /**
     * Whether the push rule is enabled or not.
     */
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Put, SetPushRuleEnabled.Url, SetPushRuleEnabled.Body, Unit>
        {
            @Resource("_matrix/client/r0/pushrules/{scope}/{kind}/{ruleId}/enabled")
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
         * Whether the push rule is enabled or not.
         */
        public val enabled: Boolean
    )
}
