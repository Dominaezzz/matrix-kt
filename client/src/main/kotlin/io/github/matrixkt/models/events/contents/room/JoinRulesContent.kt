package io.github.matrixkt.models.events.contents.room

import io.github.matrixkt.models.events.JoinRule
import io.github.matrixkt.models.events.contents.Content
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JoinRulesContent(
    /**
     * The type of rules used for users wishing to join this room.
     * One of: ["public", "knock", "invite", "private"]
     */
    @SerialName("join_rule")
    val joinRule: JoinRule
) : Content()
