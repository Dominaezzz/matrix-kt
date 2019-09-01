package io.github.matrixkt.models.events.contents

import io.github.matrixkt.models.events.JoinRule
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomJoinRulesContent(
    /**
     * The type of rules used for users wishing to join this room.
     * One of: ["public", "knock", "invite", "private"]
     */
    @SerialName("join_rule")
    val joinRule: JoinRule
) : Content()
