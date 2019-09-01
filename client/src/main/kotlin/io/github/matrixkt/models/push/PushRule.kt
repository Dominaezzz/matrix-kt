package io.github.matrixkt.models.push

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class PushRule(
    /**
     * The actions to perform when this rule is matched.
     */
    val actions: List<JsonElement>,

    /**
     * The conditions that must hold true for an event in order for a rule to be applied to an event.
     * A rule with no conditions always matches.
     * Only applicable to ``underride`` and ``override`` rules.
     */
    val conditions: List<PushCondition>? = null,

    /**
     * Whether this is a default rule, or has been set explicitly.
     */
    val default: Boolean,

    /**
     * Whether the push rule is enabled or not.
     */
    val enabled: Boolean,

    /**
     * The glob-style pattern to match against.
     * Only applicable to ``content`` rules.
     */
    val pattern: String? = null,

    /**
     * The ID of this rule.
     */
    @SerialName("rule_id")
    val ruleId: String
)
