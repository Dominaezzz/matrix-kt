package io.github.matrixkt.events.contents

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The list of user IDs typing in this room, if any.
 */
@SerialName("m.typing")
@Serializable
public data class TypingContent(
    /**
     * The list of user IDs typing in this room, if any.
     */
    @SerialName("user_ids")
    val userIds: List<String>
)
