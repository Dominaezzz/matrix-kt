package io.github.matrixkt.models.events.contents.room

import io.github.matrixkt.models.events.HistoryVisibility
import io.github.matrixkt.models.events.contents.Content
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HistoryVisibilityContent(
    /**
     * Who can see the room history. One of: ["invited", "joined", "shared", "world_readable"]
     */
    @SerialName("history_visibility")
    val historyVisibility: HistoryVisibility
) : Content()
