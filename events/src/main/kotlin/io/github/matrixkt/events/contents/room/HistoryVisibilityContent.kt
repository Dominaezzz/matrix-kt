package io.github.matrixkt.events.contents.room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This event controls whether a user can see the events that happened in a room from before they joined.
 */
@SerialName("m.room.history_visibility")
@Serializable
public data class HistoryVisibilityContent(
    /**
     * Who can see the room history. One of: ["invited", "joined", "shared", "world_readable"]
     */
    @SerialName("history_visibility")
    val historyVisibility: HistoryVisibility
)

@Serializable
public enum class HistoryVisibility {
    @SerialName("invited")
    INVITED,

    @SerialName("joined")
    JOINED,

    @SerialName("shared")
    SHARED,

    @SerialName("world_readable")
    WORLD_READABLE;
}
