package io.github.matrixkt.api

import io.github.matrixkt.models.push.PushCondition
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * This endpoint allows the creation, modification and deletion of pushers
 * for this user ID. The behaviour of this endpoint varies depending on the
 * values in the JSON body.
 *
 * When creating push rules, they MUST be enabled by default.
 */
public class SetPushRule(
    public override val url: Url,
    /**
     * The push rule data. Additional top-level keys may be present depending
     * on the parameters for the rule ``kind``.
     */
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Put, SetPushRule.Url, SetPushRule.Body, Unit> {
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
        public val kind: String,
        /**
         * The identifier for the rule.
         */
        public val ruleId: String,
        /**
         * Use 'before' with a ``rule_id`` as its value to make the new rule the
         * next-most important rule with respect to the given user defined rule.
         * It is not possible to add a rule relative to a predefined server rule.
         */
        public val before: String? = null,
        /**
         * This makes the new rule the next-less important rule relative to the
         * given user defined rule. It is not possible to add a rule relative
         * to a predefined server rule.
         */
        public val after: String? = null
    )

    @Serializable
    public class Body(
        /**
         * The action(s) to perform when the conditions for this rule are met.
         */
        public val actions: List<String>,
        /**
         * The conditions that must hold true for an event in order for a
         * rule to be applied to an event. A rule with no conditions
         * always matches. Only applicable to ``underride`` and ``override`` rules.
         */
        public val conditions: List<PushCondition>? = null,
        /**
         * Only applicable to ``content`` rules. The glob-style pattern to match against.
         */
        public val pattern: String? = null
    )
}
