package io.github.matrixkt.models.events.contents.room

import io.github.matrixkt.models.events.JoinRule
import io.github.matrixkt.models.events.contents.Content
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A room may be `public` meaning anyone can join the room without any prior action.
 * Alternatively, it can be `invite` meaning that a user who wishes to join the room
 * must first receive an invite to the room from someone already inside of the room.
 * Currently, `knock` and `private` are reserved keywords which are not implemented.
 */
@SerialName("m.room.join_rules")
@Serializable
data class JoinRulesContent(
    /**
     * The type of rules used for users wishing to join this room.
     * One of: ["public", "knock", "invite", "private"]
     */
    @SerialName("join_rule")
    val joinRule: JoinRule
) : Content()
