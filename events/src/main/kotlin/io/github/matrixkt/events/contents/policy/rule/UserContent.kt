package io.github.matrixkt.events.contents.policy.rule

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A moderation policy rule which affects users.
 */
@SerialName("m.policy.rule.user")
@Serializable
public data class UserContent(
    /**
     * The entity affected by this rule.
     * Glob characters ``*`` and ``?`` can be used to match zero or more and one or more characters respectively.
     */
    val entity: String,

    /**
     * The suggested action to take. Currently only ``m.ban`` is specified.
     */
    val recommendation: String,

    /**
     * The human-readable description for the [recommendation].
     */
    val reason: String
)
