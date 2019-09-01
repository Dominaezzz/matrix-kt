package io.github.matrixkt.models.push

import kotlinx.serialization.Serializable

/**
 * The global ruleset.
 */
@Serializable
data class RuleSet(
    val content: List<PushRule>? = null,
    val override: List<PushRule>? = null,
    val room: List<PushRule>? = null,
    val sender: List<PushRule>? = null,
    val underride: List<PushRule>? = null
)
