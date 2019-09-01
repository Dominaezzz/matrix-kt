package io.github.matrixkt.models.events.contents

import io.github.matrixkt.models.events.HistoryVisibility
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomHistoryVisibilityContent(
    /**
     * Who can see the room history. One of: ["invited", "joined", "shared", "world_readable"]
     */
    @SerialName("history_visibility")
    val historyVisibility: HistoryVisibility
) : Content()
