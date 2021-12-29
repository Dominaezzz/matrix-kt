package io.github.matrixkt.events.push

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
public data class PushRule(
    /**
     * The actions to perform when this rule is matched.
     */
    public val actions: List<JsonElement>,

    /**
     * The conditions that must hold true for an event in order for a rule to be applied to an event.
     * A rule with no conditions always matches.
     * Only applicable to ``underride`` and ``override`` rules.
     */
    public val conditions: List<PushCondition> = emptyList(),

    /**
     * Whether this is a default rule, or has been set explicitly.
     */
    public val default: Boolean,

    /**
     * Whether the push rule is enabled or not.
     */
    public val enabled: Boolean,

    /**
     * The glob-style pattern to match against.
     * Only applicable to ``content`` rules.
     */
    public val pattern: String? = null,

    /**
     * The ID of this rule.
     */
    @SerialName("rule_id")
    public val ruleId: String
)
