package io.github.matrixkt.events.push

import kotlinx.serialization.Serializable

/**
 * The global ruleset.
 */
@Serializable
public data class Ruleset(
    val content: List<PushRule> = emptyList(),
    val override: List<PushRule> = emptyList(),
    val room: List<PushRule> = emptyList(),
    val sender: List<PushRule> = emptyList(),
    val underride: List<PushRule> = emptyList()
)
